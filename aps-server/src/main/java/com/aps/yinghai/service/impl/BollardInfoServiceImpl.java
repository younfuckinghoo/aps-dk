package com.aps.yinghai.service.impl;

import com.aps.yinghai.entity.BollardInfo;
import com.aps.yinghai.mapper.BollardInfoMapper;
import com.aps.yinghai.service.IBollardInfoService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 缆柱信息 服务实现类
 * </p>
 *
 * @author haoyong
 * @since 2024-09-02
 */
@Service
public class BollardInfoServiceImpl extends ServiceImpl<BollardInfoMapper, BollardInfo> implements IBollardInfoService {

    @Override
    public List<BollardInfo> listBollardByBerthIdList(List<String> berthIdList) {
        LambdaQueryWrapper<BollardInfo> queryWrapper = Wrappers.lambdaQuery(BollardInfo.class).in(BollardInfo::getBerthId, berthIdList).orderByAsc(BollardInfo::getBollardNo);
        return this.list(queryWrapper);
    }
}
