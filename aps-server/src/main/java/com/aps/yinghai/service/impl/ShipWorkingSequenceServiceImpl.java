package com.aps.yinghai.service.impl;

import com.aps.yinghai.entity.ShipWorkingSequence;
import com.aps.yinghai.mapper.ShipWorkingSequenceMapper;
import com.aps.yinghai.service.IShipWorkingSequenceService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 船舶舱口卸序 服务实现类
 * </p>
 *
 * @author haoyong
 * @since 2024-09-13
 */
@Service
public class ShipWorkingSequenceServiceImpl extends ServiceImpl<ShipWorkingSequenceMapper, ShipWorkingSequence> implements IShipWorkingSequenceService {


    @Override
    public List<ShipWorkingSequence> listByShipForecastIdList(List<String> shipForecastIdList) {
        LambdaQueryWrapper<ShipWorkingSequence> queryWrapper = Wrappers.lambdaQuery(ShipWorkingSequence.class).in(ShipWorkingSequence::getShipForecastId, shipForecastIdList);
        return this.list(queryWrapper);
    }
}
