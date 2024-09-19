package com.aps.yinghai.iGTOS;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

@Data
@TableName("DIC_BOLLARD_INFO")
public class BizBollardInfo extends BizBaseEntity {

    @TableId("ID")
    private String id;
    /**
     * 泊位ID
     */
    @TableField("BERTH_ID")
    private String berthId;

    /**
     * 泊位编号
     */
    @TableField("BERTH_NO")
    private String berthNo;

    /**
     * 缆桩编号
     */
    @TableField("BOLLARD_CODE")
    private Integer bollardNo;

    /**
     * 缆桩名称
     */
    @TableField("BOLLARD_NAME")
    private String bollardName;

    /**
     * 缆桩距岸线位置
     */
    @TableField("ABSOLUTE_DISTANCE")
    private BigDecimal shorelineDistance;

    /**
     * 与上一缆桩距离
     */
    @TableField("BOLLARD_SPACING")
    private BigDecimal lastBollardDistance;

    /**
     * 停用标记1停用 0可用
     */
    @TableField("IS_STOP")
    private Integer isStop;



}
