package com.aps.yinghai.mapper;

import com.aps.yinghai.entity.BerthInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.time.LocalDateTime;

/**
 * <p>
 * 泊位信息 Mapper 接口
 * </p>
 *
 * @author haoyong
 * @since 2024-09-02
 */
public interface BerthInfoMapper extends BaseMapper<BerthInfo> {

    LocalDateTime getMaxCreateTime();
}
