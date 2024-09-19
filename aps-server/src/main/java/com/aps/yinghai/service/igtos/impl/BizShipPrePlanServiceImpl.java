package com.aps.yinghai.service.igtos.impl;

import com.aps.yinghai.config.datasource.DataSourceFlag;
import com.aps.yinghai.constant.IGTOSConstant;
import com.aps.yinghai.enums.DataSourceFlagEnum;
import com.aps.yinghai.iGTOS.BizShipPrePlan;
import com.aps.yinghai.mapper.igtos.BizShipPrePlanMapper;
import com.aps.yinghai.service.igtos.IBizShipPrePlanService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BizShipPrePlanServiceImpl extends ServiceImpl<BizShipPrePlanMapper, BizShipPrePlan> implements IBizShipPrePlanService {

    /**
     * 查询在指定时间之后并且状态是预排的数据
     * @param createTime
     * @return
     */

    @DataSourceFlag(DataSourceFlagEnum.IGTOS)
    @Override
    public List<BizShipPrePlan> listShipPrePlanAfterTime(LocalDateTime createTime) {

        LambdaQueryWrapper<BizShipPrePlan> queryWrapper = Wrappers.lambdaQuery(BizShipPrePlan.class).gt(BizShipPrePlan::getCreateDate, createTime)
                .eq(BizShipPrePlan::getShipStatus, IGTOSConstant.ShipStatusConst.TO_BE_PRE_PLAN);
        return this.list(queryWrapper);
    }
}
