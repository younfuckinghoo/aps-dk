package com.aps.yinghai.entity;

import com.aps.yinghai.base.BaseEntity;
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
 * 船舶舱口卸序
 * </p>
 *
 * @author haoyong
 * @since 2024-09-13
 */
@Getter
@Setter
@TableName("ALG_SHIP_WORKING_SEQUENCE")
public class ShipWorkingSequence extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 父表id
     */
    @TableField("SHIP_FORECAST_ID")
    private String shipForecastId;

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
