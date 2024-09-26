package com.aps.yinghai.controller;


import com.aps.yinghai.base.Result;
import com.aps.yinghai.dto.PlanningShipDTO;
import com.aps.yinghai.service.IPlanSchedulingService;
import com.aps.yinghai.task.ShipForecastTask;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Tag(name = "船舶计划")
@RequestMapping("/plan")
public class PlanSchedulingController {

    private final IPlanSchedulingService planSchedulingService;
    private final ShipForecastTask shipForecastTask;

    public PlanSchedulingController(IPlanSchedulingService planSchedulingService, ShipForecastTask shipForecastTask) {
        this.planSchedulingService = planSchedulingService;
        this.shipForecastTask = shipForecastTask;
    }

    @Operation(summary = "长期计划排产")
    @PostMapping("scheduling")
    @ApiResponse(responseCode = "200", description = "成功",
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = PlanningShipDTO.class))})
    public Result<List> scheduling(@Parameter(name = "排手续不全的") @RequestParam(name = "absentProcedure",required = false) Integer absentProcedure){
        return Result.ok(planSchedulingService.longTermScheduling(absentProcedure), List.class);
    }


    @Operation(summary = "昼夜计划排产")
    @PostMapping("dayNight")
    @ApiResponse(responseCode = "200", description = "成功",
            content = {@Content(mediaType = "application/json")})
    public Result<List> schedulingDayNight(){
        return Result.ok(planSchedulingService.dayNightScheduling(), List.class);
    }


    @Operation(summary = "拉数据")
    @PostMapping("pullFoecast")
    @ApiResponse(responseCode = "200", description = "成功",
            content = {@Content(mediaType = "application/json")})
    public Result pull(){
        shipForecastTask.pullShipPrePlan();
        return Result.ok();
    }



}
