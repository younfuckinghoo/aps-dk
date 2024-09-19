package com.aps.yinghai.iGTOS;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

@Data
@TableName("BIZ_SHIP_PRE_PLAN_WORK_RELATION")
public class BizShipPlanWorkDetail extends BizBaseEntity {

    /**
     * 父表id
     */
    @TableField("SHIP_PRE_PLAN_ID")
    private String shipPrePlanId;

    /**
     * 票序
     */
    @TableField("TICKET_NUM")
    private Integer ticketNum;

    /**
     * 估水时间
     */
    @TableField("WATER_ESTIMATION_TIME")
    private String waterEstimationTime;

    /**
     * 含水量
     */
    @TableField("WATER_CONTENT")
    private BigDecimal waterRatio;


}
