package com.aps.yinghai.entity;

import com.aps.yinghai.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author haoyong
 * @since 2024-09-10
 */
@Getter
@Setter
@TableName("ALG_TIDE")
public class Tide implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "ID", type = IdType.ASSIGN_UUID)
    private String id;

    private LocalDate date;

    private BigDecimal h0;

    private BigDecimal h1;

    private BigDecimal h2;

    private BigDecimal h3;

    private BigDecimal h4;

    private BigDecimal h5;

    private BigDecimal h6;

    private BigDecimal h7;

    private BigDecimal h8;

    private BigDecimal h9;

    private BigDecimal h10;

    private BigDecimal h11;

    private BigDecimal h12;

    private BigDecimal h13;

    private BigDecimal h14;

    private BigDecimal h15;

    private BigDecimal h16;

    private BigDecimal h17;

    private BigDecimal h18;

    private BigDecimal h19;

    private BigDecimal h20;

    private BigDecimal h21;

    private BigDecimal h22;

    private BigDecimal h23;

    private BigDecimal tideHeight1;

    private BigDecimal tideHeight2;

    private BigDecimal tideHeight3;

    private BigDecimal tideHeight4;

    private LocalTime tideTime1;

    private LocalTime tideTime2;

    private LocalTime tideTime3;

    private LocalTime tideTime4;


}
