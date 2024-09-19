package com.aps.yinghai.iGTOS;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

@Data
@TableName("DIC_CARGO_NAME_INFO")
public class BizCargoInfo extends BizBaseEntity {


    @TableId("ID")
    private String id;
    /**
     * 货名
     */
    @TableField("CARGO_NAME")
    private String cargoName;

    /**
     * 货名代码
     */
    @TableField("CARGO_NAME_CODE")
    private String cargoNameCode;



    /**
     * 是否删除
     */
    @TableField("IS_DELETE")
    private Integer isDelete;

    /**
     * 类型 3 三级货类
     */
    @TableField("CARGO_NAME_TYPE")
    private Integer cargoNameType;
}
