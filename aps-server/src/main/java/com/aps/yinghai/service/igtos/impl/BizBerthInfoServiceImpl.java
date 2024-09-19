package com.aps.yinghai.service.igtos.impl;

import com.aps.yinghai.config.datasource.DataSourceFlag;
import com.aps.yinghai.constant.IGTOSConstant;
import com.aps.yinghai.enums.DataSourceFlagEnum;
import com.aps.yinghai.iGTOS.BizBerthInfo;
import com.aps.yinghai.iGTOS.BizShipPrePlan;
import com.aps.yinghai.mapper.igtos.BizBerthInfoMapper;
import com.aps.yinghai.mapper.igtos.BizShipPrePlanMapper;
import com.aps.yinghai.service.igtos.IBizBerthInfoService;
import com.aps.yinghai.service.igtos.IBizShipPrePlanService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BizBerthInfoServiceImpl extends ServiceImpl<BizBerthInfoMapper, BizBerthInfo> implements IBizBerthInfoService {

    /**
     * 查询在指定时间之后
     * @param createTime
     * @return
     */

    @DataSourceFlag(DataSourceFlagEnum.IGTOS)
    @Override
    public List<BizBerthInfo> listBerthAfterTime(LocalDateTime createTime) {
        LambdaQueryWrapper<BizBerthInfo> queryWrapper = Wrappers.lambdaQuery(BizBerthInfo.class).gt(BizBerthInfo::getCreateDate, createTime);
        return this.list(queryWrapper);
    }


}
