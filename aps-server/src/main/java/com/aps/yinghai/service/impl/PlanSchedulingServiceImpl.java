package com.aps.yinghai.service.impl;

import com.aps.yinghai.constant.PlanConstant;
import com.aps.yinghai.dto.PlanningBerthDTO;
import com.aps.yinghai.dto.PlanningBerthPoolDTO;
import com.aps.yinghai.dto.PlanningShipDTO;
import com.aps.yinghai.entity.BerthInfo;
import com.aps.yinghai.entity.BollardInfo;
import com.aps.yinghai.entity.CabinInfo;
import com.aps.yinghai.entity.ShipForecast;
import com.aps.yinghai.exception.ProgramCalculationException;
import com.aps.yinghai.service.*;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
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

    public PlanSchedulingServiceImpl(IShipForecastService iShipForecastService, ICabinInfoService iCabinInfoService, IBerthInfoService iBerthInfoService, IBollardInfoService iBollardInfoService) {
        this.iShipForecastService = iShipForecastService;
        this.iCabinInfoService = iCabinInfoService;
        this.iBerthInfoService = iBerthInfoService;
        this.iBollardInfoService = iBollardInfoService;
    }


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
        Map<Integer, List<CabinInfo>> cabinListMap = this.listCabinByShip(shipForecastList);

        // 获取泊位列表
        List<BerthInfo> berthInfoList = iBerthInfoService.list(Wrappers.lambdaQuery(BerthInfo.class).eq(BerthInfo::getAvailable, PlanConstant.YES));

        // 获取每个泊位的缆柱
        Map<Integer, List<BollardInfo>> bollardListMap = this.listBollardByShip(berthInfoList);

        List<PlanningShipDTO> shipDTOList = this.planningLongTerm(startTime, shipForecastList, cabinListMap, berthInfoList, bollardListMap);
        shipDTOList.forEach(t->{
            if (t.isPlaned() && t.getOccupiedBerth()!=null) {
                t.getOccupiedBerth().setPreBerth(null);
                t.getOccupiedBerth().setNextBerth(null);
            }
        });
        return shipDTOList;
    }

    private List<PlanningShipDTO> planningLongTerm(LocalDateTime startTime,
                                                   List<ShipForecast> shipForecastList,
                                                   Map<Integer, List<CabinInfo>> cabinListMap,
                                                   List<BerthInfo> berthInfoList,
                                                   Map<Integer, List<BollardInfo>> bollardListMap) {
        // 包装初始化船舶
        List<PlanningShipDTO> shipDTOList = shipForecastList.stream()
                .map(t -> PlanningShipDTO.packageShip(t, cabinListMap.get(t.getId()), startTime))
                .collect(Collectors.toList());


        // 包装初始化泊位
        List<PlanningBerthDTO> berthDTOList = berthInfoList.stream().map(t -> PlanningBerthDTO.packageBerthDTO(t, bollardListMap.get(t.getId()))).collect(Collectors.toList());

        PlanningBerthPoolDTO planningBerthPoolDTO = new PlanningBerthPoolDTO(berthDTOList);
        AtomicInteger atomicInteger = new AtomicInteger(shipDTOList.size());

        int index = 0;
        // 这个循环是一个小时尝试排一次
        do {
            LocalDateTime planningTime = startTime.plusHours(index);
            logger.info("正在排产时间：{}",planningTime);
            // 找到在此刻之前已经到港的未排的船
            List<PlanningShipDTO> availableShipList = shipDTOList.stream().filter(t -> t.getShipForecast().getExceptArriveTime().isBefore(planningTime) && !t.isPlaned()).collect(Collectors.toList());

            // 筛选出大于300米的
            List<PlanningShipDTO> shipMoreThan300 = availableShipList.stream().filter(t -> t.lengthMoreThan300()).sorted((s1, s2) -> s2.getTotalQty().compareTo(s1.getTotalQty())).collect(Collectors.toList());
            // 小于300米的
            List<PlanningShipDTO> shipLessThan300 = availableShipList.stream().filter(t -> !t.lengthMoreThan300()).sorted((s1, s2) -> {
                int compare = s1.getShipForecast().getExceptArriveTime().compareTo(s1.getShipForecast().getExceptArriveTime());
                if (compare==0){
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
                        boolean updateBerth = d1Berth.updateBerth(planningShipDTO,1);
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
                if (planningShipDTO.isInTrade()){
                    List<String> nos = new ArrayList<>();
                    nos.add("D4");
                    // D5吃水不超过16.2
                    if (planningShipDTO.getShipForecast().getDraft().compareTo(new BigDecimal("16.2"))<1){
                        nos.add("D5");
                    }
                    tradeBerthDTOList = planningBerthPoolDTO.getBerthByBerthNo(nos);
                }else {// 外贸
                    tradeBerthDTOList = planningBerthPoolDTO.getBerthByBerthNo(Arrays.asList("D2", "D3"));

                }
                if (!CollectionUtils.isEmpty(tradeBerthDTOList)){
                    selectedBerthLoop:
                    for (PlanningBerthDTO planningBerthDTO : tradeBerthDTOList) {
                        planningShipDTO.setOccupiedBerth(planningBerthDTO);
                        // 根据舱时量计算工作时间 计算靠泊时间、开工时间、完工时间、离泊时间
                        planningShipDTO.planning();
                        // 更新泊位数据，更新榄桩占用时间
                        boolean updateBerth = planningBerthDTO.updateBerth(planningShipDTO,0);
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
            if (index>10000)throw new ProgramCalculationException("排产时间太长，排产错误");
        } while (atomicInteger.get() > 0 );

        return shipDTOList;


    }

    private Map<Integer, List<BollardInfo>> listBollardByShip(List<BerthInfo> berthInfoList) {
        List<Integer> berthIdList = berthInfoList.stream().map(t -> t.getId()).collect(Collectors.toList());
        List<BollardInfo> bollardInfoList = iBollardInfoService.listBollardByBerthIdList(berthIdList);
        Map<Integer, List<BollardInfo>> listMap = bollardInfoList.stream().collect(Collectors.groupingBy(t -> t.getBerthId(), Collectors.toList()));
        return listMap;

    }

    private Map<Integer, List<CabinInfo>> listCabinByShip(List<ShipForecast> shipForecastList) {
        List<Integer> shipIdList = shipForecastList.stream().map(t -> t.getId()).collect(Collectors.toList());
        List<CabinInfo> cabinInfoList = iCabinInfoService.listCabinByShipIdList(shipIdList);
        Map<Integer, List<CabinInfo>> listMap = cabinInfoList.stream().collect(Collectors.groupingBy(t -> t.getShipId(), Collectors.toList()));
        return listMap;
    }

    @Override
    public List dayNightScheduling() {
        return null;
    }


}
