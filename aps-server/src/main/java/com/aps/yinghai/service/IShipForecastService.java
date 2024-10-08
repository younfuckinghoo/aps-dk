package com.aps.yinghai.service;

import com.aps.yinghai.entity.ShipForecast;
import com.baomidou.mybatisplus.extension.service.IService;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author haoyong
 * @since 2024-09-02
 */
public interface IShipForecastService extends IService<ShipForecast> {

    /**
     * 查找未排产的船
     * @return
     * @param includeAbsentProcedure
     */
    List<ShipForecast> listNotPlanningShip(Integer includeAbsentProcedure);

    /**
     * 获取下一昼夜的预计划
     * @param startTime
     * @param secondEndTime
     * @return
     */
    List<ShipForecast> listShipForecastByTimeRange(LocalDateTime startTime, LocalDateTime secondEndTime);
}
