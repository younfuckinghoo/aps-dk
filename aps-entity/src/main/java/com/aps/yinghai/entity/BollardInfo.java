package com.aps.yinghai.entity;

import com.aps.yinghai.base.BaseEntity;
import com.aps.yinghai.dto.PlanningBollardDTO;
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
 * 缆柱信息
 * </p>
 *
 * @author haoyong
 * @since 2024-09-02
 */
@Getter
@Setter
@TableName("ALG_BOLLARD_INFO")
public class BollardInfo extends BaseEntity implements Serializable,Cloneable {

    private static final long serialVersionUID = 1L;


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
    @TableField("BOLLARD_NO")
    private Integer bollardNo;

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



    /**
     * 是否可用
     */
    @TableField("AVAILABLE")
    private Integer available;

    /**
     * 占用到时间（到什么时候能用）
     */
    @TableField("OCCUPY_UNTIL")
    private LocalDateTime occupyUntil;

    public Object clone()throws CloneNotSupportedException {
        BollardInfo bollardInfo = (BollardInfo) super.clone();
        if (bollardInfo.getOccupyUntil()!=null)
            bollardInfo.setOccupyUntil(bollardInfo.getOccupyUntil().plusSeconds(0));
        return bollardInfo;
    }


}
