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
 * 泊位信息
 * </p>
 *
 * @author haoyong
 * @since 2024-09-02
 */
@Getter
@Setter
@TableName("ALG_BERTH_INFO")
public class BerthInfo extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 泊位编号
     */
    @TableField("BERTH_NO")
    private String berthNo;

    /**
     * 泊位名称
     */
    @TableField("BERTH_NAME")
    private String berthName;

    /**
     * 流程装船，1是；0否
     */
    @TableField("FLOW_LOAD")
    private Integer flowLoad;

    /**
     * 流程卸船，1是；0否
     */
    @TableField("FLOW_UNLOAD")
    private Integer flowUnload;

    /**
     * 搬倒装船，1是；0否
     */
    @TableField("MOVE_LOAD")
    private Integer moveLoad;

    /**
     * 搬倒卸船，1是；0否
     */
    @TableField("MOVE_UNLOAD")
    private Integer moveUnload;

    /**
     * 泊位长度
     */
    @TableField("BERTH_LENGTH")
    private BigDecimal berthLength;

    /**
     * 是否可用
     */
    @TableField("AVAILABLE")
    private Integer available;

    /**
     * 是否借用
     */
    @TableField("IS_LEND")
    private Integer isLend;

    /**
     * 是否被借用
     */
    @TableField("IS_BE_LEND")
    private Integer isBeLend;


}
