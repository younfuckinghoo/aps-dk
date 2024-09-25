package com.aps.yinghai.service.impl;

import com.aps.yinghai.dto.PlanningBerthDTO;
import com.aps.yinghai.dto.PlanningBerthPoolDTO;
import com.aps.yinghai.dto.PlanningBollardDTO;
import com.aps.yinghai.dto.PlanningShipDTO;
import com.aps.yinghai.dto.igtos.BizDayNightClassDTO;
import com.aps.yinghai.dto.igtos.BizShipWorkPlanDTO;
import com.aps.yinghai.entity.*;
import com.aps.yinghai.enums.ShipStatusEnum;
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
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
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
                int compare = s1.getShipForecast().getExpectArriveTime().compareTo(s1.getShipForecast().getExpectArriveTime());
                if (compare == 0) {
                    return s2.getTotalQty().compareTo(s1.getTotalQty());
                }
                return compare;

            }).collect(Collectors.toList());
            availableShipList.clear();
            availableShipList.addAll(shipMoreThan300);
            availableShipList.addAll(shipLessThan300);

            availableLoop:
            for (PlanningShipDTO planningShipDTO : availableShipList) {
                planningShipDTO.setReadyTime(planningTime);

                // 大于289米选择D1 吃水超过18.3
                if (planningShipDTO.lengthMoreThan289() || planningShipDTO.draftMoreThan18_3()) {
                    PlanningBerthDTO d1Berth = planningBerthPoolDTO.getD1Berth(planningShipDTO.getReadyTime());
                    // 如果能排上
                    if (d1Berth != null) {
                        planningShipDTO.setOccupiedBerth(d1Berth);
                        // 根据舱时量计算工作时间 计算靠泊时间、开工时间、完工时间、离泊时间
                        planningShipDTO.planning();
                        // 更新泊位数据，更新榄桩占用时间
                        boolean updateBerth = d1Berth.updateBerth(planningShipDTO, 1);
                        // 排上了
                        if (updateBerth) {
                            planningShipDTO.setPlaned(true);
                            atomicInteger.getAndDecrement();
                        } else {
                            // 未排上 清除数据
                            planningShipDTO.clearPlanData();
                        }

                    }
                    // 如果排不上就不排了 如果D1排不上就去2-5则注掉continue
                    continue availableLoop;
                }


                List<PlanningBerthDTO> tradeBerthDTOList;
                // 内贸
                if (planningShipDTO.isInTrade()) {
                    List<String> nos = new ArrayList<>();
                    nos.add("D4");
                    // D5吃水不超过16.2
                    if (planningShipDTO.getShipForecast().getArriveDraught() == null || planningShipDTO.getShipForecast().getArriveDraught().compareTo(new BigDecimal("16.2")) < 1) {
                        nos.add("D5");
                    }
                    tradeBerthDTOList = planningBerthPoolDTO.getBerthByBerthNo(nos);
                } else {// 外贸
                    tradeBerthDTOList = planningBerthPoolDTO.getBerthByBerthNo(Arrays.asList("D2", "D3"));

                }
                if (!CollectionUtils.isEmpty(tradeBerthDTOList)) {
                    selectedBerthLoop:
                    for (PlanningBerthDTO planningBerthDTO : tradeBerthDTOList) {
                        planningShipDTO.setOccupiedBerth(planningBerthDTO);
                        // 根据舱时量计算工作时间 计算靠泊时间、开工时间、完工时间、离泊时间
                        planningShipDTO.planning();
                        // 更新泊位数据，更新榄桩占用时间
                        boolean updateBerth = planningBerthDTO.updateBerth(planningShipDTO, 0);
                        // 排上了
                        if (updateBerth) {
                            planningShipDTO.setPlaned(true);
                            atomicInteger.getAndDecrement();
                            // 排下一条船
                            continue availableLoop;
                        } else {
                            // 未排上 清除数据
                            planningShipDTO.clearPlanData();
                        }
                    }
                }


            }


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
        List<ShipForecast> shipForecastList = iShipForecastService.listShipForecastByTimeRange(startTime ,secondEndTime);
        List<String> shipForecastIdList = shipForecastList.stream().map(t -> t.getId()).collect(Collectors.toList());
        // 获取与计划的卸序子表
        List<ShipWorkingSequence> shipWorkingSequences = this.iShipWorkingSequenceService.listByShipForecastIdList(shipForecastIdList);
        Map<String, List<ShipWorkingSequence>> sequenceListMap = shipWorkingSequences.stream().collect(Collectors.groupingBy(t -> t.getShipForecastId(), Collectors.toList()));
        // 获取作业详情子表
        List<ShipWorkingInfoDetail> workingInfoDetailList  = this.iShipWorkingInfoDetailService.listByShipForecastIdList(shipForecastIdList);
        Map<String, List<ShipWorkingInfoDetail>> workingInfoListMap = workingInfoDetailList.stream().collect(Collectors.groupingBy(t -> t.getShipForecastId(), Collectors.toList()));
        // 创建昼夜计划包装列表
        List<PlanningShipDTO> dayNightShipDTOList = shipForecastList.stream().map(t -> PlanningShipDTO.packageDayNightShip(t, sequenceListMap.get(t.getId()), workingInfoListMap.get(t.getId()))).sorted((a,b)->a.getShipForecast().getStartTime().compareTo(b.getShipForecast().getStartTime())).collect(Collectors.toList());


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
            do{
                dayNightPlanner.planningClass(planningShipDTO);
            }while (!planningShipDTO.dayNightPlanDone(secondEndTime));


        }
        System.out.println(dayNightShipDTOList);
        Collection<BizShipWorkPlanDTO> BizShipWorkPlanDTOCollection = dayNightPlanner.getWorkPlanDTOMap().values();
        List<BizShipWorkPlan> workPlanList = new ArrayList<>();
        List<BizDaynightClassPlan> workPlanClassList = new ArrayList<>();
        for (BizShipWorkPlanDTO bizShipWorkPlanDTO : BizShipWorkPlanDTOCollection) {
            BizShipWorkPlan plan = bizShipWorkPlanDTO.getPlan();
            BizDayNightClassDTO dayClass = bizShipWorkPlanDTO.getDayClass();
            BizDayNightClassDTO nightClass = bizShipWorkPlanDTO.getNightClass();
            if (dayClass!=null)
            workPlanClassList.add(dayClass.getClassPlan());
            if (nightClass!=null)
            workPlanClassList.add(nightClass.getClassPlan());
            BigDecimal planWeight = BigDecimal.ZERO;
            if (dayClass!=null){
                planWeight = planWeight.add(dayClass.getClassPlan().getPlanWeight());
            }
            if (nightClass!=null){
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
