package com.aps.yinghai.iGTOS;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("BIZ_SHIPCYCLE")
public class BizShipCycle extends BizBaseEntity {



    @TableId("SHIPCYCLE_ID")
    private String id;

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
    private Integer inOutTrade;
    /**
     * 船标准载重吨
     */
    @TableField("DEADWEIGHT_TON")
    private BigDecimal deadweightTon;
    /**
     *进口载货量
     */
    @TableField("LOADQTY_IN")
    private BigDecimal loadqtyIn;
    /**
     *出口载货量
     */
    @TableField("LOADQTY_OUT")
    private BigDecimal loadqtyOut;
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
     * 抵港中吃水
     */
    @TableField("MIDDLE_WATER")
    private BigDecimal middleWater;
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
     * 离港中吃水
     */
    @TableField("MIDDLE_WATER_OUT")
    private BigDecimal middleWaterOut;
    /**
     * LEAVE_END_WATER
     */
    @TableField("LEAVE_END_WATER")
    private BigDecimal leaveEndWater;

    /**
     * 出口船代
     */
    @TableField("SHIPAGENT_OUT_NAME")
    private String shipagentOutName;

    /**
     * 进口船代
     */
    @TableField("SHIPAGENT_IN_NAME")
    private String shipagentInName;

    /**
     * 货代
     */
    @TableField("FORWARDER")
    private String forwarder;

    /**
     * 船舶是否报关（前置手续）
     */
    @TableField("HGFXZT_ISRELEASE")
    private Integer hgfxztIsrelease;
    /**
     * 舱口数
     */
    @TableField("SHIPCABIN_QTY")
    private Integer shipcabinQty;

    /**
     * 抵港时间（预抵时间，经常更新）
     */
    @TableField("DATE_ARRIVE")
    private LocalDateTime dateArrive;
}
