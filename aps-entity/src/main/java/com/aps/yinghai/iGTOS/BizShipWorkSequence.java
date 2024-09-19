package com.aps.yinghai.iGTOS;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

@Data
@TableName("BIZ_SHIP_PRE_PLAN_CABIN_RELATION")
public class BizShipWorkSequence extends BizBaseEntity {

    @TableId("ID")
    private String id;
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
     * 货物名称
     */
    @TableField("CARGO_NAME")
    private String cargoName;


    /**
     * 舱口序号
     */
    @TableField("SHIP_CABIN_NO")
    private String shipCabinNo;



    /**
     * 总重（调度室手工录入）
     */
    @TableField("TOTAL_WEIGHT")
    private BigDecimal totalWeight;

    /**
     * 单机舱时量（调度室手工录入）
     */
    @TableField("SINGLE_SHIP_WORK_HOUR_QTY")
    private BigDecimal singleShipWorkHourQty;


}
