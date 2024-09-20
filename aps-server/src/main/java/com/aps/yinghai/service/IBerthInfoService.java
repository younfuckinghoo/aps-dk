package com.aps.yinghai.service;

import com.aps.yinghai.entity.BerthInfo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 泊位信息 服务类
 * </p>
 *
 * @author haoyong
 * @since 2024-09-02
 */
public interface IBerthInfoService extends IService<BerthInfo> {

    /**
     * 查询原始的泊位 不带S N M
     * @return
     */
    List<BerthInfo> listOriginalBerth();
}
