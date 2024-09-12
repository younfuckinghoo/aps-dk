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
--                                  `DRAFT` decimal(7,2) DEFAULT NULL COMMENT '抵港吃水',
--                                  `CAPACITY_PER_HOUR` decimal(8,2) DEFAULT NULL COMMENT '舱时量',
--                                  `LOAD_UNLOAD` int DEFAULT NULL COMMENT '装/卸',
--                                  `SHIP_STATUS` int DEFAULT NULL COMMENT '船舶状态（1未就绪；2已就绪；3已作业；4作业完成）,
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
     `AVAILABLE` int DEFAULT NULL COMMENT '是否可用',
     PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
ALTER TABLE `aps`.`BERTH_INFO` COMMENT = '泊位信息';


CREATE TABLE `BOLLARD_INFO` (
     `ID` int NOT NULL AUTO_INCREMENT,
     `BERTH_ID` int DEFAULT NULL COMMENT '泊位ID',
     `BERTH_NO` varchar(255) DEFAULT NULL COMMENT '泊位编号',
     `BOLLARD_NO` int DEFAULT NULL COMMENT '缆桩编号',
     `BOLLARD_NAME` varchar(255) DEFAULT NULL COMMENT '缆桩名称',
     `SHORELINE_DISTANCE` decimal(7,2) DEFAULT NULL COMMENT '缆桩距岸线位置',
     `LAST_BOLLARD_DISTANCE` decimal(7,2) DEFAULT NULL COMMENT '与上一缆桩距离',
     `OCCUPY_UNTIL` timestamp NULL DEFAULT NULL COMMENT '占用到时间（到什么时候能用）',
     PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
ALTER TABLE `aps`.`BOLLARD_INFO` COMMENT = '缆柱信息';


CREATE TABLE `CABIN_INFO` (
                                `ID` int NOT NULL AUTO_INCREMENT,
                                `SHIP_ID` int DEFAULT NULL COMMENT '船舶ID',
                                `CABIN_NO` int DEFAULT NULL COMMENT '舱号',
                                `LOAD_QTY` decimal(7,2) DEFAULT NULL COMMENT '装载量',
                                `CARGO_NAME` varchar(255) DEFAULT NULL COMMENT '货种名称',
                                PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
ALTER TABLE `aps`.`CABIN_INFO` COMMENT = '舱口信息';


CREATE TABLE `aps`.`TIDE`  (
   `ID` int NOT NULL AUTO_INCREMENT,
   `TIDE_DATE` date NULL COMMENT '日期',
   `FLOW_FIRST` timestamp NULL COMMENT '第一次涨潮',
   `EBB_FIRST` timestamp NULL COMMENT '第一次落潮',
   `FLOW_SECOND` timestamp NULL COMMENT '第二次涨潮',
   `EBB_SECOND` timestamp NULL COMMENT '第二次落潮',
   `AT_0` int NULL COMMENT '0时潮高',
   `AT_1` int NULL COMMENT '1时潮高',
   `AT_2` int NULL COMMENT '2时潮高',
   `AT_3` int NULL COMMENT '3时潮高',
   `AT_4` int NULL COMMENT '4时潮高',
   `AT_5` int NULL COMMENT '5时潮高',
   `AT_6` int NULL COMMENT '6时潮高',
   `AT_7` int NULL COMMENT '7时潮高',
   `AT_8` int NULL COMMENT '8时潮高',
   `AT_9` int NULL COMMENT '9时潮高',
   `AT_10` int NULL COMMENT '10时潮高',
   `AT_11` int NULL COMMENT '11时潮高',
   `AT_12` int NULL COMMENT '12时潮高',
   `AT_13` int NULL COMMENT '13时潮高',
   `AT_14` int NULL COMMENT '14时潮高',
   `AT_15` int NULL COMMENT '15时潮高',
   `AT_16` int NULL COMMENT '16时潮高',
   `AT_17` int NULL COMMENT '17时潮高',
   `AT_18` int NULL COMMENT '18时潮高',
   `AT_19` int NULL COMMENT '19时潮高',
   `AT_20` int NULL COMMENT '20时潮高',
   `AT_21` int NULL COMMENT '21时潮高',
   `AT_22` int NULL COMMENT '22时潮高',
   `AT_23` int NULL COMMENT '23时潮高',

   PRIMARY KEY (`ID`)
);