package com.aps.yinghai.dto;

import com.aps.yinghai.entity.BerthInfo;
import com.aps.yinghai.entity.BollardInfo;
import com.aps.yinghai.entity.CabinInfo;
import com.aps.yinghai.entity.ShipForecast;
import com.aps.yinghai.enums.TradeTypeEnum;
import lombok.Data;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Data
public class PlanningShipDTO {
    private ShipForecast shipForecast;
    /**
     * 所有舱口之和 总吨数
     */
    private BigDecimal totalQty;
    /**
     * 计划吨数
     */
    private BigDecimal planningQty;
    /**
     * 舱口列表
     */
    private List<CabinInfo> cabinInfoList;
    /**
     * 是否已排
     */
    private boolean isPlaned = false;
    /**
     * 开工时间
     */
    private LocalDateTime startTime;
    /**
     * 完工时间
     */
    private LocalDateTime endTime;

    /**
     * 靠泊时间
     */
    private LocalDateTime anchorTime;
    /**
     * 离泊时间
     */
    private LocalDateTime leaveTime;

    /**
     * 泊位信息 停靠的泊位
     */
    private PlanningBerthDTO occupiedBerth;
    /**
     * 占用的缆柱集合
     */
    private List<PlanningBollardDTO> occupiedBollardList;

    // 就绪时间 可以安排靠泊
    private LocalDateTime readyTime;

    public static PlanningShipDTO packageShip(ShipForecast shipForecast, List<CabinInfo> cabinInfoList, LocalDateTime startTime){
        PlanningShipDTO planningShipDTO = new PlanningShipDTO();
        planningShipDTO.setShipForecast(shipForecast);
        planningShipDTO.setCabinInfoList(cabinInfoList);
        Optional<BigDecimal> reduce = cabinInfoList.stream().map(t -> t.getLoadQty()).reduce((a, b) -> a.add(b));
        reduce.ifPresentOrElse(b->planningShipDTO.setTotalQty(b),()->planningShipDTO.setTotalQty(BigDecimal.ZERO));

        // 准备好的时间 如果有提前好几天来的船那么就绪时间为排产开始时间否则为船舶实际的抵港时间
        if (shipForecast.getExceptArriveTime().isBefore(startTime)){
            planningShipDTO.readyTime = startTime;
        }else{
            planningShipDTO.readyTime = shipForecast.getExceptArriveTime();
        }
        return planningShipDTO;
    }


    public boolean lengthMoreThan289() {
        boolean flag = this.shipForecast.getShipLength().compareTo(BigDecimal.valueOf(289))>=0;
        return flag;
    }

    public boolean lengthMoreThan300() {
        boolean flag = this.shipForecast.getShipLength().compareTo(BigDecimal.valueOf(300))>=0;
        return flag;
    }

    /**
     * 吃水超过18.3
     * @return
     */
    public boolean draftMoreThan18_3() {
        boolean flag = this.shipForecast.getDraft().compareTo(new BigDecimal("18.3"))>0;
        return flag;
    }

    /**
     * 计算靠泊时间、开工时间、完工时间、离泊时间
     * @return
     */
    public boolean planning() {
        // 工作时长 小时
        BigDecimal workingDuration = this.totalQty.divide(this.shipForecast.getCapacityPerHour(),2, RoundingMode.HALF_UP);
        BigDecimal durationMinute = workingDuration.multiply(BigDecimal.valueOf(60)).setScale(0, RoundingMode.CEILING);

        // 设置靠泊时间
        this.anchorTime = this.readyTime;
        // 开工时间
        // D1泊位 +2小时
        if (this.occupiedBerth.isD1()){
            this.startTime = this.anchorTime.plusMinutes(120);
        }else{
            if (isInTrade()){
                // 内贸+1小时
                this.startTime = this.anchorTime.plusMinutes(60);
            }else{
                // 外贸+1小时
                this.startTime = this.anchorTime.plusMinutes(90);
            }
        }
        // 完工时间 = 开工时间 +作业时间
        this.endTime = this.startTime.plusMinutes(durationMinute.longValue());

        // 离港时间规则：
        // D1泊位 +2小时
        if (this.occupiedBerth.isD1()){
            this.leaveTime = this.endTime.plusMinutes(120);
        }else{
            if (isInTrade()){
                // 内贸+1小时
                this.leaveTime = this.endTime.plusMinutes(60);
            }else{
                // 外贸+1小时
                this.leaveTime = this.endTime.plusMinutes(90);
            }
        }
        return true;

    }



    /**
     * 设置占用泊位、缆柱
     * @return
     */
    public boolean setOccupied(){

        return true;
    }


    /**
     * 内贸
     * @return
     */
    public boolean isInTrade(){
        return TradeTypeEnum.IN.getCode() == this.shipForecast.getInOutTrade();
    }

    /**
     * 内贸
     * @return
     */
    public boolean isOutTrade(){
        return TradeTypeEnum.IN.getCode() == this.shipForecast.getInOutTrade();
    }

    /**
     * 排不上就清除排泊数据
     */
    public void clearPlanData() {
        this.startTime = null;
        this.endTime = null;
        this.anchorTime = null;
        this.leaveTime = null;
        this.occupiedBerth = null;
        this.occupiedBollardList = null;

    }
}
