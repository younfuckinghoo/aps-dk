package com.aps.yinghai.service.igtos.impl;

import com.aps.yinghai.config.datasource.DataSourceFlag;
import com.aps.yinghai.enums.DataSourceFlagEnum;
import com.aps.yinghai.iGTOS.BizCargoInfo;
import com.aps.yinghai.iGTOS.BizShipCycle;
import com.aps.yinghai.mapper.igtos.BizCargoMapper;
import com.aps.yinghai.mapper.igtos.ShipCycleMapper;
import com.aps.yinghai.service.igtos.IBizCargoInfoService;
import com.aps.yinghai.service.igtos.IBizShipCycleService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BizCargoInfoServiceImpl extends ServiceImpl<BizCargoMapper, BizCargoInfo> implements IBizCargoInfoService {



    @DataSourceFlag(DataSourceFlagEnum.IGTOS)
    @Override
    public List<BizCargoInfo> listCargoAfterTime(LocalDateTime localDateTime) {
        LambdaQueryWrapper<BizCargoInfo> lambdaQueryWrapper = Wrappers.lambdaQuery(BizCargoInfo.class).gt(BizCargoInfo::getCreateDate, localDateTime);
        return this.list(lambdaQueryWrapper);
    }
}
