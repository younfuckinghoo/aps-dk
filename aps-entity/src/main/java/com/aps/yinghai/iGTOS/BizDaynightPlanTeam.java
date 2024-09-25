package com.aps.yinghai.iGTOS;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * <p>
 * 计划装卸班组
 * </p>
 *
 * @author xuemz
 * @since 2024-09-20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("BIZ_DAYNIGHT_PLAN_TEAM")
@Schema(name = "BizDaynightPlanTeam对象", description = "计划装卸班组")
public class BizDaynightPlanTeam extends BizBaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "ID", type = IdType.ASSIGN_UUID)
    private String id;

    @Schema(name = "计划班次ID")
    private String daynightClassId;

    @Schema(name = "班组人数")
    private Integer teamQty;

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

    @Schema(name = "部门代码")
    private String deptCode;

    @Schema(name = "部门名称")
    private String deptName;


}
