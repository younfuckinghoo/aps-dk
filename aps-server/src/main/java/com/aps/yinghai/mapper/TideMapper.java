package com.aps.yinghai.mapper;

import com.aps.yinghai.entity.Tide;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.time.LocalDateTime;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author haoyong
 * @since 2024-09-10
 */
public interface TideMapper extends BaseMapper<Tide> {

    LocalDateTime getMaxDate();

}
