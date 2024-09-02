package com.aps.yinghai.entity;

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
 * 缆柱信息
 * </p>
 *
 * @author haoyong
 * @since 2024-09-02
 */
@Getter
@Setter
@TableName("bollard_info")
public class BollardInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "ID", type = IdType.AUTO)
    private Integer id;

    /**
     * 泊位ID
     */
    @TableField("BERTH_ID")
    private Integer berthId;

    /**
     * 泊位编号
     */
    @TableField("BERTH_NO")
    private String berthNo;

    /**
     * 缆桩编号
     */
    @TableField("BOLLARD_NO")
    private String bollardNo;

    /**
     * 缆桩名称
     */
    @TableField("BOLLARD_NAME")
    private String bollardName;

    /**
     * 缆桩距岸线位置
     */
    @TableField("SHORELINE_DISTANCE")
    private BigDecimal shorelineDistance;

    /**
     * 与上一缆桩距离
     */
    @TableField("LAST_BOLLARD_DISTANCE")
    private BigDecimal lastBollardDistance;
}
