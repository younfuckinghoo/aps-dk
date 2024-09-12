package com.aps.yinghai.service.igtos;

import com.aps.yinghai.iGTOS.ShipCycle;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface IShipCycleService extends IService<ShipCycle> {

    List<ShipCycle> list();
}
