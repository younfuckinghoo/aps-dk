package com.aps.yinghai.service.igtos;

import com.aps.yinghai.iGTOS.BizShipPlanWorkDetail;
import com.aps.yinghai.iGTOS.BizShipPrePlan;
import com.baomidou.mybatisplus.extension.service.IService;

import java.time.LocalDateTime;
import java.util.List;

public interface IBizShipWorkDetailService extends IService<BizShipPlanWorkDetail> {

    List<BizShipPlanWorkDetail> listByPrePlanIdList(List<String> prePlanIdList);
}
