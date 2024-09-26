package com.aps.yinghai.dto;

import com.aps.yinghai.entity.BerthInfo;
import com.aps.yinghai.entity.BollardInfo;
import com.aps.yinghai.exception.ObjectCloneException;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
//@JsonIdentityInfo(generator= ObjectIdGenerators.IntSequenceGenerator.class, property="@id",scope = PlanningBerthDTO.class)
@Data
public class PlanningBerthDTO {

    private PlanningBerthDTO preBerth;
    private PlanningBerthDTO nextBerth;

    private BerthInfo berthInfo;
    private List<PlanningBollardDTO> bollardDTOList;


    public static PlanningBerthDTO packageBerthDTO(BerthInfo berthInfo, List<BollardInfo> bollardInfoList) {
        PlanningBerthDTO planningBerthDTO = new PlanningBerthDTO();
        planningBerthDTO.berthInfo = berthInfo;
        planningBerthDTO.bollardDTOList = bollardInfoList.stream().sorted(Comparator.comparing(t -> t.getBollardNo())).map(t -> PlanningBollardDTO.packageBollardDTO(t)).collect(Collectors.toList());
        return planningBerthDTO;
    }

    public boolean isD1() {
        return StringUtils.equals("D1", this.berthInfo.getBerthNo());
    }


    public boolean updateBerth(PlanningShipDTO planningShipDTO,int skipBollardCount) {
        // 需要克隆缆柱数据 否则排产失败时 揽柱的原始被占用时间数据会丢失
        List<PlanningBollardDTO> cloneBollardList = this.bollardDTOList.stream().skip(skipBollardCount).map(t -> {
            try {
                return (PlanningBollardDTO)t.clone();
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
                throw new ObjectCloneException("排产缆柱数据失败！",e);
            }
        }).collect(Collectors.toList());
        // 船舶长度120%
        BigDecimal shipRequireLength = planningShipDTO.getShipForecast().getShipLength().multiply(new BigDecimal("1.2"));
        int occupiedAmount = 0;
        List<PlanningBollardDTO> candidateBollardList = new ArrayList<>();
        // 当前所有占用的缆柱长度
        BigDecimal totalLength = BigDecimal.ZERO;
        // 是否已安排
        boolean planned = false;
        // 顺序查找空闲的缆柱
        seek:
        for (int i = 0; i < cloneBollardList.size(); ) {
            PlanningBollardDTO planningBollardDTO = cloneBollardList.get(i);
            // 找到第一个空闲的缆柱
            boolean free = planningBollardDTO.isFree(planningShipDTO.getReadyTime());
            // 找到空闲的缆柱后要开始选取后面空余的缆柱
            if (free) {
                // 每次开始匹配是否满足长度时都要初始化还原占用数据
                totalLength = BigDecimal.ZERO;
                candidateBollardList.clear();
                occupiedAmount = 0;
                plan:
                for (int j = i; j < cloneBollardList.size(); j++) {
                    PlanningBollardDTO freeBollard = cloneBollardList.get(j);
                    // 不管是被占用还是未被占用，都先加上
                    candidateBollardList.add(freeBollard);
                    // 累积缆柱长度 但是一般不累加第一个榄桩的长度 因为如果要累加第一个榄桩的长度需要将船排到前一个泊位
                    if (j>0)
                        totalLength = totalLength.add(freeBollard.getBollardInfo().getLastBollardDistance());

                    if (totalLength.compareTo(shipRequireLength) > 0) {
                        // 如果不是从第一个榄桩开始排的，那么可以将船的艏缆系到前一个被占用的缆上
                        if (i > 0) {
                            PlanningBollardDTO firstBollard = cloneBollardList.get(i - 1);
                            candidateBollardList.add(0, firstBollard);
                        }
                        // 更新占用时间
                        candidateBollardList.stream().forEach(t -> t.updateOccupiedTime(planningShipDTO.getLeaveTime()));
                        planned = true;

                    }
                    // 如果安排好了 停止查找
                    if (planned) {
                        break seek;
                    }

                    // 如果是空闲的
                    if (freeBollard.isFree(planningShipDTO.getReadyTime())) {
                        occupiedAmount++;
                        // 如果是空闲的则优先向后找本泊位的空闲缆柱
                        continue plan;
                    } else {
                        // 如果安排好了 停止查找
                        if (planned) {
                            break seek;
                        } else {
                            // 如果到了被占用的缆柱还不满足尝试向前交叉一根
                            if (i > 1) {
                                // 向前交叉一根缆柱
                                totalLength = totalLength.add(cloneBollardList.get(i - 1).getBollardInfo().getLastBollardDistance());
                                // 向前交叉一根满足的话
                                if (totalLength.compareTo(shipRequireLength) > 0) {
                                    // 前一根和交叉的那根都要被占用
                                    PlanningBollardDTO firstBollard = cloneBollardList.get(i - 2);
                                    PlanningBollardDTO secondBollard = cloneBollardList.get(i - 1);
                                    candidateBollardList.add(0, secondBollard);
                                    candidateBollardList.add(0, firstBollard);
                                    // 更新占用时间
                                    candidateBollardList.stream().forEach(t -> t.updateOccupiedTime(planningShipDTO.getLeaveTime()));

                                    planned = true;
                                    break seek;
                                }
                            }
                            break plan;

                        }

                    }
                }
                //上一次for循环累计长度不够 但是可以额外看一下能不能跨泊位停靠
                //如果包含第一个缆桩则往前探
                planned = trySniffForward(shipRequireLength,totalLength,candidateBollardList,planningShipDTO);
                if (planned)break seek;
                //如果包含最后一个榄桩则向后探 totalLength在trySniffForward中的变化由于引用传递所以并不准确所以需要在方法中用候选缆柱距离求和现算
                planned = trySniffBackward(shipRequireLength,candidateBollardList,planningShipDTO);
                if (planned)break seek;

                // 前面安排的缆柱长度加起来不够 直接跳过
                i += occupiedAmount;
            }
            i++;

        }

        if (planned){
            planningShipDTO.setOccupiedBollardList(candidateBollardList);
            rewriteBollardOccupyTime(candidateBollardList);
            return true;
        }else{
            return false;
        }
    }



    /**
     * 回写榄桩的被占用时间
     * @param candidateBollardList
     */
    private void rewriteBollardOccupyTime(List<PlanningBollardDTO> candidateBollardList) {
        // 将被占用的缆柱更新到原始数据上
        Map<String, PlanningBollardDTO> bollardDTOMap = this.bollardDTOList.stream().collect(Collectors.toMap(t -> t.getBollardInfo().getId(), t -> t));
        Map<String, PlanningBollardDTO> preBollardDTOMap = hasPreBerth()?this.preBerth.getBollardDTOList().stream().collect(Collectors.toMap(t -> t.getBollardInfo().getId(), t -> t)): Collections.EMPTY_MAP;
        Map<String, PlanningBollardDTO> nexBollardDTOMap = hasNextBerth()?this.nextBerth.getBollardDTOList().stream().collect(Collectors.toMap(t -> t.getBollardInfo().getId(), t -> t)): Collections.EMPTY_MAP;

        candidateBollardList.forEach(changed->{
            String id = changed.getBollardInfo().getId();
            PlanningBollardDTO original = bollardDTOMap.get(id);
            if (original==null){
                original = preBollardDTOMap.get(id);
            }
            if (original==null){
                original = nexBollardDTOMap.get(id);
            }
            if (original!=null)
            original.getBollardInfo().setOccupyUntil(changed.getBollardInfo().getOccupyUntil());
        });
    }

    private boolean trySniffBackward(BigDecimal shipRequireLength, List<PlanningBollardDTO> candidateBollardList, PlanningShipDTO shipDTO) {
        if (!CollectionUtils.isEmpty(candidateBollardList) && this.hasNextBerth()){
            // 所选的第一个榄桩的距离一般不算
            Optional<BigDecimal> optional = candidateBollardList.stream().map(t -> t.getBollardInfo().getLastBollardDistance()).reduce((b1, b2) -> b1.add(b2));
            if (!optional.isPresent() || optional.get().compareTo(BigDecimal.ZERO)<=0) {
                return false;
            }
            BigDecimal totalLength = optional.get();
            PlanningBollardDTO lastCandidate = candidateBollardList.get(candidateBollardList.size()-1);
            // 如果是最后一个榄柱才能允许向后一个泊位嗅探缆柱
            if (isLastBollard(lastCandidate.getBollardInfo().getId())){
                // 需要克隆缆柱数据 否则排产失败时 揽柱的原始被占用时间数据会丢失
                List<PlanningBollardDTO> cloneBollardList = this.nextBerth.getBollardDTOList().stream().map(t -> {
                    try {
                        return (PlanningBollardDTO)t.clone();
                    } catch (CloneNotSupportedException e) {
                        e.printStackTrace();
                        throw new ObjectCloneException("排产缆柱数据失败！",e);
                    }
                }).collect(Collectors.toList());


                // 开始加上后一个泊位的缆柱 从前往后
                for (int i = 0; i < cloneBollardList.size() ; i++) {
                    PlanningBollardDTO nextBollard = cloneBollardList.get(i);
                    // 这个榄桩未被占用
                    if (nextBollard.isFree(shipDTO.getReadyTime())){
                        totalLength = totalLength.add(nextBollard.getBollardInfo().getLastBollardDistance());
                        if (totalLength.compareTo(shipRequireLength)>0){
                            candidateBollardList.add(nextBollard);
                            nextBollard.getBollardInfo().setOccupyUntil(shipDTO.getLeaveTime());
                            return true;
                        }
                    }else{
                        // 向后嗅探到了被占用的榄桩了 说明排不上了
                        return false;
                    }

                }


            }
        }
        return false;

    }

    /**
     * 试着向前一个泊位嗅探榄桩
     * @param shipRequireLength
     * @param totalLength
     * @param candidateBollardList
     * @param shipDTO
     * @return
     */
    private boolean trySniffForward(BigDecimal shipRequireLength, BigDecimal totalLength, List<PlanningBollardDTO> candidateBollardList, PlanningShipDTO shipDTO) {
       if (!CollectionUtils.isEmpty(candidateBollardList) && this.hasPreBerth()){
           PlanningBollardDTO firstCandidate = candidateBollardList.get(0);
           // 如果是第一个榄柱才能允许向前一个泊位嗅探缆柱
           if (isFirstBollard(firstCandidate.getBollardInfo().getId())){
               // 需要克隆缆柱数据 否则排产失败时 揽柱的原始被占用时间数据会丢失
               List<PlanningBollardDTO> cloneBollardList = this.preBerth.getBollardDTOList().stream().map(t -> {
                   try {
                       return (PlanningBollardDTO)t.clone();
                   } catch (CloneNotSupportedException e) {
                       e.printStackTrace();
                       throw new ObjectCloneException("排产缆柱数据失败！",e);
                   }
               }).collect(Collectors.toList());

               // 如果加起来大于需求长度则 将可以将缆柱系到前一个泊位的最后一个缆柱上
               totalLength = totalLength.add(firstCandidate.getBollardInfo().getLastBollardDistance());
               if (totalLength.compareTo(shipRequireLength)>0){
                   PlanningBollardDTO preBerthLastBollard = cloneBollardList.get(cloneBollardList.size() - 1);
                   preBerthLastBollard.getBollardInfo().setOccupyUntil(shipDTO.getLeaveTime());
                   candidateBollardList.add(0,preBerthLastBollard);
                   return true;
               }
               // 开始加上前一个泊位的缆柱 从后往前
               for (int i = cloneBollardList.size()-1; i >-1 ; i--) {
                   PlanningBollardDTO currentBollard = cloneBollardList.get(i);
                   totalLength = totalLength.add(currentBollard.getBollardInfo().getLastBollardDistance());
                   // 不管当前的缆柱被占用还是未被占用 都比较一下是否满足满足就返回true 如果这跟缆柱是被占用的说明是向前交叉了一根
                   if (totalLength.compareTo(shipRequireLength)>0){
                       candidateBollardList.add(0,currentBollard);
                       PlanningBollardDTO preBollard = cloneBollardList.get(i - 1);
                       candidateBollardList.add(0,preBollard);
                       currentBollard.getBollardInfo().setOccupyUntil(shipDTO.getLeaveTime());
                       preBollard.getBollardInfo().setOccupyUntil(shipDTO.getLeaveTime());
                       return true;
                   }
                   // 这个榄桩未被占用
                   if (!currentBollard.isFree(shipDTO.getReadyTime())){
                       // 向前嗅探到了被占用的榄桩了 说明排不上了
                       return false;
                   }

               }


           }
       }
        return false;

    }


    /**
     * 是否有前泊位
     * @return
     */
    public boolean hasPreBerth(){
        return this.preBerth != null;
    }

    /**
     * 是否有后泊位
     * @return
     */
    public boolean hasNextBerth(){
        return this.nextBerth != null;
    }

    /**
     * 是第一个缆
     * @return
     */
    public boolean isFirstBollard(String bollardId){
        return this.bollardDTOList.get(0).getBollardInfo().getId().equals(bollardId);
    }

    /**
     * 是最后一个缆
     * @return
     */
    public boolean isLastBollard(String bollardId){
        return this.bollardDTOList.get(this.bollardDTOList.size()-1).getBollardInfo().getId().equals(bollardId);
    }




}
