package com.aps.yinghai.service;

import com.aps.yinghai.entity.ShipWorkingSequence;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 船舶舱口卸序 服务类
 * </p>
 *
 * @author haoyong
 * @since 2024-09-13
 */
public interface IShipWorkingSequenceService extends IService<ShipWorkingSequence> {
    List<ShipWorkingSequence> listByShipForecastIdList(List<String> shipForeCastIdList);
}
