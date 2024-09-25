package com.aps.yinghai.service;

import com.aps.yinghai.entity.ShipWorkingInfoDetail;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 船舶作业信息详细信息 服务类
 * </p>
 *
 * @author haoyong
 * @since 2024-09-13
 */
public interface IShipWorkingInfoDetailService extends IService<ShipWorkingInfoDetail> {

    /**
     * 通过主表查询子表
     * @param shipForecastIdList
     * @return
     */
    List<ShipWorkingInfoDetail> listByShipForecastIdList(List<String> shipForecastIdList);
}
