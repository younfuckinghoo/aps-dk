package com.aps.yinghai.controller;


import com.aps.yinghai.base.Result;
import com.aps.yinghai.entity.ShipForecast;
import com.aps.yinghai.service.IShipForecastService;
import com.aps.yinghai.vo.ShipForecastPageVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
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

@RequestMapping("shipForecast")
@Tag(name = "船舶预报")
@RestController
public class ShipForecastController {

    private final IShipForecastService shipForecastService;

    public ShipForecastController(IShipForecastService shipForecastService) {
        this.shipForecastService = shipForecastService;
    }


   /* @Operation(summary = "同步数据")
    @PostMapping("set")
    @ApiResponse(responseCode = "200", description = "成功",
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = Ship.class))})
    public Result<Ship> set(@Parameter(name = "船舶预报信息") @RequestBody Ship ship){
        ship.setShipName("first immortal");
        return Result.ok(ship, Ship.class);
    }*/


    @Operation(summary = "获取数据列表")
    @PostMapping("list")
    @ApiResponse(responseCode = "200", description = "成功",
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ShipForecast.class))})
    public Result< List> list(@Parameter(name = "船舶预报信息") @RequestBody ShipForecastPageVO shipForecastPageVO){
        ShipForecast condition = shipForecastPageVO.getCondition();
        LambdaQueryWrapper<ShipForecast> lambdaQueryWrapper = Wrappers.lambdaQuery(condition);
        lambdaQueryWrapper.like(StringUtils.isNotBlank(condition.getShipNo()),ShipForecast::getShipNo,condition.getShipNo());
        List<ShipForecast> list = shipForecastService.list(shipForecastPageVO,lambdaQueryWrapper);
        System.out.println("船舶分页列表"+list);
        return Result.ok(list, List.class);
    }

    @Operation(summary = "保存数据")
    @PostMapping("save")
    @ApiResponse(responseCode = "200", description = "成功",
            content = {@Content(mediaType = "application/json")})
    public Result save(@Parameter(name = "船舶预报信息") @RequestBody ShipForecast ship){
        this.shipForecastService.saveOrUpdate(ship);
        return Result.ok();
    }


}
