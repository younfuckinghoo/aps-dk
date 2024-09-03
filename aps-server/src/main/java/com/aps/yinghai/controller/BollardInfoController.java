package com.aps.yinghai.controller;

import com.aps.yinghai.base.Result;
import com.aps.yinghai.entity.BollardInfo;
import com.aps.yinghai.entity.ShipForecast;
import com.aps.yinghai.service.IBerthInfoService;
import com.aps.yinghai.service.IBollardInfoService;
import com.aps.yinghai.vo.BollardInfoPageVO;
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
 * 缆柱信息 前端控制器
 * </p>
 *
 * @author haoyong
 * @since 2024-09-02
 */
@RestController
@Tag(name = "缆柱信息")
@RequestMapping("/bollardInfo")
public class BollardInfoController {

    private final IBollardInfoService bollardInfoService;

    public BollardInfoController(IBollardInfoService bollardInfoService) {
        this.bollardInfoService = bollardInfoService;
    }

    @Operation(summary = "获取数据列表")
    @PostMapping("list")
    @ApiResponse(responseCode = "200", description = "成功",
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = BollardInfo.class))})
    public Result<List> list(@Parameter(name = "船舶预报信息") @RequestBody BollardInfoPageVO bollardInfoPageVO){
        BollardInfo condition = bollardInfoPageVO.getCondition();
        LambdaQueryWrapper<BollardInfo> lambdaQueryWrapper = Wrappers.lambdaQuery(condition);
        lambdaQueryWrapper.like(StringUtils.isNotBlank(condition.getBollardNo()),BollardInfo::getBollardNo,condition.getBollardNo());
        List<BollardInfo> list = bollardInfoService.list(bollardInfoPageVO,lambdaQueryWrapper);
        System.out.println("缆柱分页列表"+list);
        return Result.ok(list, List.class);
    }

    @Operation(summary = "保存数据")
    @PostMapping("save")
    @ApiResponse(responseCode = "200", description = "成功",
            content = {@Content(mediaType = "application/json")})
    public Result<BollardInfo> save(@Parameter(name = "船舶预报信息") @RequestBody BollardInfo bollardInfo){
        this.bollardInfoService.saveOrUpdate(bollardInfo);
        return Result.ok();
    }
}
