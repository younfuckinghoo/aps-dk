package com.aps.yinghai.iGTOS;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;

@TableName("BIZ_SHIPCYCLE")
public class ShipCycle {

    /**
     * 中文名
     */
    @TableField("SHIP_NAME_CH")
    private String shipNameCh;
    /**
     * 英文名
     */
    @TableField("SHIP_NAME_EN")
    private String shipNameEn;
    /**
     *
     */
    @TableField("SHIP_NO")
    private String shipNo;
    /**
     *
     */
    @TableField("IN_OUT_TRADE")
    private String inOutTrade;
    /**
     *
     */
    @TableField("DEADWEIGHT_TON")
    private String deadweightTon;
    /**
     *
     */
    @TableField("SHIP_LENGTH")
    private BigDecimal shipLength;
    /**
     *
     */
    @TableField("SHIP_WIDTH")
    private BigDecimal shipWidth;
    /**
     *
     */
    @TableField("ARRIVE_START_WATER")
    private BigDecimal arriveStartWater;
    /**
     *
     */
    @TableField("ARRIVE_END_WATER")
    private BigDecimal arriveEndWater;
    /**
     *
     */
    @TableField("LEAVE_START_WATER")
    private BigDecimal leaveStartWater;
    /**
     * LEAVE_END_WATER
     */
    @TableField("LEAVE_END_WATER")
    private BigDecimal leaveEndWater;

}
