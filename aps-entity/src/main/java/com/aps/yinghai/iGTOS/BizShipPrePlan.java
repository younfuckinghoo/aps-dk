package com.aps.yinghai.iGTOS;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("BIZ_SHIP_PRE_PLAN")
public class BizShipPrePlan extends BizBaseEntity {

    @TableId("ID")
    private String id;

    /**
     * 算法状态
     */
    @TableField(value = "ALGORITHM_STATE",updateStrategy= FieldStrategy.NOT_NULL)
    private Integer algorithmState;

    /**
     * 抵港吃水
     */
    @TableField(value = "ARRIVE_DRAUGHT",updateStrategy= FieldStrategy.NOT_NULL)
    private BigDecimal arriveDraught;

    /**
     * 离港吃水
     */
    @TableField(value = "LEAVE_DRAUGHT",updateStrategy= FieldStrategy.NOT_NULL)
    private BigDecimal leaveDraught;

    /**
     * 预抵时间
     */
    @TableField(value = "EXPECT_ARRIVE_TIME",updateStrategy= FieldStrategy.NOT_NULL)
    private LocalDateTime expectArriveTime;


    /**
     * 装载量
     */
    @TableField(value = "LOAD_QTY",updateStrategy= FieldStrategy.NOT_NULL)
    @Schema(name = "装载量")
    private BigDecimal loadQty;

    /**
     * 等待常数排水时长（数字）
     */
    @TableField(value = "WAIT_CONSTANT_DRAINAGE_TIME",updateStrategy= FieldStrategy.NOT_NULL)
    private BigDecimal waitConstantDrainageTime;

    /**
     * 离泊水尺（装船、数字）
     */
    @TableField(value = "UNBERTHING_DRAFT",updateStrategy= FieldStrategy.NOT_NULL)
    private BigDecimal unberthingDraft;


    /**
     * 平均舱时量
     */
    @TableField(value = "AVG_SHIP_WORK_HOUR_QTY",updateStrategy= FieldStrategy.NOT_NULL)
    private BigDecimal capacityPerHour;

    /**
     * 是否特殊作业默认为否
     */
    @TableField(value = "IS_SPECIAL_WORK",updateStrategy= FieldStrategy.NOT_NULL)
    private Integer isSpecialWork;

    /**
     * 含水量
     */
    @TableField(value = "WATER_RATIO",updateStrategy= FieldStrategy.NOT_NULL)
    private BigDecimal waterRatio;

    /**
     * 装卸类型
     */
    @TableField(value = "LOAD_UNLOAD",updateStrategy= FieldStrategy.NOT_NULL)
    @Schema(name = "装卸类型")
    private Integer loadUnload;

    /**
     * 岸机类型
     */
    @TableField(value = "MACHINE_TYPE_CODE",updateStrategy= FieldStrategy.NOT_NULL)
    private String machineTypeCode;

    /**
     * 岸机数量
     */
    @TableField(value = "MACHINE_COUNT",updateStrategy= FieldStrategy.NOT_NULL)
    private Integer machineCount;

    /**
     * 中间水尺次数,云港通需要推送
     */
    @TableField(value = "MIDDLE_WATER_NUM",updateStrategy= FieldStrategy.NOT_NULL)
    private Integer middleWaterNum;


    /**
     * 船舶状态（7预排）
     */
    @TableField(value = "SHIP_STATUS",updateStrategy= FieldStrategy.NOT_NULL)
    private Integer shipStatus;

    /**
     *船号 能够判断是否引入船期
     */
    @TableField(value = "SHIP_NO",updateStrategy= FieldStrategy.NOT_NULL)
    private String shipNo;
    /**
     *船名
     */
    @TableField(value = "SHIP_NAME",updateStrategy= FieldStrategy.NOT_NULL)
    private String shipName;

    /**
     *泊位
     */
    @TableField(value = "BERTH_NO",updateStrategy= FieldStrategy.NOT_NULL)
    private String berthNo;

    /**
     *靠泊时间
     */
    @TableField(value = "EXPECT_BERTH_TIME",updateStrategy= FieldStrategy.NOT_NULL)
    private LocalDateTime expectBerthTime;

    /**
     *开工时间
     */
    @TableField(value = "START_TIME",updateStrategy= FieldStrategy.NOT_NULL)
    private LocalDateTime startTime;

    /**
     *完工时间
     */
    @TableField(value = "END_TIME",updateStrategy= FieldStrategy.NOT_NULL)
    private LocalDateTime endTime;
    /**
     *离港时间
     */
    @TableField(value = "EXPECT_LEAVE_TIME",updateStrategy= FieldStrategy.NOT_NULL)
    private LocalDateTime expectLeaveTime;

    /**
     *前缆
     */
    @TableField(value = "BOLLARD_FORWARD",updateStrategy= FieldStrategy.NOT_NULL)
    private String bollardForward;
    /**
     *后缆
     */
    @TableField(value = "BOLLARD_BEHIND",updateStrategy= FieldStrategy.NOT_NULL)
    private String bollardBehind;


}
