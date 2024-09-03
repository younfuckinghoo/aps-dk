package com.aps.yinghai.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 舱口信息
 * </p>
 *
 * @author haoyong
 * @since 2024-09-03
 */
@Getter
@Setter
@TableName("cabin_info")
@Schema(name = "舱口信息")
public class CabinInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "ID", type = IdType.AUTO)
    private Integer id;

    /**
     * 船舶ID
     */
    @TableField("SHIP_ID")
    private Integer shipId;

    /**
     * 舱号
     */
    @TableField("CABIN_NO")
    private Integer cabinNo;

    /**
     * 装载量
     */
    @TableField("LOAD_QTY")
    private BigDecimal loadQty;

    /**
     * 货种名称
     */
    @TableField("CARGO_NAME")
    private String cargoName;
}
