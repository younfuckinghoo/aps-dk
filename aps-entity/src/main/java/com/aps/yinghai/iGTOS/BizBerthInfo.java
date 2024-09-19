package com.aps.yinghai.iGTOS;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

@Data
@TableName("DIC_BERTH_INFO")
public class BizBerthInfo extends BizBaseEntity {

    @TableId("ID")
    private String id;
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
    @TableField("IS_LOAD_SHIP_PROCESS")
    private Integer flowLoad;

    /**
     * 流程卸船，1是；0否
     */
    @TableField("IS_UNLOAD_SHIP_PROCESS")
    private Integer flowUnload;

    /**
     * 搬倒装船，1是；0否
     */
    @TableField("IS_LOAD_SHIP_MOVE")
    private Integer moveLoad;

    /**
     * 搬倒卸船，1是；0否
     */
    @TableField("IS_UNLOAD_SHIP_MOVE")
    private Integer moveUnload;

    /**
     * 泊位长度
     */
    @TableField("BERTH_LENGTH")
    private BigDecimal berthLength;



    /**
     * 是否借用
     */
    @TableField("IS_LEND")
    private Integer isLend;

    /**
     * 是否被借用
     */
    @TableField("IS_LENDED")
    private Integer isLended;



}
