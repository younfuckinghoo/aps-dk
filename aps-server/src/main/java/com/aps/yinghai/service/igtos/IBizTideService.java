package com.aps.yinghai.service.igtos;

import com.aps.yinghai.iGTOS.BizBerthInfo;
import com.aps.yinghai.iGTOS.BizShipCycle;
import com.aps.yinghai.iGTOS.BizTide;
import com.baomidou.mybatisplus.extension.service.IService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface IBizTideService extends IService<BizTide> {

    List<BizTide> listTideAfterTime(LocalDate createTime);
}
