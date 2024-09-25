package com.aps.yinghai.entity;

import com.aps.yinghai.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 船舶作业信息详细信息
 * </p>
 *
 * @author haoyong
 * @since 2024-09-13
 */
@Getter
@Setter
@TableName("ALG_SHIP_WORKING_INFO_DETAIL")
public class ShipWorkingInfoDetail extends BaseEntity implements Serializable {

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
     * 估水时间 MM-dd HH:mm
     */
    @TableField("WATER_ESTIMATION_TIME")
    private String waterEstimationTime;

    /**
     * 含水量
     */
    @TableField("WATER_RATIO")
    private BigDecimal waterRatio;
}
