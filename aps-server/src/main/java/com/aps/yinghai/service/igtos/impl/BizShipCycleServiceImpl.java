package com.aps.yinghai.service.igtos.impl;

import com.aps.yinghai.config.datasource.DataSourceFlag;
import com.aps.yinghai.enums.DataSourceFlagEnum;
import com.aps.yinghai.iGTOS.BizShipCycle;
import com.aps.yinghai.mapper.igtos.ShipCycleMapper;
import com.aps.yinghai.service.igtos.IBizShipCycleService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class BizShipCycleServiceImpl extends ServiceImpl<ShipCycleMapper, BizShipCycle> implements IBizShipCycleService {

    @DataSourceFlag(DataSourceFlagEnum.IGTOS)
    @Override
    public List<BizShipCycle> list(){
        return super.list();
    }

    @DataSourceFlag(DataSourceFlagEnum.IGTOS)
    @Override
    public List<BizShipCycle> listByShipNoList(List<String> shipNoList) {
        LambdaQueryWrapper<BizShipCycle> lambdaQueryWrapper = Wrappers.lambdaQuery(BizShipCycle.class).in(BizShipCycle::getShipNo, shipNoList);
        return this.list(lambdaQueryWrapper);
    }
}
