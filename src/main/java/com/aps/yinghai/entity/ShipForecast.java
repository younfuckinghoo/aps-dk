package com.aps.yinghai.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author haoyong
 * @since 2024-09-02
 */
@Getter
@Setter
@TableName("ship_forecast")
public class ShipForecast implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "ID", type = IdType.AUTO)
    private Integer id;

    /**
     * 船名
     */
    @TableField("SHIP_NAME")
    private String shipName;

    /**
     * 船号
     */
    @TableField("SHIP_NO")
    private String shipNo;

    /**
     * 内外贸
     */
    @TableField("IN_OUT_TRADE")
    private Integer inOutTrade;

    /**
     * 装载量
     */
    @TableField("LOAD_QTY")
    private Integer loadQty;

    /**
     * 抵港时间
     */
    @TableField("EXCEPT_ARRIVE_TIME")
    private LocalDateTime exceptArriveTime;

    /**
     * 货物手续
     */
    @TableField("CARGO_PROCEDURE")
    private String cargoProcedure;

    /**
     * 船舶手续
     */
    @TableField("SHIP_PROCEDURE")
    private String shipProcedure;

    /**
     * 船长
     */
    @TableField("SHIP_LENGTH")
    private BigDecimal shipLength;

    /**
     * 船宽
     */
    @TableField("SHIP_WIDTH")
    private BigDecimal shipWidth;

    /**
     * 抵港吃水
     */
    @TableField("DRAFT")
    private String draft;

    /**
     * 舱时量
     */
    @TableField("CAPCITY_PER_HOUR")
    private BigDecimal capcityPerHour;
}
