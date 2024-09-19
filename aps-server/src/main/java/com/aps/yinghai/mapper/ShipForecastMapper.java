package com.aps.yinghai.mapper;

import com.aps.yinghai.entity.ShipForecast;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.time.LocalDateTime;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author haoyong
 * @since 2024-09-02
 */
public interface ShipForecastMapper extends BaseMapper<ShipForecast> {


    LocalDateTime checkMaxCreateTime();

}
