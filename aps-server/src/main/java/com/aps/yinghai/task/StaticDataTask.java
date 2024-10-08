package com.aps.yinghai.task;

import com.aps.yinghai.constant.PlanConstant;
import com.aps.yinghai.entity.BerthInfo;
import com.aps.yinghai.entity.BollardInfo;
import com.aps.yinghai.entity.CargoInfo;
import com.aps.yinghai.entity.Tide;
import com.aps.yinghai.iGTOS.BizBerthInfo;
import com.aps.yinghai.iGTOS.BizBollardInfo;
import com.aps.yinghai.iGTOS.BizCargoInfo;
import com.aps.yinghai.iGTOS.BizTide;
import com.aps.yinghai.mapper.BerthInfoMapper;
import com.aps.yinghai.mapper.BollardInfoMapper;
import com.aps.yinghai.mapper.CargoInfoMapper;
import com.aps.yinghai.mapper.TideMapper;
import com.aps.yinghai.service.igtos.IBizBerthInfoService;
import com.aps.yinghai.service.igtos.IBizBollardInfoService;
import com.aps.yinghai.service.igtos.IBizCargoInfoService;
import com.aps.yinghai.service.igtos.IBizTideService;
import com.aps.yinghai.util.DateTimeUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Component
public class StaticDataTask {

    public static final Logger logger = LoggerFactory.getLogger(StaticDataTask.class);

    private final BollardInfoMapper bollardInfoMapper;
    private final CargoInfoMapper cargoInfoMapper;
    private final IBizBollardInfoService iBizBollardInfoService;
    private final BerthInfoMapper berthInfoMapper;
    private final IBizBerthInfoService iBizBerthInfoService;
    private final IBizCargoInfoService iBizCargoInfoService;
    private final TideMapper tideMapper;
    private final IBizTideService iBizTideService;

    public StaticDataTask(IBizBollardInfoService iBizBollardInfoService, BollardInfoMapper bollardInfoMapper, CargoInfoMapper cargoInfoMapper, BerthInfoMapper berthInfoMapper, IBizBerthInfoService iBizBerthInfoService, IBizCargoInfoService iBizCargoInfoService, TideMapper tideMapper, IBizTideService iBizTideService) {
        this.bollardInfoMapper = bollardInfoMapper;
        this.cargoInfoMapper = cargoInfoMapper;
        this.berthInfoMapper = berthInfoMapper;
        this.iBizBollardInfoService = iBizBollardInfoService;
        this.iBizBerthInfoService = iBizBerthInfoService;
        this.iBizCargoInfoService = iBizCargoInfoService;
        this.tideMapper = tideMapper;
        this.iBizTideService = iBizTideService;
    }



    /**
     * 同步新增泊位信息
     * 每天两点
     */
//    @Scheduled(cron = "0 0 1 * * ?")
    @Scheduled(cron = "1/31 * * * * ?")
    public void pullBerth(){
        LocalDateTime maxBerthCreateTime = berthInfoMapper.getMaxCreateTime();
        if (maxBerthCreateTime == null)maxBerthCreateTime = LocalDateTime.of(2001,1,1,0,0,0);
        List<BizBerthInfo> bizBerthInfos = iBizBerthInfoService.listBerthAfterTime(maxBerthCreateTime);
        if (!CollectionUtils.isEmpty(bizBerthInfos)){
            List<BerthInfo> berthInfos = bizBerthInfos.stream().map(biz -> {
                BerthInfo berthInfo = new BerthInfo();
                berthInfo.setId(biz.getId());
                berthInfo.setBerthNo(biz.getBerthNo());
                berthInfo.setBerthName(biz.getBerthName());
                berthInfo.setBerthLength(biz.getBerthLength());
                berthInfo.setFlowLoad(biz.getFlowLoad());
                berthInfo.setFlowUnload(biz.getFlowUnload());
                berthInfo.setMoveLoad(biz.getMoveUnload());
                berthInfo.setIsBeLend(biz.getIsLended());
                berthInfo.setAvailable(PlanConstant.YES);
                berthInfo.setIsLend(biz.getIsLend());
                berthInfo.setCreateDate(biz.getCreateDate());
                berthInfo.setReviseDate(biz.getReviseDate());
                return berthInfo;
            }).collect(Collectors.toList());
            berthInfoMapper.insert(berthInfos);
        }


    }


    /**
     * 同步新增系榄桩
     * 每天一点
     */
//    @Scheduled(cron = "0 0 2 * * ?")
    @Scheduled(cron = "1/59 * * * * ?")
    public void pullBollard(){
        LocalDateTime maxBollardCreateTime = bollardInfoMapper.getMaxCreateTime();
        if (maxBollardCreateTime == null)maxBollardCreateTime = LocalDateTime.of(2001,1,1,0,0,0);
        List<BizBollardInfo> bizBollardInfos = iBizBollardInfoService.listBollardAfterTime(maxBollardCreateTime);
        List<String> berthIdList = bizBollardInfos.stream().map(t -> t.getBerthId()).collect(Collectors.toList());
        List<BerthInfo> berthInfos = berthInfoMapper.selectList(Wrappers.lambdaQuery(BerthInfo.class).in(BerthInfo::getId, berthIdList));
        Map<String, BerthInfo> berthMap = berthInfos.stream().collect(Collectors.toMap(t -> t.getId(), t -> t));

        if (!CollectionUtils.isEmpty(bizBollardInfos)){
            List<BollardInfo> bollardInfos = bizBollardInfos.stream().map(biz -> {
                BollardInfo bollardInfo = new BollardInfo();
                bollardInfo.setId(biz.getId());
                bollardInfo.setBollardNo(biz.getBollardNo());
                BerthInfo berthInfo = berthMap.get(biz.getBerthId());
                if (berthInfo!=null){
                    bollardInfo.setBerthId(berthInfo.getId());
                    bollardInfo.setBerthNo(berthInfo.getBerthNo());
                }

                bollardInfo.setBollardName(biz.getBollardName());
                bollardInfo.setLastBollardDistance(biz.getLastBollardDistance());
                bollardInfo.setShorelineDistance(biz.getShorelineDistance());
                bollardInfo.setAvailable(biz.getIsStop());
                bollardInfo.setCreateDate(biz.getCreateDate());
                bollardInfo.setReviseDate(biz.getReviseDate());
                return bollardInfo;
            }).collect(Collectors.toList());
            bollardInfoMapper.insert(bollardInfos);
        }


    }




    /**
     * 同步新增货物信息
     * 每天三点
     */

//    @Scheduled(cron = "0 0 3 * * ?")
    @Scheduled(cron = "1/31 * * * * ?")
    public void pullCargo(){
        LocalDateTime maxCargoCreateTime = cargoInfoMapper.getMaxCreateTime();
        if (maxCargoCreateTime == null)maxCargoCreateTime = LocalDateTime.of(2001,1,1,0,0,0);
        List<BizCargoInfo> bizCargoInfos = iBizCargoInfoService.listCargoAfterTime(maxCargoCreateTime);
        if (!CollectionUtils.isEmpty(bizCargoInfos)){
            List<CargoInfo> cargoInfos = bizCargoInfos.stream().map(biz -> {
                CargoInfo cargoInfo = new CargoInfo();
                cargoInfo.setId(biz.getId());
                cargoInfo.setCargoCode(biz.getCargoNameCode());
                cargoInfo.setCargoName(biz.getCargoName());
                cargoInfo.setCreateDate(biz.getCreateDate());
                cargoInfo.setReviseDate(biz.getReviseDate());
                return cargoInfo;
            }).collect(Collectors.toList());
            cargoInfoMapper.insert(cargoInfos);
        }


    }



    /**
     * 每月1、10、20日0点爬取数据
     * 延后1.5小时同步拉取
     * 同步新增潮汐信息 yyyy-MM-dd HH:mm:ss
     * 每天四点
     */
    private String tide_date_format = "yyyy-MM-dd";
    private String tide_time_format = "HH:mm";

//    @Scheduled(cron = "0 30 1 1,10,20 * ?")
@Scheduled(cron = "1/31 * * * * ?")
    public void pullTide(){
        LocalDate maxTideDate = tideMapper.getMaxDate();
        if (maxTideDate == null){
            maxTideDate = LocalDate.of(2001,2,1);
        }
        List<BizTide> bizTides = iBizTideService.listTideAfterTime(maxTideDate);
        if (!CollectionUtils.isEmpty(bizTides)){
            List<Tide> cargoInfos = bizTides.stream().map(biz -> {
                Tide tide = new Tide();
                tide.setId(biz.getId());

                tide.setTideDate(DateTimeUtil.parseDate(biz.getTideDate(),this.tide_date_format));
                tide.setTideHeight1(new BigDecimal(biz.getTideHeight1()).setScale(2, RoundingMode.CEILING));
                tide.setTideHeight2(new BigDecimal(biz.getTideHeight2()).setScale(2, RoundingMode.CEILING));
                tide.setTideHeight3(new BigDecimal(biz.getTideHeight3()).setScale(2, RoundingMode.CEILING));
                if (StringUtils.isNotBlank(biz.getTideHeight4()))
                tide.setTideHeight4(new BigDecimal(biz.getTideHeight4()).setScale(2, RoundingMode.CEILING));


                tide.setTideTime1(DateTimeUtil.parseTime(biz.getTideTime1(),this.tide_time_format));
                tide.setTideTime2(DateTimeUtil.parseTime(biz.getTideTime2(),this.tide_time_format));
                tide.setTideTime3(DateTimeUtil.parseTime(biz.getTideTime3(),this.tide_time_format));
                if (StringUtils.isNotBlank(biz.getTideTime4()))
                tide.setTideTime4(DateTimeUtil.parseTime(biz.getTideTime4(),this.tide_time_format));
                tide.setH0(new BigDecimal(biz.getH0()).setScale(2, RoundingMode.CEILING));
                tide.setH1(new BigDecimal(biz.getH1()).setScale(2, RoundingMode.CEILING));
                tide.setH2(new BigDecimal(biz.getH2()).setScale(2, RoundingMode.CEILING));
                tide.setH3(new BigDecimal(biz.getH3()).setScale(2, RoundingMode.CEILING));
                tide.setH4(new BigDecimal(biz.getH4()).setScale(2, RoundingMode.CEILING));
                tide.setH5(new BigDecimal(biz.getH5()).setScale(2, RoundingMode.CEILING));
                tide.setH6(new BigDecimal(biz.getH6()).setScale(2, RoundingMode.CEILING));
                tide.setH7(new BigDecimal(biz.getH7()).setScale(2, RoundingMode.CEILING));
                tide.setH8(new BigDecimal(biz.getH8()).setScale(2, RoundingMode.CEILING));
                tide.setH9(new BigDecimal(biz.getH9()).setScale(2, RoundingMode.CEILING));

                tide.setH10(new BigDecimal(biz.getH10()).setScale(2, RoundingMode.CEILING));
                tide.setH11(new BigDecimal(biz.getH11()).setScale(2, RoundingMode.CEILING));
                tide.setH12(new BigDecimal(biz.getH12()).setScale(2, RoundingMode.CEILING));
                tide.setH13(new BigDecimal(biz.getH13()).setScale(2, RoundingMode.CEILING));
                tide.setH14(new BigDecimal(biz.getH14()).setScale(2, RoundingMode.CEILING));
                tide.setH15(new BigDecimal(biz.getH15()).setScale(2, RoundingMode.CEILING));
                tide.setH16(new BigDecimal(biz.getH16()).setScale(2, RoundingMode.CEILING));
                tide.setH17(new BigDecimal(biz.getH17()).setScale(2, RoundingMode.CEILING));
                tide.setH18(new BigDecimal(biz.getH18()).setScale(2, RoundingMode.CEILING));
                tide.setH19(new BigDecimal(biz.getH19()).setScale(2, RoundingMode.CEILING));

                tide.setH20(new BigDecimal(biz.getH20()).setScale(2, RoundingMode.CEILING));
                tide.setH21(new BigDecimal(biz.getH21()).setScale(2, RoundingMode.CEILING));
                tide.setH22(new BigDecimal(biz.getH22()).setScale(2, RoundingMode.CEILING));
                tide.setH23(new BigDecimal(biz.getH23()).setScale(2, RoundingMode.CEILING));

                return tide;
            }).collect(Collectors.toList());
            tideMapper.insert(cargoInfos);
        }


    }









}
