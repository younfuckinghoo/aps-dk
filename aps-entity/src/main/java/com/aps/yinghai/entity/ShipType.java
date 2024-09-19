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
 * 船型
 * </p>
 *
 * @author haoyong
 * @since 2024-09-13
 */
@Getter
@Setter
@TableName("ALG_SHIP_TYPE")
public class ShipType  implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "ID", type = IdType.AUTO)
    private Integer id;

    /**
     * 船型
     */
    @TableField("SHIP_TYPE")
    private String shipType;

    /**
     * 当前阶段-开始
     */
    @TableField("STAGE_START")
    private BigDecimal stageStart;

    /**
     * 当前阶段-结束
     */
    @TableField("STAGE_END")
    private BigDecimal stageEnd;

    /**
     * 舱时量
     */
    @TableField("CAPACITY_PER_HOUR")
    private BigDecimal capacityPerHour;

    /**
     * 清仓机械 机械代码
     */
    @TableField("CLEAN_MACHINE")
    private String cleanMachine;
}
