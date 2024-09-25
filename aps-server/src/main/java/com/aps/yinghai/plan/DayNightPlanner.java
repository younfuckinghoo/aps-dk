package com.aps.yinghai.plan;

import com.aps.yinghai.dto.PlanningBerthDTO;
import com.aps.yinghai.dto.PlanningBerthPoolDTO;
import com.aps.yinghai.dto.PlanningBollardDTO;
import com.aps.yinghai.dto.PlanningShipDTO;
import com.aps.yinghai.dto.igtos.BizDayNightClassDTO;
import com.aps.yinghai.dto.igtos.BizShipWorkPlanDTO;
import com.aps.yinghai.entity.BollardInfo;
import com.aps.yinghai.entity.ShipWorkingSequence;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


public class DayNightPlanner {


    private final LocalDateTime startTime;
    private final LocalDateTime firstEndTime;
    private final LocalDateTime secondEndTime;

    private final PlanningBerthPoolDTO planningBerthPoolDTO;
    private final List<BollardInfo> bollardInfoList;


    private Map<String, BizShipWorkPlanDTO> workPlanDTOMap = new HashMap();

    public DayNightPlanner(LocalDateTime startTime, LocalDateTime firstEndTime, LocalDateTime secondEndTime, PlanningBerthPoolDTO planningBerthPoolDTO, List<BollardInfo> bollardInfoList) {
        this.startTime = startTime;
        this.firstEndTime = firstEndTime;
        this.secondEndTime = secondEndTime;
        this.planningBerthPoolDTO = planningBerthPoolDTO;
        this.bollardInfoList = bollardInfoList;
    }

    public boolean planningClass(PlanningShipDTO planningShipDTO) {
        LocalDateTime shipStartTime = planningShipDTO.getStartTime();
        // 工作计划DTO
        BizShipWorkPlanDTO bizShipWorkPlanDTO = workPlanDTOMap.get(planningShipDTO.getShipForecast().getIgtosShipcycleId());
        if (bizShipWorkPlanDTO == null) {
            bizShipWorkPlanDTO = BizShipWorkPlanDTO.buildWorkPlan(planningShipDTO, startTime.withHour(0).withMinute(0).withSecond(0));
            workPlanDTOMap.put(planningShipDTO.getShipForecast().getIgtosShipcycleId(), bizShipWorkPlanDTO);
        }
        List<PlanningBollardDTO> bollardDTOList = new ArrayList<>();
        findAlternativeBollardDTO(planningShipDTO, bollardDTOList);
        // 匹配实际用到的缆柱
        List<PlanningBollardDTO> actualBollardList = filterActualBollardList(planningShipDTO, bollardDTOList);
        // 找到最晚的占用时间
        Optional<BollardInfo> latestBollard = actualBollardList.stream().map(t -> t.getBollardInfo()).filter(t -> t.getOccupyUntil() != null).sorted((a, b) -> b.getOccupyUntil().compareTo(a.getOccupyUntil())).findFirst();
        // 如果榄桩占用时间超过其预定的开工时间 那么开工时间就需要延后
        if (latestBollard.isPresent() && shipStartTime.isBefore(latestBollard.get().getOccupyUntil())) {
            shipStartTime = latestBollard.get().getOccupyUntil();
            planningShipDTO.setStartTime(shipStartTime);
        }


        LocalDateTime workUntil = shipStartTime.plusMinutes(0);
        // 属于夜计划
        if (shipStartTime.isBefore(firstEndTime)) {


            BizDayNightClassDTO nightClass = bizShipWorkPlanDTO.createNightClass();

            nightClass.getClassPlan().setPlanDate(shipStartTime.withHour(0).withMinute(0).withSecond(0));
            // 循环卸序
            for (int i = 0; i < planningShipDTO.getShipWorkingSequences().size(); ) {
                ShipWorkingSequence shipWorkingSequence = planningShipDTO.getShipWorkingSequences().get(i);
                BigDecimal workHours = shipWorkingSequence.getWorkHours();
                // 如果当前票还有工作要做
                if (workHours.compareTo(BigDecimal.ZERO) > 0) {
                    BigDecimal workMinutes = workHours.multiply(BigDecimal.valueOf(60));
                    LocalDateTime lastStartTime = workUntil.plusSeconds(0);

                    workUntil = workUntil.plusMinutes(workMinutes.longValue());
                    // 如果在这个班范围内 说明这票货能全部干完
                    if (workUntil.isBefore(firstEndTime)) {
                        // 干完了要做1.5小时水尺
                        workUntil = workUntil.plusMinutes(90);
                        // 累加重量
                        nightClass.getClassPlan().setPlanWeight(nightClass.getClassPlan().getPlanWeight().add(shipWorkingSequence.getTotalWeight()));
                        nightClass.getClassPlan().setCargoName(shipWorkingSequence.getCargoName());
                        nightClass.getClassPlan().setWorkTime(nightClass.getClassPlan().getWorkTime().add(shipWorkingSequence.getWorkHours()));
                        shipWorkingSequence.setWorkHours(BigDecimal.ZERO);
                        shipWorkingSequence.setTotalWeight(BigDecimal.ZERO);
                        Integer machineCount = shipWorkingSequence.getMachineCount();
                        if (machineCount==null)machineCount = 0;
                        Integer workLineQty = nightClass.getClassPlan().getWorkLineQty();
                        if (workLineQty==null)workLineQty = 0;
                        nightClass.getClassPlan().setWorkLineQty(Math.max(machineCount,workLineQty));
                        for (PlanningBollardDTO planningBollardDTO : actualBollardList) {
                            // 这一票的工作占用榄桩到什么时候
                            planningBollardDTO.getBollardInfo().setOccupyUntil(workUntil);
                        }

                        i++;
                    } else {
                        // 计算开始时间和到这个班次最后的时间时间差 作为工作时长
                        Duration between = Duration.between(lastStartTime, firstEndTime);
                        long minuteBetween = between.toMinutes();
                        BigDecimal workHour = BigDecimal.valueOf(minuteBetween).divide(BigDecimal.valueOf(60), 1, RoundingMode.CEILING);
                        BigDecimal planWeight = workHour.multiply(shipWorkingSequence.getSingleShipWorkHourQty().multiply(BigDecimal.valueOf(shipWorkingSequence.getMachineCount())));
                        nightClass.getClassPlan().setPlanWeight(nightClass.getClassPlan().getPlanWeight().add(planWeight));
                        nightClass.getClassPlan().setCargoName(shipWorkingSequence.getCargoName());
                        nightClass.getClassPlan().setWorkTime(nightClass.getClassPlan().getWorkTime().add(workHour));

                        shipWorkingSequence.setTotalWeight(shipWorkingSequence.getTotalWeight().subtract(planWeight));
                        shipWorkingSequence.setWorkHours(shipWorkingSequence.getWorkHours().subtract(workHour));

                        Integer machineCount = shipWorkingSequence.getMachineCount();
                        if (machineCount==null)machineCount = 0;
                        Integer workLineQty = nightClass.getClassPlan().getWorkLineQty();
                        if (workLineQty==null)workLineQty = 0;
                        nightClass.getClassPlan().setWorkLineQty(Math.max(machineCount,workLineQty));
                        for (PlanningBollardDTO planningBollardDTO : actualBollardList) {
                            // 这一票的工作占用榄桩到什么时候
                            planningBollardDTO.getBollardInfo().setOccupyUntil(firstEndTime);
                        }
                        planningShipDTO.setStartTime(firstEndTime);

                        // 这个班就排满了跳出循环
                        break;
                    }
                } else {
                    i++;
                }
            }


        } else {
            // 昼计划
            BizDayNightClassDTO dayClass = bizShipWorkPlanDTO.createDayClass();
            dayClass.getClassPlan().setPlanDate(shipStartTime.withHour(0).withMinute(0).withSecond(0));
            // 循环卸序
            for (int i = 0; i < planningShipDTO.getShipWorkingSequences().size(); ) {
                ShipWorkingSequence shipWorkingSequence = planningShipDTO.getShipWorkingSequences().get(i);
                BigDecimal workHours = shipWorkingSequence.getWorkHours();
                // 如果当前票还有工作要做
                if (workHours.compareTo(BigDecimal.ZERO) > 0) {
                    BigDecimal workMinutes = workHours.multiply(BigDecimal.valueOf(60));
                    LocalDateTime lastStartTime = workUntil.plusSeconds(0);

                    workUntil = workUntil.plusMinutes(workMinutes.longValue());
                    // 如果在这个班范围内 说明这票货能全部干完
                    if (workUntil.isBefore(secondEndTime)) {
                        // 干完了要做1.5小时水尺
                        workUntil = workUntil.plusMinutes(90);
                        // 累加重量
                        dayClass.getClassPlan().setPlanWeight(dayClass.getClassPlan().getPlanWeight().add(shipWorkingSequence.getTotalWeight()));
                        dayClass.getClassPlan().setCargoName(shipWorkingSequence.getCargoName());
                        dayClass.getClassPlan().setWorkTime(dayClass.getClassPlan().getWorkTime().add(shipWorkingSequence.getWorkHours()));
                        shipWorkingSequence.setWorkHours(BigDecimal.ZERO);
                        shipWorkingSequence.setTotalWeight(BigDecimal.ZERO);
                        Integer machineCount = shipWorkingSequence.getMachineCount();
                        if (machineCount==null)machineCount = 0;
                        Integer workLineQty = dayClass.getClassPlan().getWorkLineQty();
                        if (workLineQty==null)workLineQty = 0;
                        dayClass.getClassPlan().setWorkLineQty(Math.max(machineCount,workLineQty));
                        for (PlanningBollardDTO planningBollardDTO : actualBollardList) {
                            // 这一票的工作占用榄桩到什么时候
                            planningBollardDTO.getBollardInfo().setOccupyUntil(workUntil);
                        }

                        i++;
                    } else {
                        // 计算开始时间和到这个班次最后的时间时间差 作为工作时长
                        Duration between = Duration.between(lastStartTime, secondEndTime);
                        long minuteBetween = between.toMinutes();
                        BigDecimal workHour = BigDecimal.valueOf(minuteBetween).divide(BigDecimal.valueOf(60), 1, RoundingMode.CEILING);
                        BigDecimal planWeight = workHour.multiply(shipWorkingSequence.getSingleShipWorkHourQty().multiply(BigDecimal.valueOf(shipWorkingSequence.getMachineCount())));
                        dayClass.getClassPlan().setPlanWeight(dayClass.getClassPlan().getPlanWeight().add(planWeight));
                        dayClass.getClassPlan().setCargoName(shipWorkingSequence.getCargoName());
                        dayClass.getClassPlan().setWorkTime(dayClass.getClassPlan().getWorkTime().add(workHour));
                        Integer machineCount = shipWorkingSequence.getMachineCount();
                        if (machineCount==null)machineCount = 0;
                        Integer workLineQty = dayClass.getClassPlan().getWorkLineQty();
                        if (workLineQty==null)workLineQty = 0;
                        dayClass.getClassPlan().setWorkLineQty(Math.max(machineCount,workLineQty));

                        shipWorkingSequence.setTotalWeight(shipWorkingSequence.getTotalWeight().subtract(planWeight));
                        shipWorkingSequence.setWorkHours(shipWorkingSequence.getWorkHours().subtract(workHour));

                        for (PlanningBollardDTO planningBollardDTO : actualBollardList) {
                            // 这一票的工作占用榄桩到什么时候
                            planningBollardDTO.getBollardInfo().setOccupyUntil(secondEndTime);
                        }
                        planningShipDTO.setStartTime(secondEndTime);

                        // 这个班就排满了跳出循环
                        break;
                    }
                } else {
                    i++;
                }
            }

        }

        return true;
    }

    /**
     * 过滤出来实际用到到缆柱
     *
     * @param planningShipDTO
     * @param bollardDTOList
     * @return
     */
    private List<PlanningBollardDTO> filterActualBollardList(PlanningShipDTO planningShipDTO, List<PlanningBollardDTO> bollardDTOList) {
        String bollardForward = planningShipDTO.getShipForecast().getBollardForward();
        String bollardBehind = planningShipDTO.getShipForecast().getBollardBehind();
        Integer from = Integer.valueOf(bollardForward);
        Integer to = Integer.valueOf(bollardBehind);
        return bollardDTOList.stream().filter(t -> {

            return t.getBollardInfo().getBollardNo() >= from && t.getBollardInfo().getBollardNo() <= to;
        }).collect(Collectors.toList());

    }

    /**
     * 找到可能用到的缆柱
     *
     * @param planningShipDTO
     * @param bollardDTOList
     */
    private void findAlternativeBollardDTO(PlanningShipDTO planningShipDTO, List<PlanningBollardDTO> bollardDTOList) {
        String berthNo = planningShipDTO.getShipForecast().getBerthNo();
        PlanningBerthDTO berthDTO = planningBerthPoolDTO.getBerthByBerthNo(berthNo);
        PlanningBerthDTO preBerth = berthDTO.getPreBerth();
        PlanningBerthDTO nextBerth = berthDTO.getNextBerth();
        if (preBerth != null) bollardDTOList.addAll(preBerth.getBollardDTOList());
        bollardDTOList.addAll(berthDTO.getBollardDTOList());
        if (nextBerth != null) bollardDTOList.addAll(nextBerth.getBollardDTOList());
    }


    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getFirstEndTime() {
        return firstEndTime;
    }

    public LocalDateTime getSecondEndTime() {
        return secondEndTime;
    }

    public PlanningBerthPoolDTO getPlanningBerthPoolDTO() {
        return planningBerthPoolDTO;
    }

    public List<BollardInfo> getBollardInfoList() {
        return bollardInfoList;
    }

    public Map<String, BizShipWorkPlanDTO> getWorkPlanDTOMap() {
        return workPlanDTOMap;
    }

    public void setWorkPlanDTOMap(Map<String, BizShipWorkPlanDTO> workPlanDTOMap) {
        this.workPlanDTOMap = workPlanDTOMap;
    }
}
