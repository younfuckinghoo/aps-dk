package com.aps.yinghai.iGTOS;

import com.aps.yinghai.enums.WorkTypeEnum;
import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("BIZ_SHIP_WORK_PLAN")
public class BizShipWorkPlan extends BizBaseEntity {


    @TableId(value = "ID", type = IdType.ASSIGN_UUID)
    private String id;

    @TableField("SHIPCYCLE_ID")
    private String shipcycleId;

    /**
     * 中文名
     */
    @TableField("SHIP_NAME_CH")
    private String shipNameCh;

    /**
     * 装卸类型
     */
    @TableField("LOAD_UNLOAD")
    @Schema(name = "装卸类型")
    private Integer loadUnload;
    /**
     * 货种名称
     */
    @TableField("CARGO_NAME")
    private String cargoName;
    /**
     * 舱口数
     */
    @TableField("CABIN_QTY")
    private Integer cabinQty;

    /**
     * 总重（调度室手工录入）
     */
    @TableField("TOTAL_WEIGHT")
    private BigDecimal totalWeight;

    /**
     * 计划吨
     */
    @TableField("PLAN_WEIGHT")
    private BigDecimal planWeight;

    /**
     * 剩余吨
     */
    @TableField("LEFT_WEIGHT")
    private BigDecimal leftWeight;


    /**
     *靠泊时间
     */
    @TableField(value = "BERTH_DATE",updateStrategy= FieldStrategy.NOT_NULL)
    private LocalDateTime berthDate;

    /**
     *开工时间
     */
    @TableField(value = "START_WORK_DATE",updateStrategy= FieldStrategy.NOT_NULL)
    private LocalDateTime startWorkDate;

    /**
     *完工时间
     */
    @TableField(value = "PLAN_FINISH_DATE",updateStrategy= FieldStrategy.NOT_NULL)
    private LocalDateTime planFinishDate;
    /**
     *离港时间
     */
    @TableField(value = "PLAN_DEPART_DATE",updateStrategy= FieldStrategy.NOT_NULL)
    private LocalDateTime planDepartDate;

    /**
     * 船舶代理
     */
    @TableField("SHIPAGENT_NAME")
    private String shipagentName;
    /**
     * 前缆
     */
    @TableField("START_BOLLARD")
    private String startBollard;

    /**
     * 后缆
     */
    @TableField("END_BOLLARD")
    private String endBollard;
    /**
     * 作业线类型
     */
    @TableField("WORK_TYPE")
    private String workType = ""+WorkTypeEnum.SHIP.getCode();
    /**
     * 计划日期
     */
    @TableField("PLAN_DATE")
    private LocalDateTime planDate;

    /**
     * 首靠泊位号
     */
    @TableField("BERTH_NO")
    private String berthNo;





    @Schema(name = "创建人ID")
    private String creatorId;

    @Schema(name = "创建人")
    private String creator;
    @Schema(name = "是否删除")
    private Integer isDelete = 0;
    /**
     * 董矿：10
     */
    @Schema(name = "系统")
    private String sysCode = "10";

}
