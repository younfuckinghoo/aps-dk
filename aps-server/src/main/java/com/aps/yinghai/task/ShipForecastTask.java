package com.aps.yinghai.task;

import com.aps.yinghai.entity.ShipForecast;
import com.aps.yinghai.entity.ShipWorkingInfoDetail;
import com.aps.yinghai.entity.ShipWorkingSequence;
import com.aps.yinghai.enums.ShipAlgStateEnum;
import com.aps.yinghai.iGTOS.*;
import com.aps.yinghai.mapper.ShipForecastMapper;
import com.aps.yinghai.mapper.ShipWorkingInfoDetailMapper;
import com.aps.yinghai.mapper.ShipWorkingSequenceMapper;
import com.aps.yinghai.service.IShipForecastService;
import com.aps.yinghai.service.igtos.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Component
public class ShipForecastTask {

    public static final Logger logger = LoggerFactory.getLogger(ShipForecastTask.class);

    private final ShipForecastMapper shipForecastMapper;
    private final ShipWorkingSequenceMapper shipWorkingSequenceMapper;
    private final ShipWorkingInfoDetailMapper shipWorkingInfoDetailMapper;
    private final IBizShipPrePlanService iBizShipPrePlanService;
    private final IBizShipCycleService iBizShipCycleService;
    private final IBizShipCycleExtendService iBizShipCycleExtendService;
    private final IShipForecastService iShipForecastService;
    private final IBizShipWorkDetailService iBizShipWorkDetailService;
    private final IBizShipWorkSequenceService iBizShipWorkSequenceService;

    public ShipForecastTask(ShipForecastMapper shipForecastMapper, ShipWorkingSequenceMapper shipWorkingSequenceMapper, ShipWorkingInfoDetailMapper shipWorkingInfoDetailMapper, IBizShipPrePlanService iBizShipPrePlanService, IBizShipCycleService iBizShipCycleService, IBizShipCycleExtendService iBizShipCycleExtendService, IShipForecastService iShipForecastService, IBizShipWorkDetailService iBizShipWorkDetailService, IBizShipWorkSequenceService iBizShipWorkSequenceService) {
        this.shipForecastMapper = shipForecastMapper;
        this.shipWorkingSequenceMapper = shipWorkingSequenceMapper;
        this.shipWorkingInfoDetailMapper = shipWorkingInfoDetailMapper;
        this.iBizShipPrePlanService = iBizShipPrePlanService;
        this.iBizShipCycleService = iBizShipCycleService;
        this.iBizShipCycleExtendService = iBizShipCycleExtendService;
        this.iShipForecastService = iShipForecastService;
        this.iBizShipWorkDetailService = iBizShipWorkDetailService;
        this.iBizShipWorkSequenceService = iBizShipWorkSequenceService;
    }

    @Scheduled(cron = "0 0/5 * * * ?")
    public void pullShipPrePlan(){
        List<BizShipPrePlan> bizShipPrePlans;
        List<BizShipCycle> bizShipCycles;
        List<BizShipCycleExtend> bizShipCycleExtends;
        Map<String, BizShipCycleExtend> shipCycleExtendMap = null;
        Map<String, BizShipCycle> shipCycleMap = null;
        logger.info("---------------------pullShipPrePlan--------------------");
        // 1.首先查询当前算法数据中最大的创建日期
        LocalDateTime localDateTime = shipForecastMapper.checkMaxCreateTime();
        if (localDateTime == null)localDateTime = LocalDateTime.of(2001,1,1,0,0,0);
        // 2.查询iGTOS的预计划表中大于这个创建日期的数据
        bizShipPrePlans = iBizShipPrePlanService.listShipPrePlanAfterTime(localDateTime);
        // 3.查到数据后查询其对应的扩展表、船期、船期扩展表
        if (!CollectionUtils.isEmpty(bizShipPrePlans)){
            List<String> shipNoList = bizShipPrePlans.stream().filter(t -> StringUtils.isNoneBlank(t.getShipNo())).map(t -> t.getShipNo()).collect(Collectors.toList());
            bizShipCycles = iBizShipCycleService.listByShipNoList(shipNoList);
            if (!CollectionUtils.isEmpty(bizShipCycles)){
                shipCycleMap = bizShipCycles.stream().collect(Collectors.toMap(t -> t.getShipNo(), t -> t));
                List<String> shipCycleIdList = bizShipCycles.stream().map(t -> t.getId()).collect(Collectors.toList());
                bizShipCycleExtends = iBizShipCycleExtendService.listByShipCycleIdList(shipCycleIdList);
                shipCycleExtendMap = bizShipCycleExtends.stream().collect(Collectors.toMap(t -> t.getShipcycleId(), t -> t));
            }
        }

        List<ShipForecast> shipForecastList = new ArrayList<>();
        // 4.将这些数据整合到船舶预报表中
        for (BizShipPrePlan plan : bizShipPrePlans) {
            ShipForecast shipForecast = new ShipForecast();
            shipForecast.setId(plan.getId());
            shipForecast.setCreateDate(plan.getCreateDate());
            shipForecast.setReviseDate(plan.getReviseDate());
            shipForecast.setShipName(plan.getShipName());
            // 初始化算法状态
            shipForecast.setAlgorithmState(ShipAlgStateEnum.NOT_READY.getCode());
            shipForecast.setArriveDraught(plan.getArriveDraught());
            shipForecast.setLeaveDraught(plan.getLeaveDraught());
            shipForecast.setExpectArriveTime(plan.getExpectArriveTime());
            shipForecast.setLoadQty(plan.getLoadQty());
            shipForecast.setLoadUnload(plan.getLoadUnload());
            shipForecast.setWaitConstantDrainageTime(plan.getWaitConstantDrainageTime());
            shipForecast.setShipStatus(plan.getShipStatus());
            shipForecast.setCapacityPerHour(plan.getCapacityPerHour());
            shipForecast.setIsSpecialWork(plan.getIsSpecialWork());
            shipForecast.setWaterRatio(plan.getWaterRatio());


            if (StringUtils.isNotBlank(plan.getShipNo())) {
                shipForecast.setShipNo(plan.getShipNo());
                BizShipCycle bizShipCycle = shipCycleMap.get(plan.getShipNo());
                if (bizShipCycle!=null){
                    shipForecast.setIgtosShipcycleId(bizShipCycle.getId());
                    shipForecast.setShipName(bizShipCycle.getShipNameCh());
                    shipForecast.setInOutTrade(bizShipCycle.getInOutTrade());
                    shipForecast.setDeadweightTon(bizShipCycle.getDeadweightTon());
                    shipForecast.setShipLength(bizShipCycle.getShipLength());
                    shipForecast.setShipWidth(bizShipCycle.getShipWidth());
                    shipForecast.setArriveStartWater(bizShipCycle.getArriveStartWater());
                    shipForecast.setMiddleWater(bizShipCycle.getMiddleWater());
                    shipForecast.setArriveEndWater(bizShipCycle.getArriveEndWater());
                    shipForecast.setLeaveStartWater(bizShipCycle.getLeaveStartWater());
                    shipForecast.setMiddleWaterOut(bizShipCycle.getMiddleWaterOut());
                    shipForecast.setLeaveEndWater(bizShipCycle.getLeaveEndWater());
                    shipForecast.setShipagentInName(bizShipCycle.getShipagentInName());
                    shipForecast.setShipagentOutName(bizShipCycle.getShipagentOutName());
                    shipForecast.setForwarder(bizShipCycle.getForwarder());
                    shipForecast.setHgfxztIsrelease(bizShipCycle.getHgfxztIsrelease());
                    shipForecast.setDateArrive(bizShipCycle.getDateArrive());

                    if (bizShipCycle.getReviseDate()!=null && bizShipCycle.getReviseDate().isAfter(shipForecast.getReviseDate())){
                        shipForecast.setReviseDate(bizShipCycle.getReviseDate());
                    }

                    BizShipCycleExtend bizShipCycleExtend = shipCycleExtendMap.get(bizShipCycle.getId());
                    if (bizShipCycleExtend!=null){
                        shipForecast.setIsGuarantee(bizShipCycleExtend.getIsGuarantee());
                        shipForecast.setIsHsysb(bizShipCycleExtend.getIsHsysb());
                        shipForecast.setLoadUnload(bizShipCycleExtend.getLoadUnload());
                        shipForecast.setOriginPlace(bizShipCycleExtend.getOriginPlace());
                        shipForecast.setIsInbond(bizShipCycleExtend.getIsInbond());
                        shipForecast.setIsUnloadShipEntrust(bizShipCycleExtend.getIsUnloadShipEntrust());
                        shipForecast.setIsCargoDeclaration(bizShipCycleExtend.getIsCargoDeclaration());
                        shipForecast.setIsLoadShipNotice(bizShipCycleExtend.getIsLoadShipNotice());
                        shipForecast.setIsUrlFhzl(bizShipCycleExtend.getIsUrlFhzl());
                        if (bizShipCycleExtend.getReviseDate()!=null && bizShipCycleExtend.getReviseDate().isAfter(shipForecast.getReviseDate())){
                            shipForecast.setReviseDate(bizShipCycleExtend.getReviseDate());
                        }
                    }

                }
            }
            shipForecastList.add(shipForecast);
        }
        if (!CollectionUtils.isEmpty(shipForecastList)){
            List<String> prePlanIdList = shipForecastList.stream().map(t -> t.getId()).collect(Collectors.toList());
            List<BizShipPlanWorkDetail> bizShipPlanWorkDetails = iBizShipWorkDetailService.listByPrePlanIdList(prePlanIdList);
            List<BizShipWorkSequence> bizShipWorkSequences = iBizShipWorkSequenceService.listByPrePlanIdList(prePlanIdList);
            List<ShipWorkingInfoDetail> workingInfoDetailList = bizShipPlanWorkDetails.stream().map(t -> {
                ShipWorkingInfoDetail shipWorkingInfoDetail = new ShipWorkingInfoDetail();
                shipWorkingInfoDetail.setId(t.getId());
                shipWorkingInfoDetail.setShipForecastId(t.getShipPrePlanId());
                shipWorkingInfoDetail.setWaterRatio(t.getWaterRatio());
                shipWorkingInfoDetail.setTicketNum(t.getTicketNum());
                shipWorkingInfoDetail.setWaterEstimationTime(t.getWaterEstimationTime());
                shipWorkingInfoDetail.setCreateDate(t.getCreateDate());
                shipWorkingInfoDetail.setReviseDate(t.getReviseDate());
                return shipWorkingInfoDetail;
            }).collect(Collectors.toList());
            List<ShipWorkingSequence> workingSequenceList = bizShipWorkSequences.stream().map(t -> {
                ShipWorkingSequence shipWorkingSequence = new ShipWorkingSequence();
                shipWorkingSequence.setId(t.getId());
                shipWorkingSequence.setShipCabinNo(t.getShipCabinNo());
                shipWorkingSequence.setShipForecastId(t.getShipPrePlanId());
                shipWorkingSequence.setSingleShipWorkHourQty(t.getSingleShipWorkHourQty());
                shipWorkingSequence.setTotalWeight(t.getTotalWeight());
                shipWorkingSequence.setCreateDate(t.getCreateDate());
                shipWorkingSequence.setReviseDate(t.getReviseDate());
                return shipWorkingSequence;
            }).collect(Collectors.toList());
            shipWorkingInfoDetailMapper.insert(workingInfoDetailList);
            shipWorkingSequenceMapper.insert(workingSequenceList);
        }


        iShipForecastService.saveBatch(shipForecastList);


    }









}
