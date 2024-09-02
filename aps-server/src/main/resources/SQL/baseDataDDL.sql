-- CREATE TABLE `ship_forecast` (
--                                  `ID` int NOT NULL AUTO_INCREMENT,
--                                  `SHIP_NAME` varchar(255) DEFAULT NULL COMMENT '船名',
--                                  `SHIP_NO` varchar(255) DEFAULT NULL COMMENT '船号',
--                                  `IN_OUT_TRADE` int DEFAULT NULL COMMENT '内外贸',
--                                  `LOAD_QTY` int DEFAULT NULL COMMENT '装载量',
--                                  `EXCEPT_ARRIVE_TIME` timestamp NULL DEFAULT NULL COMMENT '抵港时间',
--                                  `CARGO_PROCEDURE` varchar(255) DEFAULT NULL COMMENT '货物手续',
--                                  `SHIP_PROCEDURE` varchar(255) DEFAULT NULL COMMENT '船舶手续',
--                                  `SHIP_LENGTH` decimal(7,2) DEFAULT NULL COMMENT '船长',
--                                  `SHIP_WIDTH` decimal(7,2) DEFAULT NULL COMMENT '船宽',
--                                  `DRAFT` varchar(255) DEFAULT NULL COMMENT '抵港吃水',
--                                  `CAPACITY_PER_HOUR` decimal(6,2) DEFAULT NULL COMMENT '舱时量',
--                                  `LOAD_UNLOAD` int DEFAULT NULL COMMENT '装/卸',
--                                  PRIMARY KEY (`ID`)
-- ) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
-- ALTER TABLE `aps`.`ship_forecast` COMMENT = '船舶预报';

CREATE TABLE `BERTH_INFO` (
     `ID` int NOT NULL AUTO_INCREMENT,
     `BERTH_NO` varchar(255) DEFAULT NULL COMMENT '泊位编号',
     `BERTH_NAME` varchar(255) DEFAULT NULL COMMENT '泊位名称',
     `FLOW_LOAD` int DEFAULT NULL COMMENT '流程装船，1是；0否',
     `FLOW_UNLOAD` int DEFAULT NULL COMMENT '流程卸船，1是；0否',
     `MOVE_LOAD` int DEFAULT NULL COMMENT '搬倒装船，1是；0否',
     `MOVE_UNLOAD` int DEFAULT NULL COMMENT '搬倒卸船，1是；0否',
     `BERTH_LENGTH` decimal(7,2) DEFAULT NULL COMMENT '泊位长度',
     PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
ALTER TABLE `aps`.`BERTH_INFO` COMMENT = '泊位信息';


CREATE TABLE `BOLLARD_INFO` (
     `ID` int NOT NULL AUTO_INCREMENT,
     `BERTH_ID` int DEFAULT NULL COMMENT '泊位ID',
     `BERTH_NO` varchar(255) DEFAULT NULL COMMENT '泊位编号',
     `BOLLARD_NO` varchar(255) DEFAULT NULL COMMENT '缆桩编号',
     `BOLLARD_NAME` varchar(255) DEFAULT NULL COMMENT '缆桩名称',
     `SHORELINE_DISTANCE` decimal(7,2) DEFAULT NULL COMMENT '缆桩距岸线位置',
     `LAST_BOLLARD_DISTANCE` decimal(7,2) DEFAULT NULL COMMENT '与上一缆桩距离',
     PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
ALTER TABLE `aps`.`BOLLARD_INFO` COMMENT = '缆柱信息';