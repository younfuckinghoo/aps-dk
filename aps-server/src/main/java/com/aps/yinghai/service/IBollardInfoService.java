package com.aps.yinghai.service;

import com.aps.yinghai.entity.BollardInfo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 缆柱信息 服务类
 * </p>
 *
 * @author haoyong
 * @since 2024-09-02
 */
public interface IBollardInfoService extends IService<BollardInfo> {

    List<BollardInfo> listBollardByBerthIdList(List<Integer> berthIdList);
}
