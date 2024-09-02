package com.aps.yinghai.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(name = "船舶预报")
@Data
public class Ship {

    @Schema(title = "id", description = "ID", defaultValue = "")
    private String id;

    @Schema(title = "shipName", description = "船名", defaultValue = "")
    private String shipName;

    @Schema(title = "shipNo", description = "船号", defaultValue = "")
    private BigDecimal shipNo;

    @Schema(title = "inOutTrade", description = "贸易类型-内外贸", defaultValue = "")
    private BigDecimal inOutTrade;

    @Schema(title = "loadQty", description = "载货量", defaultValue = "")
    private BigDecimal loadQty;

    @Schema(title = "expectArriveTime", description = "抵港时间-预抵时间", defaultValue = "")
    private LocalDateTime expectArriveTime;

    @Schema(title = "guaranteeLetter", description = "保函", defaultValue = "")
    private String guaranteeLetter;

    @Schema(title = "customsDeclaration", description = "报关", defaultValue = "")
    private String customsDeclaration;

    @Schema(title = "maritime", description = "海事", defaultValue = "")
    private String maritime;

    @Schema(title = "shipLength", description = "船长", defaultValue = "")
    private BigDecimal shipLength;

    @Schema(title = "shipWidth", description = "船宽", defaultValue = "")
    private BigDecimal shipWidth;

    @Schema(title = "arriveDraught", description = "抵港吃水", defaultValue = "")
    private String arriveDraught;

    @Schema(title = "agentName", description = "船代-代理", defaultValue = "")
    private String agentName;

    @Schema(title = "cargoFactory", description = "厂家-货物厂家", defaultValue = "")
    private String cargoFactory;

    @Schema(title = "cargoAgent", description = "货代", defaultValue = "")
    private String cargoAgent;

    @Schema(title = "loadSequence", description = "配载卸序", defaultValue = "")
    private String loadSequence;

    @Schema(title = "draftMark", description = "水尺", defaultValue = "")
    private String draftMark;

}
