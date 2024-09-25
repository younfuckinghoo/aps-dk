package com.aps.yinghai.dto.igtos;

import com.aps.yinghai.dto.PlanningShipDTO;
import com.aps.yinghai.enums.TradeTypeEnum;
import com.aps.yinghai.iGTOS.BizShipWorkPlan;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class BizShipWorkPlanDTO {

    private BizShipWorkPlan plan;

    private BizDayNightClassDTO dayClass;
    private BizDayNightClassDTO nightClass;

    public static BizShipWorkPlanDTO buildWorkPlan(PlanningShipDTO planningShipDTO, LocalDateTime planDate) {
        BizShipWorkPlanDTO bizShipWorkPlanDTO = new BizShipWorkPlanDTO();
        BizShipWorkPlan bizShipWorkPlan = new BizShipWorkPlan();
        bizShipWorkPlanDTO.plan = bizShipWorkPlan;
        bizShipWorkPlan.setId(UUID.randomUUID().toString());
        bizShipWorkPlan.setCreator("shuiguiyuan");
        bizShipWorkPlan.setCreatorId("shuiguiyuan");
        bizShipWorkPlan.setCreateDate(LocalDateTime.now());
        bizShipWorkPlan.setBerthNo(planningShipDTO.getShipForecast().getBerthNo());
        bizShipWorkPlan.setCabinQty(planningShipDTO.getShipForecast().getShipcabinQty());
        bizShipWorkPlan.setPlanDate(planDate);
        bizShipWorkPlan.setShipcycleId(planningShipDTO.getShipForecast().getIgtosShipcycleId());
        bizShipWorkPlan.setStartBollard(planningShipDTO.getShipForecast().getBollardForward());
        bizShipWorkPlan.setEndBollard(planningShipDTO.getShipForecast().getBollardBehind());
        bizShipWorkPlan.setLoadUnload(planningShipDTO.getShipForecast().getLoadUnload());
        bizShipWorkPlan.setTotalWeight(planningShipDTO.getShipForecast().getLoadQty());
        if (planningShipDTO.getShipForecast().getInOutTrade() == TradeTypeEnum.IN.getCode()) {
            bizShipWorkPlan.setShipagentName(planningShipDTO.getShipForecast().getShipagentInName());
        } else {
            bizShipWorkPlan.setShipagentName(planningShipDTO.getShipForecast().getShipagentOutName());
        }
        bizShipWorkPlan.setShipNameCh(planningShipDTO.getShipForecast().getShipName());
        bizShipWorkPlan.setBerthDate(planningShipDTO.getShipForecast().getExpectBerthTime());
        bizShipWorkPlan.setStartWorkDate(planningShipDTO.getShipForecast().getStartTime());
        bizShipWorkPlan.setPlanFinishDate(planningShipDTO.getShipForecast().getEndTime());
        bizShipWorkPlan.setPlanDepartDate(planningShipDTO.getShipForecast().getExpectLeaveTime());


        return bizShipWorkPlanDTO;

    }


    public BizDayNightClassDTO createDayClass() {
        this.dayClass = BizDayNightClassDTO.buildDayClass(this.plan.getId());
        return this.dayClass;

    }

    public BizDayNightClassDTO createNightClass() {
        this.nightClass = BizDayNightClassDTO.buildNightClass(this.plan.getId());
        return this.nightClass;
    }


}
