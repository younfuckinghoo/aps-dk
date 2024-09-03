package com.aps.yinghai.vo;

import com.aps.yinghai.entity.BollardInfo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(name = "缆柱分页")
@Data
public class BollardInfoPageVO extends Page<BollardInfo> {

    @Schema(name = "缆柱分页条件")
    private BollardInfo condition;
}
