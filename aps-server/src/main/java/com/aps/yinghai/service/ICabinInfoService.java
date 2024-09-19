package com.aps.yinghai.service;

import com.aps.yinghai.entity.CabinInfo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 舱口信息 服务类
 * </p>
 *
 * @author haoyong
 * @since 2024-09-03
 */
public interface ICabinInfoService extends IService<CabinInfo> {

    List<CabinInfo> listCabinByShipIdList(List<String> shipIdList);
}
