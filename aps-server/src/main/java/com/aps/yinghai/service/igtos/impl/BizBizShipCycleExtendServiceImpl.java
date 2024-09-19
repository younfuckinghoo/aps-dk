package com.aps.yinghai.service.igtos.impl;

import com.aps.yinghai.config.datasource.DataSourceFlag;
import com.aps.yinghai.enums.DataSourceFlagEnum;
import com.aps.yinghai.iGTOS.BizShipCycleExtend;
import com.aps.yinghai.mapper.igtos.BizShipCycleExtendMapper;
import com.aps.yinghai.service.igtos.IBizShipCycleExtendService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BizBizShipCycleExtendServiceImpl extends ServiceImpl<BizShipCycleExtendMapper, BizShipCycleExtend> implements IBizShipCycleExtendService {



    @DataSourceFlag(DataSourceFlagEnum.IGTOS)
    @Override
    public List<BizShipCycleExtend> listByShipCycleIdList(List<String> shipCycleIdList) {
        LambdaQueryWrapper<BizShipCycleExtend> queryWrapper = Wrappers.lambdaQuery(BizShipCycleExtend.class).in(BizShipCycleExtend::getShipcycleId,shipCycleIdList );
        return this.list(queryWrapper);
    }
}
