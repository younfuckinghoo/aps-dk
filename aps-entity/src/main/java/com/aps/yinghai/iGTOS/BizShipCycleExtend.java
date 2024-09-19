package com.aps.yinghai.iGTOS;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("BIZ_SHIPCYCLE_EXPAND")
public class BizShipCycleExtend extends BizBaseEntity {


    /**
     * SHIPCYCLE_ID
     */
    @TableField("SHIPCYCLE_ID")
    private String shipcycleId;

    /**
     * 是否保函（提单手续，不存在不能靠泊）,云港通需要推送
     */
    @TableField("IS_GUARANTEE")
    private Integer isGuarantee;

    /**
     * 是否海事预申报（前置手续7天后可排，但可商议）云港通需要推送
     */
    @TableField("IS_HSYSB")
    private Integer isHsysb;


    /**
     * 装卸类型
     */
    @TableField("LOAD_UNLOAD")
    @Schema(name = "装卸类型")
    private Integer loadUnload;

//    /**
//     * 中间水尺次数,云港通需要推送
//     */
//    @TableField("MIDDLE_WATER_NUM")
//    private Integer middleWaterNum;

    /**
     * 产地（调度手工录入）
     */
    @TableField("ORIGIN_PLACE")
    private String originPlace;

    /**
     * 是否保税（市场部手工录入）
     */
    @TableField("IS_INBOND")
    private Integer isInbond;

    /**
     * 是否卸船委托（市场部录入）
     */
    @TableField("IS_UNLOAD_SHIP_ENTRUST")
    private Integer isUnloadShipEntrust;

    /**
     * 货物是否报关（市场部录入）
     */
    @TableField("IS_CARGO_DECLARATION")
    private Integer isCargoDeclaration;

    /**
     * 是否装船通知(市场部录入)
     */
    @TableField("IS_LOAD_SHIP_NOTICE")
    private Integer isLoadShipNotice;

    /**
     * 是否放货指令(市场部录入)
     */
    @TableField("IS_URL_FHZL")
    private Integer isUrlFhzl;


}
