package com.aps.yinghai.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
@TableName("tide")
public class Tide implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "ID", type = IdType.AUTO)
    private Integer id;

    /**
     * 日期
     */
    @TableField("TIDE_DATE")
    private LocalDate tideDate;

    /**
     * 第一次涨潮
     */
    @TableField("FLOW_FIRST")
    private LocalDateTime flowFirst;

    /**
     * 第一次落潮
     */
    @TableField("EBB_FIRST")
    private LocalDateTime ebbFirst;

    /**
     * 第二次涨潮
     */
    @TableField("FLOW_SECOND")
    private LocalDateTime flowSecond;

    /**
     * 第二次落潮
     */
    @TableField("EBB_SECOND")
    private LocalDateTime ebbSecond;

    /**
     * 0时潮高
     */
    @TableField("AT_0")
    private Integer at0;

    /**
     * 1时潮高
     */
    @TableField("AT_1")
    private Integer at1;

    /**
     * 2时潮高
     */
    @TableField("AT_2")
    private Integer at2;

    /**
     * 3时潮高
     */
    @TableField("AT_3")
    private Integer at3;

    /**
     * 4时潮高
     */
    @TableField("AT_4")
    private Integer at4;

    /**
     * 5时潮高
     */
    @TableField("AT_5")
    private Integer at5;

    /**
     * 6时潮高
     */
    @TableField("AT_6")
    private Integer at6;

    /**
     * 7时潮高
     */
    @TableField("AT_7")
    private Integer at7;

    /**
     * 8时潮高
     */
    @TableField("AT_8")
    private Integer at8;

    /**
     * 9时潮高
     */
    @TableField("AT_9")
    private Integer at9;

    /**
     * 10时潮高
     */
    @TableField("AT_10")
    private Integer at10;

    /**
     * 11时潮高
     */
    @TableField("AT_11")
    private Integer at11;

    /**
     * 12时潮高
     */
    @TableField("AT_12")
    private Integer at12;

    /**
     * 13时潮高
     */
    @TableField("AT_13")
    private Integer at13;

    /**
     * 14时潮高
     */
    @TableField("AT_14")
    private Integer at14;

    /**
     * 15时潮高
     */
    @TableField("AT_15")
    private Integer at15;

    /**
     * 16时潮高
     */
    @TableField("AT_16")
    private Integer at16;

    /**
     * 17时潮高
     */
    @TableField("AT_17")
    private Integer at17;

    /**
     * 18时潮高
     */
    @TableField("AT_18")
    private Integer at18;

    /**
     * 19时潮高
     */
    @TableField("AT_19")
    private Integer at19;

    /**
     * 20时潮高
     */
    @TableField("AT_20")
    private Integer at20;

    /**
     * 21时潮高
     */
    @TableField("AT_21")
    private Integer at21;

    /**
     * 22时潮高
     */
    @TableField("AT_22")
    private Integer at22;

    /**
     * 23时潮高
     */
    @TableField("AT_23")
    private Integer at23;
}
