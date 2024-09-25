package com.aps.yinghai.dto;

import com.aps.yinghai.entity.BollardInfo;
import jakarta.validation.constraints.NotNull;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


/**
 * 泊位资源池
 */
public class PlanningBerthPoolDTO {

    private List<PlanningBerthDTO> berthDTOList;

    public PlanningBerthPoolDTO(List<PlanningBerthDTO> berthDTOList) {
        this.berthDTOList = berthDTOList;
        // 设置前后泊位
        for (int i = 1; i < berthDTOList.size(); i++) {
            PlanningBerthDTO currentBerth = berthDTOList.get(i);
            if (i>1){
                PlanningBerthDTO pre = berthDTOList.get(i - 1);
                currentBerth.setPreBerth(pre);
            }
            if (i< berthDTOList.size()-1){
                PlanningBerthDTO next = berthDTOList.get(i + 1);
                currentBerth.setNextBerth(next);
            }
        }
    }

    /**
     * 获取D1泊位 并且泊位可用
     * @param planningTime
     * @return
     */
    public PlanningBerthDTO getD1Berth(LocalDateTime planningTime){
        // 筛选D1泊位
        Optional<PlanningBerthDTO> berthDTOOptional = this.berthDTOList.stream().filter(t -> t.isD1()).findFirst();

        PlanningBerthDTO result = berthDTOOptional.get();
        if (result != null){
            // 验证泊位的榄桩是否被占用了 因为D1只能有一条船
            List<PlanningBollardDTO> bollardInfoList = result.getBollardDTOList();
            boolean free = bollardInfoList.stream().allMatch(t -> t.isFree(planningTime));
            if (free){
                return result;
            }
            // 如果被占用 则返回空
            return null;
        }
        return result;
    }

    public List<PlanningBerthDTO> getBerthByBerthNo(List<String> berthNoList){
        List<PlanningBerthDTO> result = this.berthDTOList.stream().filter(t -> berthNoList.stream().anyMatch(berthNo->StringUtils.equals(berthNo,t.getBerthInfo().getBerthNo()))).collect(Collectors.toList());
        return result;
    }

    public PlanningBerthDTO getBerthByBerthNo(@NotNull  String berthNo){
        Optional<PlanningBerthDTO> berthDTO = this.berthDTOList.stream().filter(t -> berthNo.equals(t.getBerthInfo().getBerthNo())).findFirst();
        return berthDTO.get();
    }




}
