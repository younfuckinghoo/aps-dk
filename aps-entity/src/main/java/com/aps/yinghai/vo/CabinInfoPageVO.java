package com.aps.yinghai.vo;

import com.aps.yinghai.entity.CabinInfo;
import com.aps.yinghai.entity.ShipForecast;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(name = "舱口分页")
@Data
public class CabinInfoPageVO extends Page<CabinInfo> {

    @Schema(name = "舱口分页条件")
    private CabinInfo condition;
}
