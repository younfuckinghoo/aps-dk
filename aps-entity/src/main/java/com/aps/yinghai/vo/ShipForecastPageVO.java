package com.aps.yinghai.vo;

import com.aps.yinghai.entity.ShipForecast;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(name = "船舶预报分页")
@Data
public class ShipForecastPageVO extends Page<ShipForecast> {

    @Schema(name = "船舶预报分页条件")
    private ShipForecast condition;
}
