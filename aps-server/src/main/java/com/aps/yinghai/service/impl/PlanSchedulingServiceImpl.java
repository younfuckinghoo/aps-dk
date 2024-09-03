package com.aps.yinghai.service.impl;

import com.aps.yinghai.constant.PlanConstant;
import com.aps.yinghai.entity.BerthInfo;
import com.aps.yinghai.entity.BollardInfo;
import com.aps.yinghai.entity.CabinInfo;
import com.aps.yinghai.entity.ShipForecast;
import com.aps.yinghai.service.*;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PlanSchedulingServiceImpl implements IPlanSchedulingService {

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
    public List longTermScheduling() {
        LocalDateTime now = LocalDateTime.now();
        // 默认开始时间为今天18点
        LocalDateTime startTime = now.withHour(18);
        // 如果当前时间过了18点 那就从下一个18点开始排
        if (now.isAfter(startTime)){
            startTime = startTime.plus(1, ChronoUnit.DAYS);
        }

        // 货物手续不齐全的可以进行预排
        List<ShipForecast> shipForecastList = iShipForecastService.listNotPlanningShip();

        // 获取每个船的舱
        Map<Integer,List<CabinInfo>> cabinListMap = this.listCabinByShip(shipForecastList);

        // 获取泊位列表
        List<BerthInfo> berthInfoList = iBerthInfoService.list(Wrappers.lambdaQuery(BerthInfo.class).eq(BerthInfo::getAvailable, PlanConstant.YES));

        // 获取每个泊位的缆柱
        Map<Integer,List<BollardInfo>> bollardListMap = this.listBollardByShip(berthInfoList);





        return null;
    }

    private Map<Integer, List<BollardInfo>> listBollardByShip(List<BerthInfo> berthInfoList) {
        List<Integer> berthIdList = berthInfoList.stream().map(t -> t.getId()).collect(Collectors.toList());
        List<BollardInfo> bollardInfoList = iBollardInfoService.listBollardByBerthIdList(berthIdList);
        Map<Integer, List<BollardInfo>> listMap = bollardInfoList.stream().collect(Collectors.groupingBy(t->t.getBerthId(), Collectors.toList()));
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
