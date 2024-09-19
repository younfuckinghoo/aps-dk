package com.aps.yinghai.entity;

import com.aps.yinghai.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author haoyong
 * @since 2024-09-13
 */
@Getter
@Setter
@TableName("ALG_CARGO_INFO")
public class CargoInfo extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;



    @TableField("CARGO_NAME")
    private String cargoName;

    @TableField("CARGO_CODE")
    private String cargoCode;

}
