package com.aps.yinghai.service.igtos;

import com.aps.yinghai.iGTOS.BizShipCycle;
import com.aps.yinghai.iGTOS.BizShipPrePlan;
import com.baomidou.mybatisplus.extension.service.IService;

import java.time.LocalDateTime;
import java.util.List;

public interface IBizShipPrePlanService extends IService<BizShipPrePlan> {

    List<BizShipPrePlan> listShipPrePlanAfterTime(LocalDateTime createTime);
}
