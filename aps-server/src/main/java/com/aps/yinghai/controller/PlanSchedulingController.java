package com.aps.yinghai.controller;


import com.aps.yinghai.base.Result;
import com.aps.yinghai.entity.ShipForecast;
import com.aps.yinghai.vo.ShipForecastPageVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Tag(name = "船舶计划")
@RequestMapping("/plan")
public class PlanSchedulingController {

    @Operation(summary = "开始排产")
    @PostMapping("scheduling")
    @ApiResponse(responseCode = "200", description = "成功",
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ShipForecast.class))})
    public Result<List> scheduling(){

        return Result.ok();
    }


}
