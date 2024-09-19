package com.aps.yinghai.mapper;

import com.aps.yinghai.entity.BollardInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.time.LocalDateTime;

/**
 * <p>
 * 缆柱信息 Mapper 接口
 * </p>
 *
 * @author haoyong
 * @since 2024-09-02
 */
public interface BollardInfoMapper extends BaseMapper<BollardInfo> {

    LocalDateTime getMaxCreateTime();
}
