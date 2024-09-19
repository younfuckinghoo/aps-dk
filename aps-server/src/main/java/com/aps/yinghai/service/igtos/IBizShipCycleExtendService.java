package com.aps.yinghai.service.igtos;

import com.aps.yinghai.iGTOS.BizShipCycle;
import com.aps.yinghai.iGTOS.BizShipCycleExtend;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface IBizShipCycleExtendService extends IService<BizShipCycleExtend> {

    /**
     * 根据id获取扩展表
     * @param shipCycleIdList
     * @return
     */
    List<BizShipCycleExtend> listByShipCycleIdList(List<String> shipCycleIdList);
}
