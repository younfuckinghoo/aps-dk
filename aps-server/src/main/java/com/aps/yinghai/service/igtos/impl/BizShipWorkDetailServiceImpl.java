package com.aps.yinghai.service.igtos.impl;

import com.aps.yinghai.iGTOS.BizShipPlanWorkDetail;
import com.aps.yinghai.iGTOS.BizShipWorkSequence;
import com.aps.yinghai.mapper.igtos.BizShipPlanWorkDetailMapper;
import com.aps.yinghai.service.igtos.IBizShipWorkDetailService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BizShipWorkDetailServiceImpl extends ServiceImpl<BizShipPlanWorkDetailMapper, BizShipPlanWorkDetail> implements IBizShipWorkDetailService {


    @Override
    public List<BizShipPlanWorkDetail> listByPrePlanIdList(List<String> prePlanIdList) {
        LambdaQueryWrapper<BizShipPlanWorkDetail> queryWrapper = Wrappers.lambdaQuery(BizShipPlanWorkDetail.class).in(BizShipPlanWorkDetail::getShipPrePlanId, prePlanIdList);
        return this.list(queryWrapper);
    }
}
