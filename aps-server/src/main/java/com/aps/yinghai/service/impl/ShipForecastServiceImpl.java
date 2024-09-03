package com.aps.yinghai.service.impl;

import com.aps.yinghai.entity.ShipForecast;
import com.aps.yinghai.enums.ShipStatusEnum;
import com.aps.yinghai.mapper.ShipForecastMapper;
import com.aps.yinghai.service.IShipForecastService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author haoyong
 * @since 2024-09-02
 */
@Service
public class ShipForecastServiceImpl extends ServiceImpl<ShipForecastMapper, ShipForecast> implements IShipForecastService {

    @Override
    public List<ShipForecast> listNotPlanningShip() {
        LambdaQueryWrapper<ShipForecast> lambdaQueryWrapper = Wrappers.lambdaQuery(ShipForecast.class);
        lambdaQueryWrapper.lt(ShipForecast::getShipStatus, ShipStatusEnum.WORKING);
        lambdaQueryWrapper.orderByAsc(ShipForecast::getExceptArriveTime);
        return this.list(lambdaQueryWrapper);
    }
}
