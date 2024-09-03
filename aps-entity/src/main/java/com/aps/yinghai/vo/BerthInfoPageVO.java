package com.aps.yinghai.vo;

import com.aps.yinghai.entity.BerthInfo;
import com.aps.yinghai.entity.ShipForecast;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(name = "泊位分页")
@Data
public class BerthInfoPageVO extends Page<BerthInfo> {

    @Schema(name = "缆柱分页条件")
    private BerthInfo condition;
}
