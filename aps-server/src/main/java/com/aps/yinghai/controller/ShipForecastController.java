package com.aps.yinghai.controller;


import com.aps.yinghai.base.Result;
import com.aps.yinghai.entity.ShipForecast;
import com.aps.yinghai.service.IShipForecastService;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
    public Result< List> list(@Parameter(name = "船舶预报信息") @RequestBody ShipForecast ship){
        PageDTO<ShipForecast> pageDTO = new PageDTO(1,10);
        List<ShipForecast> list = shipForecastService.list(pageDTO);
        System.out.println("船舶分页列表"+list);
        return Result.ok(list, List.class);
    }


}
