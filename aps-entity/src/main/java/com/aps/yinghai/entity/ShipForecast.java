package com.aps.yinghai.entity;

import com.aps.yinghai.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author haoyong
 * @since 2024-09-02
 */
@Getter
@Setter
@TableName("ALG_SHIP_FORECAST")
@Schema(name = "船舶预报")
public class ShipForecast extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

//    @TableId(value = "ID", type = IdType.AUTO)
//    @Schema(name = "ID")
//    private Integer id;

    /**
     * 船名
     */
    @TableField("SHIP_NAME")
    @Schema(name = "船名")
    private String shipName;

    /**
     * 船号
     */
    @TableField("SHIP_NO")
    @Schema(name = "船号")
    private String shipNo;

    /**
     * 内外贸(1内贸；2外贸)
     */
    @TableField("IN_OUT_TRADE")
    @Schema(name = "内外贸")
    private Integer inOutTrade;


    /**
     * 装载量
     */
    @TableField("LOAD_QTY")
    @Schema(name = "装载量")
    private BigDecimal loadQty;
    /**
     * 进出口载货量 来自船舶资料 优先考虑
     */
    @TableField("IN_OUT_LOAD_QTY")
    @Schema(name = "进出口载货量")
    private BigDecimal inOutLoadQty;

    /**
     * 舱口数
     */
    @TableField("SHIPCABIN_QTY")
    private Integer shipcabinQty;

    /**
     * 货物手续
     */
    @TableField("CARGO_PROCEDURE")
    @Schema(name = "货物手续")
    private String cargoProcedure;

    /**
     * 船舶手续
     */
    @TableField("SHIP_PROCEDURE")
    @Schema(name = "船舶手续")
    private String shipProcedure;

    /**
     * 船长
     */
    @TableField("SHIP_LENGTH")
    @Schema(name = "船长")
    private BigDecimal shipLength;

    /**
     * 船宽
     */
    @TableField("SHIP_WIDTH")
    @Schema(name = "船宽")
    private BigDecimal shipWidth;

    /**
     * 抵港吃水
     */
    @TableField("DRAFT")
    @Schema(name = "抵港吃水")
    private BigDecimal draft;

    /**
     * 舱时量
     */
    @TableField("CAPACITY_PER_HOUR")
    @Schema(name = "舱时量")
    private BigDecimal capacityPerHour;

    /**
     * 装卸类型
     */
    @TableField("LOAD_UNLOAD")
    @Schema(name = "装卸类型")
    private Integer loadUnload;

    /**
     * 船舶状态（1未就绪；2已就绪；3已作业；4作业完成）
     */
    @TableField("SHIP_STATUS")
    @Schema(name = "船舶状态")
    private Integer shipStatus;




    /**
     * 抵港艏吃水
     */
    @TableField("ARRIVE_START_WATER")
    private BigDecimal arriveStartWater;

    /**
     * 抵港中吃水
     */
    @TableField("MIDDLE_WATER")
    private BigDecimal middleWater;

    /**
     * 抵港艉吃水
     */
    @TableField("ARRIVE_END_WATER")
    private BigDecimal arriveEndWater;

    /**
     * 离港艏吃水
     */
    @TableField("LEAVE_START_WATER")
    private BigDecimal leaveStartWater;

    /**
     * 离港中吃水
     */
    @TableField("MIDDLE_WATER_OUT")
    private BigDecimal middleWaterOut;

    /**
     * 离港艉吃水
     */
    @TableField("LEAVE_END_WATER")
    private BigDecimal leaveEndWater;

    /**
     * 出口船代
     */
    @TableField("SHIPAGENT_OUT_NAME")
    private String shipagentOutName;

    /**
     * 进口船代
     */
    @TableField("SHIPAGENT_IN_NAME")
    private String shipagentInName;

    /**
     * 货代
     */
    @TableField("FORWARDER")
    private String forwarder;

    /**
     * 船舶是否报关（前置手续）
     */
    @TableField("HGFXZT_ISRELEASE")
    private Integer hgfxztIsrelease;

    /**
     * 抵港时间（预抵时间，经常更新）
     */
    @TableField("DATE_ARRIVE")
    private LocalDateTime dateArrive;

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
     * 中间水尺次数,云港通需要推送
     */
    @TableField("MIDDLE_WATER_NUM")
    private Integer middleWaterNum;

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

    /**
     * {@ShipAlgStateEnum}
     * 算法状态
     */
    @TableField("ALGORITHM_STATE")
    private Integer algorithmState;

    /**
     * 抵港吃水
     */
    @TableField("ARRIVE_DRAUGHT")
    private BigDecimal arriveDraught;

    /**
     * 离港吃水
     */
    @TableField("LEAVE_DRAUGHT")
    private BigDecimal leaveDraught;

    /**
     * 预抵时间
     */

    /**
     * 预抵时间（预计划）
     */

    @Schema(name = "预抵时间")
//    @JsonSerialize(using = LocalDateTimeSerializer.class)
//    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @TableField("EXPECT_ARRIVE_TIME")
    private LocalDateTime expectArriveTime;

    /**
     * 等待常数排水时长（数字）
     */
    @TableField("WAIT_CONSTANT_DRAINAGE_TIME")
    private BigDecimal waitConstantDrainageTime;

//    /**
//     * 离泊水尺（装船、数字）
//     */
//    @TableField("UNBERTHING_DRAFT")
//    private BigDecimal unberthingDraft;





    /**
     * IGTOS 中的船期 ID
     */
    @TableField("IGTOS_SHIPCYCLE_ID")
    private String igtosShipcycleId;

    /**
     * 是否特殊作业默认为否
     */
    @TableField("IS_SPECIAL_WORK")
    private Integer isSpecialWork;

    /**
     * 岸机类型
     */
    @TableField("MACHINE_TYPE_CODE")
    private String machineTypeCode;

    /**
     * 岸机数量
     */
    @TableField("MACHINE_COUNT")
    private Integer machineCount;

//    /**
//     * 含水量
//     */
//    @TableField("WATER_RATIO")
//    private BigDecimal waterRatio;




    /**
     *泊位
     */
    @TableField(value = "BERTH_NO",updateStrategy= FieldStrategy.NOT_NULL)
    private String berthNo;

    /**
     *靠泊时间
     */
    @TableField(value = "EXPECT_BERTH_TIME",updateStrategy= FieldStrategy.NOT_NULL)
    private LocalDateTime expectBerthTime;

    /**
     *开工时间
     */
    @TableField(value = "START_TIME",updateStrategy= FieldStrategy.NOT_NULL)
    private LocalDateTime startTime;

    /**
     *完工时间
     */
    @TableField(value = "END_TIME",updateStrategy= FieldStrategy.NOT_NULL)
    private LocalDateTime endTime;
    /**
     *离港时间
     */
    @TableField(value = "EXPECT_LEAVE_TIME",updateStrategy= FieldStrategy.NOT_NULL)
    private LocalDateTime expectLeaveTime;

    /**
     *前缆
     */
    @TableField(value = "BOLLARD_FORWARD",updateStrategy= FieldStrategy.NOT_NULL)
    private String bollardForward;
    /**
     *后缆
     */
    @TableField(value = "BOLLARD_BEHIND",updateStrategy= FieldStrategy.NOT_NULL)
    private String bollardBehind;

}
