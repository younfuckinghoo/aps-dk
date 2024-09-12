package com.aps.yinghai.service.igtos.impl;

import com.aps.yinghai.config.datasource.DataSourceFlag;
import com.aps.yinghai.enums.DataSourceFlagEnum;
import com.aps.yinghai.iGTOS.ShipCycle;
import com.aps.yinghai.mapper.igtos.ShipCycleMapper;
import com.aps.yinghai.service.igtos.IShipCycleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ShipCycleServiceImpl extends ServiceImpl<ShipCycleMapper, ShipCycle> implements IShipCycleService {

    @DataSourceFlag(DataSourceFlagEnum.IGTOS)
    @Override
    public List<ShipCycle> list(){
        return super.list();
    }
}
