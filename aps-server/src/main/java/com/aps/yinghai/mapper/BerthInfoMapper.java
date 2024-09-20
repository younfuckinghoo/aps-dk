package com.aps.yinghai.mapper;

import com.aps.yinghai.entity.BerthInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

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

    List<BerthInfo> listOriginalBerth(@Param("available") Integer available);
}
