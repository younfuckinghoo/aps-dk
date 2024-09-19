package com.aps.yinghai.iGTOS;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("BIZ_TIDE_DATA")
public class BizTide  implements Serializable {

    private static final long serialVersionUID = 1L;


    @TableId("ID")
    private String id;

    @TableField("TIDE_DATE")
    private String tideDate;

    private String h0;

    private String h1;

    private String h2;

    private String h3;

    private String h4;

    private String h5;

    private String h6;

    private String h7;

    private String h8;

    private String h9;

    private String h10;

    private String h11;

    private String h12;

    private String h13;

    private String h14;

    private String h15;

    private String h16;

    private String h17;

    private String h18;

    private String h19;

    private String h20;

    private String h21;

    private String h22;

    private String h23;

    private String tideHeight1;

    private String tideHeight2;

    private String tideHeight3;

    private String tideHeight4;

    private String tideTime1;

    private String tideTime2;

    private String tideTime3;

    private String tideTime4;

    private String tideLowhigh1;

    private String tideLowhigh2;

    private String tideLowhigh3;

    private String tideLowhigh4;


}
