package com.aps.yinghai.controller.igtos;


import com.aps.yinghai.base.Result;
import com.aps.yinghai.iGTOS.ShipCycle;
import com.aps.yinghai.service.igtos.IShipCycleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("shipCycle")
@RestController
@Tag(name = "船期")
public class ShipCycleController {


    private final IShipCycleService iShipCycleService;

    public ShipCycleController(IShipCycleService iShipCycleService) {
        this.iShipCycleService = iShipCycleService;
    }

    @Operation(summary = "获取数据列表")
    @PostMapping("list")
    @ApiResponse(responseCode = "200", description = "成功",
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ShipCycle.class))})
    public Result<List> list() {
        List<ShipCycle> list = iShipCycleService.list();
        return Result.ok(list, List.class);
    }

}
