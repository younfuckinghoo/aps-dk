package com.aps.yinghai.dto;

import com.aps.yinghai.entity.ShipForecast;
import com.aps.yinghai.entity.ShipWorkingSequence;
import com.aps.yinghai.enums.LoadUnloadEnum;
import com.aps.yinghai.enums.MachineTypeEnum;
import com.aps.yinghai.enums.TradeTypeEnum;
import lombok.Data;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class PlanningShipDTO {
    private ShipForecast shipForecast;
    private List<ShipWorkingSequence> shipWorkingSequences;
    /**
     * 载重吨数
     */
    private BigDecimal totalQty;
    /**
     * 计划吨数
     */
    private BigDecimal planningQty;
    /**
     * 整船作业时间
     */
    private BigDecimal shipWorkingDuration;

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

    public static PlanningShipDTO packageShip(ShipForecast shipForecast, LocalDateTime startTime, List<ShipWorkingSequence> shipWorkingSequences) {
        PlanningShipDTO planningShipDTO = new PlanningShipDTO();
        planningShipDTO.setShipForecast(shipForecast);

        // 准备好的时间 如果有提前好几天来的船那么就绪时间为排产开始时间否则为船舶实际的抵港时间
        if (shipForecast.getExpectArriveTime().isBefore(startTime)) {
            planningShipDTO.readyTime = startTime;
        } else {
            planningShipDTO.readyTime = shipForecast.getExpectArriveTime();
        }

        planningShipDTO.setShipWorkingSequences(shipWorkingSequences);
        // 优先考虑来自于船期预报的载重吨
        if (shipForecast.getInOutLoadQty() == null) {
            planningShipDTO.setTotalQty(shipForecast.getLoadQty());
        } else {
            planningShipDTO.setTotalQty(shipForecast.getInOutLoadQty());
        }
        return planningShipDTO;
    }


    public boolean lengthMoreThan289() {
        boolean flag = this.shipForecast.getShipLength().compareTo(BigDecimal.valueOf(289)) >= 0;
        return flag;
    }

    public boolean lengthMoreThan300() {
        boolean flag = this.shipForecast.getShipLength().compareTo(BigDecimal.valueOf(300)) >= 0;
        return flag;
    }

    /**
     * 吃水超过18.3
     *
     * @return
     */
    public boolean draftMoreThan18_3() {
        boolean flag = this.shipForecast.getDraft().compareTo(new BigDecimal("18.3")) > 0;
        return flag;
    }

    /**
     * 计算靠泊时间、开工时间、完工时间、离泊时间
     *
     * @return
     */
    public boolean planning() {
        MachineTypeEnum machineTypeEnum = null;
        Integer machineCount = 0;


        // 循环卸序 匹配固机类型、数量、工时
        for (ShipWorkingSequence shipWorkingSequence : this.shipWorkingSequences) {
            BigDecimal singleShipWorkHourQty = shipWorkingSequence.getSingleShipWorkHourQty();
            List<Integer> cabinNoList = Arrays.stream(shipWorkingSequence.getShipCabinNo().split(",")).map(Integer::valueOf).collect(Collectors.toList());

            if (this.occupiedBerth.isD1()) {
                List<Integer> gaps = new ArrayList<>();
                machineCount = twoMachinePerCabin(cabinNoList, gaps);
                machineTypeEnum = MachineTypeEnum.ZH;
                if (machineCount > 4) machineCount = 4;
            } else if (this.isUnLoad()) {
                List<Integer> gaps = new ArrayList<>();
                machineCount = twoMachinePerCabin(cabinNoList, gaps);
                machineTypeEnum = MachineTypeEnum.ME;
                if (machineCount > 9) machineCount = 9;
            } else {


                if ("D4".equals(occupiedBerth.getBerthInfo().getBerthNo()) || "D5".equals(occupiedBerth.getBerthInfo().getBerthNo())) {
                    machineTypeEnum = MachineTypeEnum.SL;
                    machineCount = 1;
                } else {
                    List<Integer> gaps = new ArrayList<>();
                    machineCount = twoMachinePerCabin(cabinNoList, gaps);
                    machineTypeEnum = MachineTypeEnum.ME;
                    if (machineCount > 9) machineCount = 9;
                }
            }
            shipWorkingSequence.setMachineCount(machineCount);
            shipWorkingSequence.setMachineTypeCode(machineTypeEnum.getCode());
            BigDecimal workHour = shipWorkingSequence.getTotalWeight().divide(singleShipWorkHourQty).divide(BigDecimal.valueOf(machineCount), 1, RoundingMode.CEILING);
            shipWorkingSequence.setWorkHours(workHour);
        }

        // 工作时长 小时
        BigDecimal totalWorkHour = shipWorkingSequences.stream().map(t -> t.getWorkHours()).reduce(BigDecimal.ZERO, (w1, w2) -> w1.add(w2));
        this.shipWorkingDuration = totalWorkHour;
        // 整船平均舱时量
        BigDecimal shipAvgCapacityPerHour = this.totalQty.divide(totalWorkHour);
        this.shipForecast.setCapacityPerHour(shipAvgCapacityPerHour);

        // 工作时间转换为分钟
        BigDecimal durationMinute = totalWorkHour.multiply(BigDecimal.valueOf(60)).setScale(0, RoundingMode.CEILING);

        // 水尺时间
        Integer middleWaterNum = this.shipForecast.getMiddleWaterNum();
        // 每次水尺1.5小时
        BigDecimal middleWaterDuration = BigDecimal.valueOf(middleWaterNum).multiply(new BigDecimal("1.5")).multiply(BigDecimal.valueOf(60)).setScale(0, RoundingMode.CEILING);

        // 设置靠泊时间
        this.anchorTime = this.readyTime;
        // 开工时间
        // D1泊位 +2小时
        if (this.occupiedBerth.isD1()) {
            this.startTime = this.anchorTime.plusMinutes(120);
        } else {
            if (isInTrade()) {
                // 内贸+1小时
                this.startTime = this.anchorTime.plusMinutes(60);
            } else {
                // 外贸+1小时
                this.startTime = this.anchorTime.plusMinutes(90);
            }
        }
        // 完工时间 = 开工时间 +作业时间 + 水尺时间
        this.endTime = this.startTime.plusMinutes(durationMinute.longValue()).plusMinutes(middleWaterDuration.longValue());


        // 离港时间规则：
        // D1泊位 +2小时
        if (this.occupiedBerth.isD1()) {
            this.leaveTime = this.endTime.plusMinutes(120);
        } else {
            if (isInTrade()) {
                // 内贸+1小时
                this.leaveTime = this.endTime.plusMinutes(60);
            } else {
                // 外贸+1小时
                this.leaveTime = this.endTime.plusMinutes(90);
            }
        }
        return true;

    }

    /**
     * 每个舱口两台门机 相邻舱口只能有一台门机
     * 相邻舱如果不作业，则同一个舱可以有两台门机
     *
     * @param cabinNoList
     * @param gaps
     * @return
     */
    private Integer twoMachinePerCabin(List<Integer> cabinNoList, List<Integer> gaps) {
        Integer machineCount;
        for (int i = cabinNoList.size() - 1; i > 0; i--) {
            gaps.add(cabinNoList.get(i) - cabinNoList.get(i - 1));
        }
        int gap1Count = gaps.stream().filter(t -> t == 1).collect(Collectors.counting()).intValue();
        machineCount = (cabinNoList.size() - gap1Count) * 2;
        return machineCount;
    }


    /**
     * 设置占用泊位、缆柱
     *
     * @return
     */
    public boolean setOccupied() {

        return true;
    }


    /**
     * 内贸
     *
     * @return
     */
    public boolean isInTrade() {
        return TradeTypeEnum.IN.getCode() == this.shipForecast.getInOutTrade();
    }

    /**
     * 内贸
     *
     * @return
     */
    public boolean isOutTrade() {
        return TradeTypeEnum.OUT.getCode() == this.shipForecast.getInOutTrade();
    }

    /**
     * 装船
     *
     * @return
     */
    public boolean isLoad() {
        return LoadUnloadEnum.LOAD.getCode() == this.shipForecast.getLoadUnload();
    }

    /**
     * 卸船
     *
     * @return
     */
    public boolean isUnLoad() {
        return LoadUnloadEnum.UNLOAD.getCode() == this.shipForecast.getLoadUnload();
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
