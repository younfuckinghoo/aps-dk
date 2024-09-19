package com.aps.yinghai.iGTOS;

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
    @TableField("ALGORITHM_STATE")
    private Integer algorithmState;

    /**
     * 抵港吃水
     */
    @TableField("ARRIVE_DRAUGHT")
    private BigDecimal arriveDraught;

    /**
     * 离港吃水
     */
    @TableField("LEAVE_DRAUGHT")
    private BigDecimal leaveDraught;

    /**
     * 预抵时间
     */
    @TableField("EXPECT_ARRIVE_TIME")
    private LocalDateTime expectArriveTime;


    /**
     * 装载量
     */
    @TableField("LOAD_QTY")
    @Schema(name = "装载量")
    private BigDecimal loadQty;

    /**
     * 等待常数排水时长（数字）
     */
    @TableField("WAIT_CONSTANT_DRAINAGE_TIME")
    private BigDecimal waitConstantDrainageTime;

    /**
     * 离泊水尺（装船、数字）
     */
    @TableField("UNBERTHING_DRAFT")
    private BigDecimal unberthingDraft;


    /**
     * 平均舱时量
     */
    @TableField("AVG_SHIP_WORK_HOUR_QTY")
    private BigDecimal capacityPerHour;

    /**
     * 是否特殊作业默认为否
     */
    @TableField("IS_SPECIAL_WORK")
    private Integer isSpecialWork;

    /**
     * 含水量
     */
    @TableField("WATER_RATIO")
    private BigDecimal waterRatio;

    /**
     * 装卸类型
     */
    @TableField("LOAD_UNLOAD")
    @Schema(name = "装卸类型")
    private Integer loadUnload;

    /**
     * 岸机类型
     */
    @TableField("MACHINE_TYPE_CODE")
    private String machineTypeCode;

    /**
     * 岸机数量
     */
    @TableField("MACHINE_COUNT")
    private Integer machineCount;

    /**
     * 中间水尺次数,云港通需要推送
     */
    @TableField("MIDDLE_WATER_NUM")
    private Integer middleWaterNum;


    /**
     * 船舶状态（7预排）
     */
    @TableField("SHIP_STATUS")
    private Integer shipStatus;

    /**
     *船号 能够判断是否引入船期
     */
    @TableField("SHIP_NO")
    private String shipNo;
    /**
     *船名
     */
    @TableField("SHIP_NAME")
    private String shipName;
}
