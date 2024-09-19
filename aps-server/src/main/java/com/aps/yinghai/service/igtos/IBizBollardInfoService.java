package com.aps.yinghai.service.igtos;

import com.aps.yinghai.iGTOS.BizBerthInfo;
import com.aps.yinghai.iGTOS.BizBollardInfo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.time.LocalDateTime;
import java.util.List;

public interface IBizBollardInfoService extends IService<BizBollardInfo> {

    List<BizBollardInfo> listBollardAfterTime(LocalDateTime createTime);
}
