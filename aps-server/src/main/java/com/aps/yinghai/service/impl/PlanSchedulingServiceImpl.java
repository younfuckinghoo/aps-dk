package com.aps.yinghai.service.impl;

import com.aps.yinghai.dto.PlanningBerthDTO;
import com.aps.yinghai.dto.PlanningBerthPoolDTO;
import com.aps.yinghai.dto.PlanningBollardDTO;
import com.aps.yinghai.dto.PlanningShipDTO;
import com.aps.yinghai.dto.igtos.BizDayNightClassDTO;
import com.aps.yinghai.dto.igtos.BizShipWorkPlanDTO;
import com.aps.yinghai.entity.*;
import com.aps.yinghai.enums.LoadUnloadEnum;
import com.aps.yinghai.exception.ObjectCloneException;
import com.aps.yinghai.exception.ProgramCalculationException;
import com.aps.yinghai.iGTOS.BizDaynightClassPlan;
import com.aps.yinghai.iGTOS.BizShipPrePlan;
import com.aps.yinghai.iGTOS.BizShipWorkPlan;
import com.aps.yinghai.iGTOS.BizShipWorkSequence;
import com.aps.yinghai.plan.DayNightPlanner;
import com.aps.yinghai.service.*;
import com.aps.yinghai.service.igtos.IBizShipDayNightClassService;
import com.aps.yinghai.service.igtos.IBizShipPrePlanService;
import com.aps.yinghai.service.igtos.IBizShipWorkPlanService;
import com.aps.yinghai.service.igtos.IBizShipWorkSequenceService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class PlanSchedulingServiceImpl implements IPlanSchedulingService {

    private static final Logger logger = LoggerFactory.getLogger(PlanSchedulingServiceImpl.class);

    private final IShipForecastService iShipForecastService;
    private final ICabinInfoService iCabinInfoService;
    private final IBerthInfoService iBerthInfoService;
    private final IBollardInfoService iBollardInfoService;
    private final IBizShipPrePlanService iBizShipPrePlanService;
    private final IBizShipWorkSequenceService iBizShipWorkSequenceService;
    private final IShipWorkingSequenceService iShipWorkingSequenceService;
    private final IShipWorkingInfoDetailService iShipWorkingInfoDetailService;
    private final IBizShipWorkPlanService iBizShipWorkPlanService;
    private final IBizShipDayNightClassService iBizShipDayNightClassService;

    public PlanSchedulingServiceImpl(IShipForecastService iShipForecastService, ICabinInfoService iCabinInfoService, IBerthInfoService iBerthInfoService, IBollardInfoService iBollardInfoService, IBizShipPrePlanService iBizShipPrePlanService, IBizShipWorkSequenceService iBizShipWorkSequenceService, IShipWorkingSequenceService iShipWorkingSequenceService, IShipWorkingInfoDetailService iShipWorkingInfoDetailService, IBizShipWorkPlanService iBizShipWorkPlanService, IBizShipDayNightClassService iBizShipDayNightClassService) {
        this.iShipForecastService = iShipForecastService;
        this.iCabinInfoService = iCabinInfoService;
        this.iBerthInfoService = iBerthInfoService;
        this.iBollardInfoService = iBollardInfoService;
        this.iBizShipPrePlanService = iBizShipPrePlanService;
        this.iBizShipWorkSequenceService = iBizShipWorkSequenceService;
        this.iShipWorkingSequenceService = iShipWorkingSequenceService;
        this.iShipWorkingInfoDetailService = iShipWorkingInfoDetailService;
        this.iBizShipWorkPlanService = iBizShipWorkPlanService;
        this.iBizShipDayNightClassService = iBizShipDayNightClassService;
    }


    @Transactional
    @Override
    public List longTermScheduling(Integer absentProcedure) {
        LocalDateTime now = LocalDateTime.now();
        // 默认开始时间为今天18点
        LocalDateTime startTime = now.withHour(18).withMinute(0).withSecond(0);
        // 如果当前时间过了18点 那就从下一个18点开始排
        if (now.isAfter(startTime)) {
            startTime = startTime.plus(1, ChronoUnit.DAYS);
        }

        // 货物手续不齐全的可以进行预排
        List<ShipForecast> shipForecastList = iShipForecastService.listNotPlanningShip(absentProcedure);

        // 获取每个船的舱
//        Map<String, List<CabinInfo>> cabinListMap = this.listCabinByShip(shipForecastList);
        // 配载卸序
        Map<String, List<ShipWorkingSequence>> workSequenceListMap = this.listWorkSequenceMap(shipForecastList);


        // 获取泊位列表
        List<BerthInfo> berthInfoList = iBerthInfoService.listOriginalBerth();

        // 获取每个泊位的缆柱
        Map<String, List<BollardInfo>> bollardListMap = this.listBollardByShip(berthInfoList);

        List<PlanningShipDTO> shipDTOList = this.planningLongTerm(startTime, shipForecastList, workSequenceListMap, berthInfoList, bollardListMap);
        shipDTOList.forEach(t -> {
            if (t.isPlaned() && t.getOccupiedBerth() != null) {
                t.getOccupiedBerth().setPreBerth(null);
                t.getOccupiedBerth().setNextBerth(null);
            }
        });
        return shipDTOList;
    }

    private Map<String, List<ShipWorkingSequence>> listWorkSequenceMap(List<ShipForecast> shipForecastList) {
        List<String> prePlanIdList = shipForecastList.stream().map(t -> t.getId()).collect(Collectors.toList());
        LambdaQueryWrapper<ShipWorkingSequence> chainWrapper = Wrappers.lambdaQuery(ShipWorkingSequence.class);
        chainWrapper.in(ShipWorkingSequence::getShipForecastId, prePlanIdList);
        List<ShipWorkingSequence> workingSequences = iShipWorkingSequenceService.list(chainWrapper);
        Map<String, List<ShipWorkingSequence>> map = workingSequences.stream().collect(Collectors.groupingBy(t -> t.getShipForecastId(), Collectors.toList()));
        return map;


    }

    private List<PlanningShipDTO> planningLongTerm(LocalDateTime startTime,
                                                   List<ShipForecast> shipForecastList,
                                                   Map<String, List<ShipWorkingSequence>> workSequenceListMap, List<BerthInfo> berthInfoList,
                                                   Map<String, List<BollardInfo>> bollardListMap) {
        // 包装初始化船舶
        List<PlanningShipDTO> shipDTOList = shipForecastList.stream()
                .map(t -> PlanningShipDTO.packageShip(t, startTime, workSequenceListMap.get(t.getId())))
                .collect(Collectors.toList());


        // 包装初始化泊位
        List<PlanningBerthDTO> berthDTOList = berthInfoList.stream().map(t -> PlanningBerthDTO.packageBerthDTO(t, bollardListMap.get(t.getId()))).collect(Collectors.toList());

        PlanningBerthPoolDTO planningBerthPoolDTO = new PlanningBerthPoolDTO(berthDTOList);
        AtomicInteger atomicInteger = new AtomicInteger(shipDTOList.size());

        int index = 0;
        // 这个循环是一个小时尝试排一次
        do {
            LocalDateTime planningTime = startTime.plusHours(index);
            logger.info("正在排产时间：{}", planningTime);
            // 找到在此刻之前已经到港的未排的船
            List<PlanningShipDTO> availableShipList = shipDTOList.stream().filter(t -> t.getShipForecast().getExpectArriveTime().isBefore(planningTime) && !t.isPlaned()).collect(Collectors.toList());

            // 筛选出大于300米的
            List<PlanningShipDTO> shipMoreThan300 = availableShipList.stream().filter(t -> t.lengthMoreThan300()).sorted((s1, s2) -> s2.getTotalQty().compareTo(s1.getTotalQty())).collect(Collectors.toList());
            // 小于300米的
            List<PlanningShipDTO> shipLessThan300 = availableShipList.stream().filter(t -> !t.lengthMoreThan300()).sorted((s1, s2) -> {
                // 现根据卸-装排序 装卸类型一样则根据卸（正序）、装（倒序）排序
                int compare = s2.getShipForecast().getLoadUnload() - s1.getShipForecast().getLoadUnload();
                if (compare == 0) {
                    if (LoadUnloadEnum.LOAD.getCode() == s1.getShipForecast().getLoadUnload()) {
                        return s1.getShipForecast().getExpectArriveTime().compareTo(s2.getShipForecast().getExpectArriveTime());
                    } else {
                        return s2.getShipForecast().getExpectArriveTime().compareTo(s1.getShipForecast().getExpectArriveTime());
                    }
                }
                //s1.getShipForecast().getExpectArriveTime().compareTo(s1.getShipForecast().getExpectArriveTime());
//                if (compare == 0) {
//                    return s2.getTotalQty().compareTo(s1.getTotalQty());
//                }
//                return compare;
//                LoadUnloadEnum.UNLOAD

                return compare;

            }).collect(Collectors.toList());
            availableShipList.clear();
            availableShipList.addAll(shipMoreThan300);
            availableShipList.addAll(shipLessThan300);

            index = 0;
            // 差值
            int cha = availableShipList.size() - 1;
            availableLoop:
            do {
                PlanningShipDTO planningShipDTO = availableShipList.get(index);
                planningShipDTO.setReadyTime(planningTime);

                // 大于300米 吃水超过18.5 必须选择D1
                if (planningShipDTO.lengthMoreThan300() || planningShipDTO.draftMoreThan18_5()) {
                    PlanningBerthDTO d1Berth = planningBerthPoolDTO.getD1Berth(planningShipDTO.getReadyTime());
                    // 如果能排上
                    if (d1Berth != null) {
                        List<PlanningBollardDTO> bollardDTOList = d1Berth.getBollardDTOList();
                        boolean updateBerth = this.planningBollard(bollardDTOList, planningShipDTO, 1, true);
                        // 排上了
                        if (updateBerth) {

                            // 设置泊位
                            planningShipDTO.setOccupiedBerth(d1Berth);
                            // 根据舱时量计算工作时间 计算靠泊时间、开工时间、完工时间、离泊时间
                            planningShipDTO.planning();
                            // 回写榄桩占用时间 只有上一步设置了离泊时间才能决定这些缆柱占用到什么时候
                            this.rewriteBollardOccupyTime(d1Berth.getBollardDTOList(), planningShipDTO.getOccupiedBollardList());
                            planningShipDTO.setPlaned(true);
                            // 每条靠泊的船靠泊时间至少间隔1小时
                            index++;
                            atomicInteger.getAndDecrement();
                        } else {
                            // 未排上 清除数据
                            planningShipDTO.clearPlanData();
                        }

                    }

                }else{
                    List<String> berthNoList = new ArrayList<>();
                    List<PlanningBerthDTO> tradeBerthDTOList;
                    List<PlanningBollardDTO> candidateBollardDTOList;
                    // 根据装卸类型、内外贸类型决定如何选泊位
                    Boolean sortAsc = findPlanningBerthNo(planningShipDTO, berthNoList);
                    // 2.根据备选泊位集合安排缆柱，设置planningShipDTO的占用缆柱集合
                    candidateBollardDTOList = listCandidateBollardByBerthNo(planningBerthPoolDTO,sortAsc,berthNoList);
                    //  3.开始进行排缆柱
                    boolean updateBerth = this.planningBollard(candidateBollardDTOList, planningShipDTO, 0, sortAsc);
                    // 排上了
                    if (updateBerth) {

                        // 设置泊位
                        // 5.根据缆柱确定泊位
                        PlanningBerthDTO d1Berth = determinantBerth(planningBerthPoolDTO,planningShipDTO.getOccupiedBollardList());
                        planningShipDTO.setOccupiedBerth(d1Berth);
                        // planningShipDTO根据泊位设置船和卸序的机器占用情况（线数、机械类型编码）、根据舱时量计算工作时间 计算靠泊时间、开工时间、完工时间、离泊时间
                        planningShipDTO.planning();
                        // 回写榄桩占用时间 只有上一步设置了离泊时间才能决定这些缆柱占用到什么时候
                        this.rewriteBollardOccupyTime(candidateBollardDTOList, planningShipDTO.getOccupiedBollardList());
                        planningShipDTO.setPlaned(true);
                        atomicInteger.getAndDecrement();

                    } else {
                        // 未排上 清除数据
                        planningShipDTO.clearPlanData();
                    }
                }



                int nextIdx = index + cha;
                PlanningShipDTO nextDTO = availableShipList.get(nextIdx);
                if (nextDTO.getShipForecast().getId().equals(planningShipDTO.getShipForecast().getId()))break ;
                // 只有一种装卸类型
                if (nextDTO.getShipForecast().getLoadUnload() == planningShipDTO.getShipForecast().getLoadUnload()) {
                    if (cha > 0) {
                        cha--;
                        index++;
                    } else {
                        cha++;
                        index--;
                    }
                } else {
                    // 现根据差计算新的下标
                    index = index + cha;
                    // 更新计算新的差
                    if (cha > 0) {
                        cha--;
                        cha = 0 - cha;
                    } else {
                        cha++;
                        cha = 0 - cha;
                    }

                }


            } while (cha != 0);



            // 每次进入都要+1小时
            index++;
            if (index > 10000) {
                throw new ProgramCalculationException("排产时间太长，排产错误");
//                logger.error("以下船舶未能合理安排计划：{}",shipDTOList.stream().filter(t->!t.isPlaned()).collect(Collectors.toList()));
//                break;
            }
        } while (atomicInteger.get() > 0);

        this.updateBizPrePlan(shipDTOList.stream().filter(t -> t.isPlaned()).collect(Collectors.toList()));


        return shipDTOList;


    }

    /**
     * 根据榄桩数量在哪个泊位上多决定归属于哪个泊位
     * @param planningBerthPoolDTO
     * @param occupiedBollardList
     * @return
     */
    private PlanningBerthDTO determinantBerth(PlanningBerthPoolDTO planningBerthPoolDTO, List<PlanningBollardDTO> occupiedBollardList) {
        Map<String, Long> berthNoMap = occupiedBollardList.stream().map(t -> t.getBollardInfo()).collect(Collectors.groupingBy(t -> t.getBerthNo(), Collectors.counting()));
        Set<Map.Entry<String, Long>> entries = berthNoMap.entrySet();
        String berthNo = "";int count = 0;
        for (Map.Entry<String, Long> entry : entries) {
            if (entry.getValue()>count) {
                count = entry.getValue().intValue();
                berthNo = entry.getKey();
            }
        }
        return planningBerthPoolDTO.getBerthByBerthNo(berthNo);

    }

    private List<PlanningBollardDTO> listCandidateBollardByBerthNo(PlanningBerthPoolDTO planningBerthPoolDTO, Boolean sortAsc, List<String> berthNoList) {
        List<PlanningBollardDTO> result = new ArrayList<>();
        for (String berthNo : berthNoList) {
            PlanningBerthDTO planningBerthDTO = planningBerthPoolDTO.getBerthByBerthNo(berthNo);
            List<PlanningBollardDTO> bollardDTOList = planningBerthDTO.getBollardDTOList();
            List<PlanningBollardDTO> collect = bollardDTOList.stream().sorted((b1, b2) -> {
                if (sortAsc)
                    return b1.getBollardInfo().getBollardNo() - b2.getBollardInfo().getBollardNo();
                else
                    return b2.getBollardInfo().getBollardNo() - b1.getBollardInfo().getBollardNo();
            }).collect(Collectors.toList());
            result.addAll(collect);
        }
        return result;
    }

    private boolean findPlanningBerthNo(PlanningShipDTO planningShipDTO, List<String> nos) {
        boolean sortAsc;
        //  要优先安排卸船到D2\D3 因为只有安排的D2\D3的卸船才能判断有没有未被安排的卸船
        // 外贸卸船优先 D2 D3
        if (planningShipDTO.isUnLoad()) {
            sortAsc = true;
            // 卸船外贸
            if (planningShipDTO.isOutTrade()) {
                nos.add("D2");
                nos.add("D3");
                nos.add("D1");
            } else {
                nos.add("D2");
                nos.add("D3");
                if (planningShipDTO.lengthMoreThan289() || planningShipDTO.draftMoreThan18_3()) {
                    nos.add(0, "D1");
                } else {
                    nos.add("D1");
                }
            }
            nos.add("D4");
            nos.add("D5");

        } else {
            sortAsc = false;
            nos.add("D5");
            nos.add("D4");
            nos.add("D3");
            nos.add("D2");
            nos.add("D1");
        }
        return sortAsc;
    }

    /**
     * 回写榄桩的被占用时间
     *
     * @param availableBollardList
     * @param planedBollardList
     */
    private void rewriteBollardOccupyTime(List<PlanningBollardDTO> availableBollardList, List<PlanningBollardDTO> planedBollardList) {
        // 将被占用的缆柱更新到原始数据上
        Map<String, PlanningBollardDTO> bollardDTOMap = availableBollardList.stream().collect(Collectors.toMap(t -> t.getBollardInfo().getId(), t -> t));

        planedBollardList.forEach(changed -> {
            String id = changed.getBollardInfo().getId();
            PlanningBollardDTO original = bollardDTOMap.get(id);

            if (original != null)
                original.getBollardInfo().setOccupyUntil(changed.getBollardInfo().getOccupyUntil());
        });
    }


    private boolean planningBollard(List<PlanningBollardDTO> bollardDTOList, PlanningShipDTO planningShipDTO, int skipNum, boolean asc) {

        List<PlanningBollardDTO> cloneBollardList = bollardDTOList.stream()
//                .sorted((t1, t2) -> {if (asc) return t1.getBollardInfo().getBollardNo() - t2.getBollardInfo().getBollardNo();else return t2.getBollardInfo().getBollardNo() - t1.getBollardInfo().getBollardNo();        })
                .skip(skipNum)
                .map(t -> {
                    try {
                        return (PlanningBollardDTO) t.cloneObj();
                    } catch (CloneNotSupportedException e) {
                        e.printStackTrace();
                        throw new ObjectCloneException("排产缆柱数据失败！", e);
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

        seek:
        for (int i = 0; i < cloneBollardList.size(); ) {
            PlanningBollardDTO planningBollardDTO = cloneBollardList.get(i);
            // 找到第一个空闲的缆柱
            boolean free = planningBollardDTO.isFree(planningShipDTO.getReadyTime());

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
                    // 累加缆柱与前一个缆柱的长度
                    totalLength = totalLength.add(freeBollard.getBollardInfo().getLastBollardDistance());
                    if (totalLength.compareTo(shipRequireLength) > 0) {
                        // 如果不是从第一个榄桩开始排的，那么可以将船的艏缆系到前一个被占用的缆上
                        if (asc) {
                            if (i > 0) {
                                PlanningBollardDTO firstBollard = cloneBollardList.get(i - 1);
                                candidateBollardList.add(0, firstBollard);
                            }
                        } else {
                            PlanningBollardDTO lastBollard = cloneBollardList.get(j + 1);
                            candidateBollardList.add(lastBollard);
                        }


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

                        if (asc && i > 1) {
                            // 向前交叉一根缆柱
                            totalLength = totalLength.add(cloneBollardList.get(i - 1).getBollardInfo().getLastBollardDistance());
                            // 向前交叉一根满足的话
                            if (totalLength.compareTo(shipRequireLength) > 0) {
                                // 前一根和交叉的那根都要被占用
                                PlanningBollardDTO firstBollard = cloneBollardList.get(i - 2);
                                PlanningBollardDTO secondBollard = cloneBollardList.get(i - 1);
                                candidateBollardList.add(0, secondBollard);
                                candidateBollardList.add(0, firstBollard);

                                planned = true;
                                break seek;
                            }
                        } else if (!asc && i > 1) {
                            // 如果是倒序向后交叉一根缆柱
                            totalLength = totalLength.add(cloneBollardList.get(i - 1).getBollardInfo().getLastBollardDistance());
                            if (totalLength.compareTo(shipRequireLength) > 0) {
                                // 前一根要被占用
                                PlanningBollardDTO lastBollard = cloneBollardList.get(i - 1);
                                candidateBollardList.add(0, lastBollard);
                                planned = true;
                                break seek;
                            }
                        }
                        break plan;

                    }

                }

                if (planned) break seek;

                // 前面安排的缆柱长度加起来不够 直接跳过
                i += occupiedAmount;

            }

            i++;

        }


        if (planned) {
            planningShipDTO.setOccupiedBollardList(candidateBollardList);
            return true;
        } else {
            return false;
        }


    }

    /**
     * 回写iGTOS排产数据
     *
     * @param planningShipDTOS
     */
    private void updateBizPrePlan(List<PlanningShipDTO> planningShipDTOS) {
        LocalDateTime now = LocalDateTime.now();
        List<BizShipPrePlan> bizShipPrePlans = planningShipDTOS.stream().map(t -> {
            List<PlanningBollardDTO> occupiedBollardList = t.getOccupiedBollardList();

            BizShipPrePlan bizShipPrePlan = new BizShipPrePlan();
            bizShipPrePlan.setId(t.getShipForecast().getId());
            bizShipPrePlan.setExpectBerthTime(t.getReadyTime());
            bizShipPrePlan.setStartTime(t.getStartTime());
            bizShipPrePlan.setCapacityPerHour(t.getShipForecast().getCapacityPerHour());
            bizShipPrePlan.setEndTime(t.getEndTime());
            bizShipPrePlan.setExpectLeaveTime(t.getLeaveTime());
            bizShipPrePlan.setExpectUnberthTime(t.getLeaveTime());
//            bizShipPrePlan.setShipStatusName(ShipStatusEnum.PRE_PLAN.getName());
            bizShipPrePlan.setBerthNo(t.getOccupiedBerth().getBerthInfo().getBerthNo());
            bizShipPrePlan.setBollardForward(occupiedBollardList.get(0).getBollardInfo().getBollardNo().toString());
            bizShipPrePlan.setBollardBehind(occupiedBollardList.get(occupiedBollardList.size() - 1).getBollardInfo().getBollardNo().toString());
            bizShipPrePlan.setReviseDate(now);
            bizShipPrePlan.setLoadQty(t.getTotalQty());

            t.getShipForecast().setLoadQty(t.getTotalQty());
            t.getShipForecast().setExpectBerthTime(t.getReadyTime());
            t.getShipForecast().setStartTime(t.getStartTime());
            t.getShipForecast().setCapacityPerHour(t.getShipForecast().getCapacityPerHour());
            t.getShipForecast().setEndTime(t.getEndTime());
            t.getShipForecast().setExpectLeaveTime(t.getLeaveTime());
            t.getShipForecast().setBerthNo(t.getOccupiedBerth().getBerthInfo().getBerthNo());
            t.getShipForecast().setBollardForward(occupiedBollardList.get(0).getBollardInfo().getBollardNo().toString());
            t.getShipForecast().setBollardBehind(occupiedBollardList.get(occupiedBollardList.size() - 1).getBollardInfo().getBollardNo().toString());
            t.getShipForecast().setReviseDate(now);

            return bizShipPrePlan;
        }).collect(Collectors.toList());

        List<ShipForecast> shipForecastList = planningShipDTOS.stream().map(t -> t.getShipForecast()).collect(Collectors.toList());
        iShipForecastService.updateBatchById(shipForecastList);

        List<ShipWorkingSequence> workingSequenceList = planningShipDTOS.stream().map(t -> t.getShipWorkingSequences()).flatMap(t -> t.stream()).collect(Collectors.toList());
        iShipWorkingSequenceService.updateBatchById(workingSequenceList);


        List<BizShipWorkSequence> bizShipWorkSequences = workingSequenceList.stream().map(t -> {
            BizShipWorkSequence bizShipWorkSequence = new BizShipWorkSequence();
            bizShipWorkSequence.setId(t.getId());
            bizShipWorkSequence.setWorkHours(t.getWorkHours());
            bizShipWorkSequence.setMachineTypeCode(t.getMachineTypeCode());
            bizShipWorkSequence.setMachineCount(t.getMachineCount());
            return bizShipWorkSequence;
        }).collect(Collectors.toList());
        iBizShipWorkSequenceService.updateBatchById(bizShipWorkSequences);
        iBizShipPrePlanService.updateBatchById(bizShipPrePlans);


    }

    private Map<String, List<BollardInfo>> listBollardByShip(List<BerthInfo> berthInfoList) {
        List<String> berthIdList = berthInfoList.stream().map(t -> t.getId()).collect(Collectors.toList());
        List<BollardInfo> bollardInfoList = iBollardInfoService.listBollardByBerthIdList(berthIdList);
        Map<String, List<BollardInfo>> listMap = bollardInfoList.stream().collect(Collectors.groupingBy(t -> t.getBerthId(), Collectors.toList()));
        return listMap;
    }

    private Map<String, List<CabinInfo>> listCabinByShip(List<ShipForecast> shipForecastList) {
        List<String> shipIdList = shipForecastList.stream().map(t -> t.getId()).collect(Collectors.toList());
        List<CabinInfo> cabinInfoList = iCabinInfoService.listCabinByShipIdList(shipIdList);
        Map<String, List<CabinInfo>> listMap = cabinInfoList.stream().collect(Collectors.groupingBy(t -> t.getShipId(), Collectors.toList()));
        return listMap;
    }

    @Override
    public List dayNightScheduling() {
        LocalDateTime now = LocalDateTime.now();
        // 默认开始时间为今天18点
        LocalDateTime startTime = now.withHour(18).withMinute(0).withSecond(0);
        // 如果当前时间过了18点 那就从下一个18点开始排
        if (now.isAfter(startTime)) {
            startTime = startTime.plus(1, ChronoUnit.DAYS);
        }
        // 第一个班结束时间
        LocalDateTime firstEndTime = startTime.plusHours(12);
        // 第二个班结束时间
        LocalDateTime secondEndTime = firstEndTime.plusHours(12);
        // 获取靠泊时间小于下一个夜昼结束6点的所有预计划
        List<ShipForecast> shipForecastList = iShipForecastService.listShipForecastByTimeRange(startTime, secondEndTime);
        List<String> shipForecastIdList = shipForecastList.stream().map(t -> t.getId()).collect(Collectors.toList());
        // 获取与计划的卸序子表
        List<ShipWorkingSequence> shipWorkingSequences = this.iShipWorkingSequenceService.listByShipForecastIdList(shipForecastIdList);
        Map<String, List<ShipWorkingSequence>> sequenceListMap = shipWorkingSequences.stream().collect(Collectors.groupingBy(t -> t.getShipForecastId(), Collectors.toList()));
        // 获取作业详情子表
        List<ShipWorkingInfoDetail> workingInfoDetailList = this.iShipWorkingInfoDetailService.listByShipForecastIdList(shipForecastIdList);
        Map<String, List<ShipWorkingInfoDetail>> workingInfoListMap = workingInfoDetailList.stream().collect(Collectors.groupingBy(t -> t.getShipForecastId(), Collectors.toList()));
        // 创建昼夜计划包装列表
        List<PlanningShipDTO> dayNightShipDTOList = shipForecastList.stream().map(t -> PlanningShipDTO.packageDayNightShip(t, sequenceListMap.get(t.getId()), workingInfoListMap.get(t.getId()))).sorted((a, b) -> a.getShipForecast().getStartTime().compareTo(b.getShipForecast().getStartTime())).collect(Collectors.toList());


        // 获取所有可用的系缆柱
        List<BerthInfo> berthInfoList = iBerthInfoService.listOriginalBerth();
        List<String> berthNoList = berthInfoList.stream().map(t -> t.getBerthNo()).collect(Collectors.toList());
        List<BollardInfo> bollardInfoList = iBollardInfoService.list(Wrappers.lambdaQuery(BollardInfo.class).in(BollardInfo::getBerthNo, berthNoList));
        Map<String, List<BollardInfo>> bollardListMap = bollardInfoList.stream().collect(Collectors.groupingBy(t -> t.getBerthNo(), Collectors.toList()));
        List<PlanningBerthDTO> planningBerthDTOList = berthInfoList.stream().map(t -> PlanningBerthDTO.packageBerthDTO(t, bollardListMap.get(t.getBerthNo()))).collect(Collectors.toList());
        PlanningBerthPoolDTO planningBerthPoolDTO = new PlanningBerthPoolDTO(planningBerthDTOList);


        DayNightPlanner dayNightPlanner = new DayNightPlanner(startTime, firstEndTime, secondEndTime, planningBerthPoolDTO, bollardInfoList);
        for (PlanningShipDTO planningShipDTO : dayNightShipDTOList) {
            // 一直到这条船排完
            do {
                dayNightPlanner.planningClass(planningShipDTO);
            } while (!planningShipDTO.dayNightPlanDone(secondEndTime));


        }
        System.out.println(dayNightShipDTOList);
        Collection<BizShipWorkPlanDTO> BizShipWorkPlanDTOCollection = dayNightPlanner.getWorkPlanDTOMap().values();
        List<BizShipWorkPlan> workPlanList = new ArrayList<>();
        List<BizDaynightClassPlan> workPlanClassList = new ArrayList<>();
        for (BizShipWorkPlanDTO bizShipWorkPlanDTO : BizShipWorkPlanDTOCollection) {
            BizShipWorkPlan plan = bizShipWorkPlanDTO.getPlan();
            BizDayNightClassDTO dayClass = bizShipWorkPlanDTO.getDayClass();
            BizDayNightClassDTO nightClass = bizShipWorkPlanDTO.getNightClass();
            if (dayClass != null)
                workPlanClassList.add(dayClass.getClassPlan());
            if (nightClass != null)
                workPlanClassList.add(nightClass.getClassPlan());
            BigDecimal planWeight = BigDecimal.ZERO;
            if (dayClass != null) {
                planWeight = planWeight.add(dayClass.getClassPlan().getPlanWeight());
            }
            if (nightClass != null) {
                planWeight = planWeight.add(nightClass.getClassPlan().getPlanWeight());
            }
            plan.setPlanWeight(planWeight);
            plan.setLeftWeight(plan.getTotalWeight().subtract(planWeight));
            workPlanList.add(plan);

        }
        iBizShipWorkPlanService.saveBatch(workPlanList);
        iBizShipDayNightClassService.saveBatch(workPlanClassList);


//        // 累加每一票的作业时长 超过一个班次最后时间就新增第二个班次
//        int idx = 0,doneCount = 0;
//        do{
//            int realIdx = idx%dayNightShipDTOList.size();
//            PlanningShipDTO planningShipDTO = dayNightShipDTOList.get(realIdx);
//            // 安排
//            boolean b = dayNightPlanner.planningClass(planningShipDTO);
//
//
//            // 如果这一条船排完了则完成数+1
//            if (planningShipDTO.dayNightPlanDone(secondEndTime)){
//                doneCount++;
//            }
//            if (doneCount == dayNightShipDTOList.size())break;
//        }while (true);


        // 直到达到第二个班次的最后时间点


        return null;
    }


}
