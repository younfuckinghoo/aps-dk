package com.aps.yinghai.controller;

import com.aps.yinghai.base.Result;
import com.aps.yinghai.entity.CabinInfo;
import com.aps.yinghai.entity.ShipForecast;
import com.aps.yinghai.service.ICabinInfoService;
import com.aps.yinghai.vo.CabinInfoPageVO;
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

/**
 * <p>
 * 舱口信息 前端控制器
 * </p>
 *
 * @author haoyong
 * @since 2024-09-03
 */
@Tag(name = "舱口信息")
@RestController
@RequestMapping("/cabinInfo")
public class CabinInfoController {

    private final ICabinInfoService cabinInfoService;

    public CabinInfoController(ICabinInfoService cabinInfoService) {
        this.cabinInfoService = cabinInfoService;
    }

    @Operation(summary = "获取数据列表")
    @PostMapping("list")
    @ApiResponse(responseCode = "200", description = "成功",
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = CabinInfo.class))})
    public Result<List> list(@Parameter(name = "船舶预报信息") @RequestBody CabinInfoPageVO cabinInfoPageVO){
        CabinInfo condition = cabinInfoPageVO.getCondition();
        LambdaQueryWrapper<CabinInfo> lambdaQueryWrapper = Wrappers.lambdaQuery(condition);
        lambdaQueryWrapper.eq(condition.getShipId()!=null,CabinInfo::getShipId,condition.getShipId());
        List<CabinInfo> list = cabinInfoService.list(cabinInfoPageVO,lambdaQueryWrapper);
        System.out.println("舱口分页列表"+list);
        return Result.ok(list, List.class);
    }

    @Operation(summary = "保存数据")
    @PostMapping("save")
    @ApiResponse(responseCode = "200", description = "成功",
            content = {@Content(mediaType = "application/json")})
    public Result save(@Parameter(name = "舱口信息") @RequestBody CabinInfo cabinInfo){
        this.cabinInfoService.saveOrUpdate(cabinInfo);
        return Result.ok();
    }
}
