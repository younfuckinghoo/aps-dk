package com.aps.yinghai.service.igtos;

import com.aps.yinghai.iGTOS.BizShipCycle;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface IBizShipCycleService extends IService<BizShipCycle> {

    List<BizShipCycle> list();
    List<BizShipCycle> listByShipNoList(List<String> shipNoList);
}
