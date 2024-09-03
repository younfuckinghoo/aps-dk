package com.aps.yinghai.controller;

import com.aps.yinghai.base.Result;
import com.aps.yinghai.entity.BerthInfo;
import com.aps.yinghai.entity.ShipForecast;
import com.aps.yinghai.service.IBerthInfoService;
import com.aps.yinghai.vo.BerthInfoPageVO;
import com.aps.yinghai.vo.ShipForecastPageVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
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

/**
 * <p>
 * 泊位信息 前端控制器
 * </p>
 *
 * @author haoyong
 * @since 2024-09-02
 */
@RestController
@RequestMapping("/berthInfo")
@Tag(name = "泊位信息")
public class BerthInfoController {

    private final IBerthInfoService berthInfoService;

    public BerthInfoController(IBerthInfoService berthInfoService) {
        this.berthInfoService = berthInfoService;
    }

    @Operation(summary = "获取数据列表")
    @PostMapping("list")
    @ApiResponse(responseCode = "200", description = "成功",
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = BerthInfo.class))})
    public Result< List> list(@Parameter(name = "泊位分页数据") @RequestBody BerthInfoPageVO berthInfoPageVO){
        BerthInfo condition = berthInfoPageVO.getCondition();
        LambdaQueryWrapper<BerthInfo> lambdaQueryWrapper = Wrappers.lambdaQuery(condition);
        lambdaQueryWrapper.like(StringUtils.isNotBlank(condition.getBerthNo()),BerthInfo::getBerthNo,condition.getBerthNo());
        List<BerthInfo> list = berthInfoService.list(berthInfoPageVO,lambdaQueryWrapper);
        System.out.println("泊位分页列表"+list);
        return Result.ok(list, List.class);
    }

    @Operation(summary = "保存数据")
    @PostMapping("save")
    @ApiResponse(responseCode = "200", description = "成功",
            content = {@Content(mediaType = "application/json")})
    public Result save(@Parameter(name = "泊位信息") @RequestBody BerthInfo berthInfo){
        this.berthInfoService.saveOrUpdate(berthInfo);
        return Result.ok();
    }


}
