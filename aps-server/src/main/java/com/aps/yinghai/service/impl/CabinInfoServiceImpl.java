package com.aps.yinghai.service.impl;

import com.aps.yinghai.entity.CabinInfo;
import com.aps.yinghai.mapper.CabinInfoMapper;
import com.aps.yinghai.service.ICabinInfoService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 舱口信息 服务实现类
 * </p>
 *
 * @author haoyong
 * @since 2024-09-03
 */
@Service
public class CabinInfoServiceImpl extends ServiceImpl<CabinInfoMapper, CabinInfo> implements ICabinInfoService {

    @Override
    public List<CabinInfo> listCabinByShipIdList(List<String> shipIdList) {
        LambdaQueryWrapper<CabinInfo> queryWrapper = Wrappers.lambdaQuery(CabinInfo.class).in(CabinInfo::getShipId, shipIdList);
        return this.list(queryWrapper);
    }
}
