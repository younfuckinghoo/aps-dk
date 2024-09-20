package com.aps.yinghai.service.impl;

import com.aps.yinghai.constant.PlanConstant;
import com.aps.yinghai.entity.BerthInfo;
import com.aps.yinghai.mapper.BerthInfoMapper;
import com.aps.yinghai.service.IBerthInfoService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 泊位信息 服务实现类
 * </p>
 *
 * @author haoyong
 * @since 2024-09-02
 */
@Service
public class BerthInfoServiceImpl extends ServiceImpl<BerthInfoMapper, BerthInfo> implements IBerthInfoService {




    @Override
    public List<BerthInfo> listOriginalBerth() {
//.list(Wrappers.lambdaQuery(BerthInfo.class).eq(BerthInfo::getAvailable, PlanConstant.YES))
        return this.baseMapper.listOriginalBerth(PlanConstant.YES);
    }
}
