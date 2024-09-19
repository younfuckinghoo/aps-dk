package com.aps.yinghai.service.igtos;

import com.aps.yinghai.iGTOS.BizCargoInfo;
import com.aps.yinghai.iGTOS.BizShipCycle;
import com.baomidou.mybatisplus.extension.service.IService;

import java.time.LocalDateTime;
import java.util.List;

public interface IBizCargoInfoService extends IService<BizCargoInfo> {


    List<BizCargoInfo> listCargoAfterTime(LocalDateTime localDateTime);
}
