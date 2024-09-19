package com.aps.yinghai.service.igtos;

import com.aps.yinghai.iGTOS.BizShipWorkSequence;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface IBizShipWorkSequenceService extends IService<BizShipWorkSequence> {
    List<BizShipWorkSequence> listByPrePlanIdList(List<String> prePlanIdList);
}
