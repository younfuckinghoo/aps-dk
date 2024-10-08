package com.aps.yinghai.service.impl;

import com.aps.yinghai.constant.IGTOSConstant;
import com.aps.yinghai.constant.PlanConstant;
import com.aps.yinghai.entity.ShipForecast;
import com.aps.yinghai.enums.ShipAlgStateEnum;
import com.aps.yinghai.mapper.ShipForecastMapper;
import com.aps.yinghai.service.IShipForecastService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
    public List<ShipForecast> listNotPlanningShip(Integer absentProcedure) {
        LambdaQueryWrapper<ShipForecast> lambdaQueryWrapper = Wrappers.lambdaQuery(ShipForecast.class);
        lambdaQueryWrapper
                .lt(ShipForecast::getAlgorithmState, ShipAlgStateEnum.WORKING.getCode())
                .eq(ShipForecast::getShipStatus, IGTOSConstant.ShipStatusConst.TO_BE_PRE_PLAN);
        // 控制如果缺手续不能排
        if (absentProcedure == PlanConstant.NO){
            lambdaQueryWrapper.eq(ShipForecast::getShipProcedure,PlanConstant.YES)
                    .eq(ShipForecast::getCargoProcedure,PlanConstant.YES);
        }
        lambdaQueryWrapper.orderByAsc(ShipForecast::getExpectArriveTime);
        return this.list(lambdaQueryWrapper);
    }

    @Override
    public List<ShipForecast> listShipForecastByTimeRange(LocalDateTime startTime, LocalDateTime endTime) {

        LambdaQueryWrapper<ShipForecast> lambdaQueryWrapper = Wrappers.lambdaQuery(ShipForecast.class);
        lambdaQueryWrapper.gt(ShipForecast::getStartTime,startTime).lt(ShipForecast::getStartTime,endTime);
        return this.list(lambdaQueryWrapper);
    }
}
