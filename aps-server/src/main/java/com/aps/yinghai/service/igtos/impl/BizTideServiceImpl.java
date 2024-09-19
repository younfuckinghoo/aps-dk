package com.aps.yinghai.service.igtos.impl;

import com.aps.yinghai.config.datasource.DataSourceFlag;
import com.aps.yinghai.enums.DataSourceFlagEnum;
import com.aps.yinghai.iGTOS.BizBerthInfo;
import com.aps.yinghai.iGTOS.BizTide;
import com.aps.yinghai.mapper.igtos.BizBerthInfoMapper;
import com.aps.yinghai.mapper.igtos.BizTideMapper;
import com.aps.yinghai.service.igtos.IBizBerthInfoService;
import com.aps.yinghai.service.igtos.IBizTideService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BizTideServiceImpl extends ServiceImpl<BizTideMapper, BizTide> implements IBizTideService {

    /**
     * 查询在指定时间之后
     * @param createTime
     * @return
     */

    @DataSourceFlag(DataSourceFlagEnum.IGTOS)
    @Override
    public List<BizTide> listTideAfterTime(LocalDateTime createTime) {
        LambdaQueryWrapper<BizTide> queryWrapper = Wrappers.lambdaQuery(BizTide.class).gt(BizTide::getDate, createTime);
        return this.list(queryWrapper);
    }


}
