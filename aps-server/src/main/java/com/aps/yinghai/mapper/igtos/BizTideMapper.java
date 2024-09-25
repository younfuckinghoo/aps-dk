package com.aps.yinghai.mapper.igtos;

import com.aps.yinghai.iGTOS.BizCargoInfo;
import com.aps.yinghai.iGTOS.BizTide;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

public interface BizTideMapper extends BaseMapper<BizTide> {

    List<BizTide> listAfterDate(@Param("tideDate") LocalDate tideDate);

}
