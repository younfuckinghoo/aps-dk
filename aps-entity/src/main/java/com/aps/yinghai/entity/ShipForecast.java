package com.aps.yinghai.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(name = "船舶预报")
public class ShipForecast implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "ID", type = IdType.AUTO)
    @Schema(name = "ID")
    private Integer id;

    /**
     * 船名
     */
    @TableField("SHIP_NAME")
    @Schema(name = "船名")
    private String shipName;

    /**
     * 船号
     */
    @TableField("SHIP_NO")
    @Schema(name = "船号")
    private String shipNo;

    /**
     * 内外贸
     */
    @TableField("IN_OUT_TRADE")
    @Schema(name = "内外贸")
    private Integer inOutTrade;

    /**
     * 装卸类型
     */
    @TableField("LOAD_UNLOAD")
    @Schema(name = "装卸类型")
    private Integer loadUnload;

    /**
     * 装载量
     */
    @TableField("LOAD_QTY")
    @Schema(name = "装载量")
    private Integer loadQty;

    /**
     * 抵港时间
     */
    @TableField("EXCEPT_ARRIVE_TIME")
    @Schema(name = "抵港时间")
//    @JsonSerialize(using = LocalDateTimeSerializer.class)
//    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private LocalDateTime exceptArriveTime;

    /**
     * 货物手续
     */
    @TableField("CARGO_PROCEDURE")
    @Schema(name = "货物手续")
    private String cargoProcedure;

    /**
     * 船舶手续
     */
    @TableField("SHIP_PROCEDURE")
    @Schema(name = "船舶手续")
    private String shipProcedure;

    /**
     * 船长
     */
    @TableField("SHIP_LENGTH")
    @Schema(name = "船长")
    private BigDecimal shipLength;

    /**
     * 船宽
     */
    @TableField("SHIP_WIDTH")
    @Schema(name = "船宽")
    private BigDecimal shipWidth;

    /**
     * 抵港吃水
     */
    @TableField("DRAFT")
    @Schema(name = "抵港吃水")
    private String draft;

    /**
     * 舱时量
     */
    @TableField("CAPACITY_PER_HOUR")
    @Schema(name = "舱时量")
    private BigDecimal capacityPerHour;
}
