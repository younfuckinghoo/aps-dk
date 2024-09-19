package com.aps.yinghai.service.igtos.impl;

import com.aps.yinghai.iGTOS.BizShipWorkSequence;
import com.aps.yinghai.mapper.igtos.BizShipWorkSequenceMapper;
import com.aps.yinghai.service.igtos.IBizShipWorkSequenceService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BizShipWorkSequenceServiceImpl extends ServiceImpl<BizShipWorkSequenceMapper, BizShipWorkSequence> implements IBizShipWorkSequenceService {


    @Override
    public List<BizShipWorkSequence> listByPrePlanIdList(List<String> prePlanIdList) {
        LambdaQueryWrapper<BizShipWorkSequence> queryWrapper = Wrappers.lambdaQuery(BizShipWorkSequence.class).in(BizShipWorkSequence::getShipPrePlanId, prePlanIdList);
        return this.list(queryWrapper);
    }
}
