package com.aps.yinghai.base;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
public abstract class BaseEntity {


    @TableId(value = "ID", type = IdType.ASSIGN_UUID)
    private String id;

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
