package com.aps.yinghai.service.igtos;

import com.aps.yinghai.iGTOS.BizBerthInfo;
import com.aps.yinghai.iGTOS.BizShipPrePlan;
import com.baomidou.mybatisplus.extension.service.IService;

import java.time.LocalDateTime;
import java.util.List;

public interface IBizBerthInfoService extends IService<BizBerthInfo> {

    List<BizBerthInfo> listBerthAfterTime(LocalDateTime createTime);
}
