package com.aps.yinghai.iGTOS;

import com.aps.yinghai.enums.WorkTypeEnum;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 昼夜作业计划班次信息
 * </p>
 *
 * @author xuemz
 * @since 2024-09-20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("BIZ_DAYNIGHT_CLASS_PLAN")
@Schema(name="BizDaynightClassPlan", description = "昼夜作业计划班次信息")
public class BizDaynightClassPlan extends BizBaseEntity {

    private static final long serialVersionUID = 1L;


    @TableId(value = "ID", type = IdType.ASSIGN_UUID)
    private String id;

    @Schema(name = "昼夜计划ID")
    private String daynightPlanId;

    /**
     * 1:船舶计划
     */
    @Schema(name = "作业类型")
    private Integer workType = WorkTypeEnum.SHIP.getCode();

    @Schema(name = "计划日期")
    private LocalDateTime planDate;

    @Schema(name = "班次")
    private String classnoName;

    @Schema(name = "班次区间")
    private String classPeriod;

    @Schema(name = "操作过程")
    private String operationProcedureId;

    @Schema(name = "货名ID")
    private String cargoNameId;

    @Schema(name = "货名")
    private String cargoName;

    @Schema(name = "货名代码")
    private String cargoNameCode;

    @Schema(name = "货物类别代码")
    private String cargoTypeCode;

    @Schema(name = "货物类别名称")
    private String cargoTypeName;

    @Schema(name = "作业线数")
    private Integer workLineQty;

    @Schema(name = "作业地点")
    private String workPlace;

    @Schema(name = "工时数")
    private BigDecimal workTime ;

    @Schema(name = "工时量")
    private BigDecimal workHourQty;

    @Schema(name = "计划重量")
    private BigDecimal planWeight;

    @Schema(name = "计划件数")
    private Long planQty;

    @Schema(name = "计划体积")
    private BigDecimal planVolume;

    @Schema(name = "计划车数")
    private Integer planCarQty;

    @Schema(name = "操作过程代码")
    private String operationCode;

    @Schema(name = "操作过程名称")
    private String operationName;

    @Schema(name = "总计划机械")
    private String totalPlanMachine;

    @Schema(name = "总计划货区")
    private String totalPlanCargoarea;

    @Schema(name = "总计划班组")
    private String totalPlanTeam;

    @Schema(name = "港区")
    private Integer portArea;

    @Schema(name = "创建人ID")
    private String creatorId;

    @Schema(name = "创建人")
    private String creator;

    @Schema(name = "创建时间")
    private LocalDateTime createDate;

    @Schema(name = "修改人ID")
    private String reviserId;

    @Schema(name = "修改人")
    private String reviser;

    @Schema(name = "修改时间")
    private LocalDateTime reviseDate;

    @Schema(name = "是否删除")
    private Integer isDelete = 0;

    @Schema(name = "删除人ID")
    private String deletorId;

    @Schema(name = "删除人")
    private String deletor;

    @Schema(name = "删除时间")
    private LocalDateTime deleteDate;

    @Schema(name = "备注")
    private String comments;

    @Schema(name = "系统编码大港1，董分11，董矿10，前港5，西联6")
    private String sysCode = "10";




}
