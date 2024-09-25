package com.aps.yinghai.service.impl;

import com.aps.yinghai.entity.ShipWorkingInfoDetail;
import com.aps.yinghai.entity.ShipWorkingSequence;
import com.aps.yinghai.mapper.ShipWorkingInfoDetailMapper;
import com.aps.yinghai.service.IShipWorkingInfoDetailService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 船舶作业信息详细信息 服务实现类
 * </p>
 *
 * @author haoyong
 * @since 2024-09-13
 */
@Service
public class ShipWorkingInfoDetailServiceImpl extends ServiceImpl<ShipWorkingInfoDetailMapper, ShipWorkingInfoDetail> implements IShipWorkingInfoDetailService {

    @Override
    public List<ShipWorkingInfoDetail> listByShipForecastIdList(List<String> shipForecastIdList) {
        LambdaQueryWrapper<ShipWorkingInfoDetail> queryWrapper = Wrappers.lambdaQuery(ShipWorkingInfoDetail.class).in(ShipWorkingInfoDetail::getShipForecastId, shipForecastIdList);
        return this.list(queryWrapper);
    }
}
