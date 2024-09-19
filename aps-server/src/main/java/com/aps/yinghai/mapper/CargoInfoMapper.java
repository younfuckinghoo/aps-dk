package com.aps.yinghai.mapper;

import com.aps.yinghai.entity.CargoInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.time.LocalDateTime;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author haoyong
 * @since 2024-09-13
 */
public interface CargoInfoMapper extends BaseMapper<CargoInfo> {

    LocalDateTime getMaxCreateTime();
}
