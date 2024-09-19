package com.aps.yinghai.iGTOS;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public abstract class BizBaseEntity {


    /**
     * 创建时间
     */
    @TableField("CREATE_DATE")
    private LocalDateTime createDate;
    /**
     * 更新时间
     */
    @TableField("REVISE_DATE")
    private LocalDateTime reviseDate;

}
