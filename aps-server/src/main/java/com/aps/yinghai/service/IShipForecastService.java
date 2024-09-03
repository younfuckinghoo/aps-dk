package com.aps.yinghai.service;

import com.aps.yinghai.entity.ShipForecast;
import com.baomidou.mybatisplus.extension.service.IService;

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
     */
    List<ShipForecast> listNotPlanningShip();
}
