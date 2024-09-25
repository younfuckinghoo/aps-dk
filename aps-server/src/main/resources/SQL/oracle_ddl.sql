--泊位信息
CREATE TABLE ALG_BERTH_INFO (
                                ID VARCHAR2(36) NOT NULL,
                                BERTH_NO VARCHAR2(255) DEFAULT NULL,
                                BERTH_NAME VARCHAR2(255) DEFAULT NULL,
                                FLOW_LOAD NUMBER(1) DEFAULT NULL,
                                FLOW_UNLOAD NUMBER(1) DEFAULT NULL,
                                MOVE_LOAD NUMBER(1) DEFAULT NULL,
                                MOVE_UNLOAD NUMBER(1) DEFAULT NULL,
                                BERTH_LENGTH NUMBER(7,2) DEFAULT NULL,
                                AVAILABLE NUMBER(1) DEFAULT NULL,
                                CREATE_DATE TIMESTAMP DEFAULT NULL,
                                REVISE_DATE TIMESTAMP DEFAULT NULL,
                                IS_LEND NUMBER(1) DEFAULT NULL,
                                IS_BE_LEND NUMBER(1) DEFAULT NULL,
                                PRIMARY KEY (ID)
);

--缆柱信息
CREATE TABLE ALG_BOLLARD_INFO (
                                  ID VARCHAR2(36) NOT NULL,
                                  BERTH_ID VARCHAR2(36) DEFAULT NULL,
                                  BERTH_NO VARCHAR2(255) DEFAULT NULL,
                                  BOLLARD_NO NUMBER(10,0) DEFAULT NULL,
                                  BOLLARD_NAME VARCHAR2(255) DEFAULT NULL,
                                  SHORELINE_DISTANCE NUMBER(7,2) DEFAULT NULL,
                                  LAST_BOLLARD_DISTANCE NUMBER(7,2) DEFAULT NULL,
                                  OCCUPY_UNTIL TIMESTAMP DEFAULT NULL,
                                  CREATE_DATE TIMESTAMP DEFAULT NULL,
                                  REVISE_DATE TIMESTAMP DEFAULT NULL,
                                  PRIMARY KEY (ID)
);

--舱口信息
CREATE TABLE ALG_CABIN_INFO (
                                ID VARCHAR2(36) NOT NULL,
                                SHIP_ID VARCHAR2(36) DEFAULT NULL,
                                CABIN_NO NUMBER(10,0) DEFAULT NULL,
                                LOAD_QTY NUMBER(7,2) DEFAULT NULL,
                                CARGO_NAME VARCHAR2(255) DEFAULT NULL,
                                PRIMARY KEY (ID)
);

--货种信息
CREATE TABLE ALG_CARGO_INFO (
                                ID VARCHAR2(36) NOT NULL,
                                CARGO_NAME VARCHAR2(255) DEFAULT NULL,
                                CARGO_CODE VARCHAR2(255) DEFAULT NULL,
                                CREATE_DATE TIMESTAMP DEFAULT NULL,
                                REVISE_DATE TIMESTAMP DEFAULT NULL,
                                PRIMARY KEY (ID)
);

--船舶预报
CREATE TABLE ALG_SHIP_FORECAST (
                                   ID VARCHAR2(36) NOT NULL,
                                   SHIP_NAME VARCHAR2(255) DEFAULT NULL,
                                   SHIP_NO VARCHAR2(255) DEFAULT NULL,
                                   IN_OUT_TRADE NUMBER(1) DEFAULT NULL,
                                   CARGO_PROCEDURE VARCHAR2(255) DEFAULT NULL,
                                   SHIP_PROCEDURE VARCHAR2(255) DEFAULT NULL,
                                   SHIP_LENGTH NUMBER(7,2) DEFAULT NULL,
                                   SHIP_WIDTH NUMBER(7,2) DEFAULT NULL,
                                   DRAFT NUMBER(4,2) DEFAULT NULL,
                                   CAPACITY_PER_HOUR NUMBER(8,2) DEFAULT NULL,
                                   LOAD_UNLOAD NUMBER(1) DEFAULT NULL,
                                   SHIP_STATUS NUMBER(1) DEFAULT NULL,
                                   LOAD_QTY NUMBER(9,2) DEFAULT NULL,
                                   IN_OUT_LOAD_QTY NUMBER(9,2) DEFAULT NULL,
                                   ARRIVE_START_WATER NUMBER(4,2) DEFAULT NULL,
                                   MIDDLE_WATER NUMBER(4,2) DEFAULT NULL,
                                   ARRIVE_END_WATER NUMBER(4,2) DEFAULT NULL,
                                   LEAVE_START_WATER NUMBER(4,2) DEFAULT NULL,
                                   MIDDLE_WATER_OUT NUMBER(4,2) DEFAULT NULL,
                                   LEAVE_END_WATER NUMBER(4,2) DEFAULT NULL,
                                   SHIPAGENT_OUT_NAME VARCHAR2(127) DEFAULT NULL,
                                   SHIPAGENT_IN_NAME VARCHAR2(127) DEFAULT NULL,
                                   FORWARDER VARCHAR2(127) DEFAULT NULL,
                                   HGFXZT_ISRELEASE NUMBER(1) DEFAULT NULL,
                                   DATE_ARRIVE TIMESTAMP DEFAULT NULL,
                                   IS_GUARANTEE NUMBER(1) DEFAULT NULL,
                                   IS_HSYSB NUMBER(1) DEFAULT NULL,
                                   MIDDLE_WATER_NUM NUMBER(10,0) DEFAULT NULL,
                                   ORIGIN_PLACE VARCHAR2(127) DEFAULT NULL,
                                   IS_INBOND NUMBER(1) DEFAULT NULL,
                                   IS_UNLOAD_SHIP_ENTRUST NUMBER(1) DEFAULT NULL,
                                   IS_CARGO_DECLARATION NUMBER(1) DEFAULT NULL,
                                   IS_LOAD_SHIP_NOTICE NUMBER(1) DEFAULT NULL,
                                   IS_URL_FHZL NUMBER(1) DEFAULT NULL,
                                   ALGORITHM_STATE NUMBER(1) DEFAULT NULL,
                                   ARRIVE_DRAUGHT NUMBER(3,2) DEFAULT NULL,
                                   LEAVE_DRAUGHT NUMBER(3,2) DEFAULT NULL,
                                   EXPECT_ARRIVE_TIME TIMESTAMP DEFAULT NULL,
                                   WAIT_CONSTANT_DRAINAGE_TIME TIMESTAMP DEFAULT NULL,
                                   UNBERTHING_DRAFT NUMBER(3,2) DEFAULT NULL,
                                   CREATE_DATE TIMESTAMP DEFAULT NULL,
                                   REVISE_DATE TIMESTAMP DEFAULT NULL,
                                   IGTOS_SHIPCYCLE_ID VARCHAR2(36) DEFAULT NULL,
                                   IS_SPECIAL_WORK VARCHAR2(36) DEFAULT NULL,
                                   MACHINE_TYPE_CODE VARCHAR2(36) DEFAULT NULL,
                                   MACHINE_COUNT NUMBER(10,0) DEFAULT NULL,
                                   WATER_RATIO NUMBER(2,2) DEFAULT NULL,
                                   BERTH_NO VARCHAR2(32) DEFAULT NULL,
                                   EXPECT_BERTH_TIME TIMESTAMP DEFAULT NULL,
                                   START_TIME TIMESTAMP DEFAULT NULL,
                                   END_TIME TIMESTAMP DEFAULT NULL,
                                   EXPECT_LEAVE_TIME TIMESTAMP DEFAULT NULL,
                                   BOLLARD_FORWARD VARCHAR2(10) DEFAULT NULL,
                                   BOLLARD_BEHIND VARCHAR2(10) DEFAULT NULL,
                                   PRIMARY KEY (ID)
);

--船型
CREATE TABLE ALG_SHIP_TYPE (
                               ID VARCHAR2(36) NOT NULL,
                               SHIP_TYPE VARCHAR2(63) DEFAULT NULL,
                               STAGE_START NUMBER(3,1) DEFAULT NULL,
                               STAGE_END NUMBER(3,1) DEFAULT NULL,
                               CAPACITY_PER_HOUR NUMBER(7,2) DEFAULT NULL,
                               CLEAN_MACHINE VARCHAR2(31) DEFAULT NULL,
                               PRIMARY KEY (ID)
);

--船舶作业信息详细信息
CREATE TABLE ALG_SHIP_WORKING_INFO_DETAIL (
                                              ID VARCHAR2(32) NOT NULL,
                                              SHIP_FORECAST_ID VARCHAR2(36) DEFAULT NULL,
                                              TICKET_NUM NUMBER(10,0) DEFAULT NULL,
                                              WATER_ESTIMATION_TIME VARCHAR2(8) DEFAULT NULL,
                                              WATER_RATIO NUMBER(2,2) DEFAULT NULL,
                                              CREATE_DATE TIMESTAMP DEFAULT NULL,
                                              REVISE_DATE TIMESTAMP DEFAULT NULL,
                                              PRIMARY KEY (ID)
);

--船舶舱口卸序
CREATE TABLE ALG_SHIP_WORKING_SEQUENCE (
                                           ID VARCHAR2(36) NOT NULL,
                                           SHIP_FORECAST_ID VARCHAR2(36) DEFAULT NULL,
                                           TICKET_NUM NUMBER(10,0) DEFAULT NULL,
                                           CARGO_NAME VARCHAR2(20) DEFAULT NULL,
                                           SHIP_CABIN_NO VARCHAR2(60) DEFAULT NULL,
                                           CREATE_DATE TIMESTAMP DEFAULT NULL,
                                           REVISE_DATE TIMESTAMP DEFAULT NULL,
                                           TOTAL_WEIGHT NUMBER(7,2) DEFAULT NULL,
                                           SINGLE_SHIP_WORK_HOUR_QTY NUMBER(6,2) DEFAULT NULL,
                                           MACHINE_COUNT NUMBER(2,0) DEFAULT NULL,
                                           MACHINE_TYPE_CODE VARCHAR2(32) DEFAULT NULL,
                                           WORK_HOURS NUMBER(2,1) DEFAULT NULL,
                                           PRIMARY KEY (ID)
);

--潮汐信息
CREATE TABLE ALG_TIDE (
                          ID VARCHAR2(32) NOT NULL,
                          TIDE_DATE DATE DEFAULT NULL,
                          TIDE_HEIGHT1 NUMBER(2,2) DEFAULT NULL,
                          TIDE_HEIGHT2 NUMBER(2,2) DEFAULT NULL,
                          TIDE_HEIGHT3 NUMBER(2,2) DEFAULT NULL,
                          TIDE_HEIGHT4 NUMBER(2,2) DEFAULT NULL,
                          TIDE_TIME1 TIMESTAMP DEFAULT NULL,
                          TIDE_TIME2 TIMESTAMP DEFAULT NULL,
                          TIDE_TIME3 TIMESTAMP DEFAULT NULL,
                          TIDE_TIME4 TIMESTAMP DEFAULT NULL,
                          H0 NUMBER(2,2) DEFAULT NULL,
                          H1 NUMBER(2,2) DEFAULT NULL,
                          H2 NUMBER(2,2) DEFAULT NULL,
                          H3 NUMBER(2,2) DEFAULT NULL,
                          H4 NUMBER(2,2) DEFAULT NULL,
                          H5 NUMBER(2,2) DEFAULT NULL,
                          H6 NUMBER(2,2) DEFAULT NULL,
                          H7 NUMBER(2,2) DEFAULT NULL,
                          H8 NUMBER(2,2) DEFAULT NULL,
                          H9 NUMBER(2,2) DEFAULT NULL,
                          H10 NUMBER(2,2) DEFAULT NULL,
                          H11 NUMBER(2,2) DEFAULT NULL,
                          H12 NUMBER(2,2) DEFAULT NULL,
                          H13 NUMBER(2,2) DEFAULT NULL,
                          H14 NUMBER(2,2) DEFAULT NULL,
                          H15 NUMBER(2,2) DEFAULT NULL,
                          H16 NUMBER(2,2) DEFAULT NULL,
                          H17 NUMBER(2,2) DEFAULT NULL,
                          H18 NUMBER(2,2) DEFAULT NULL,
                          H19 NUMBER(2,2) DEFAULT NULL,
                          H20 NUMBER(2,2) DEFAULT NULL,
                          H21 NUMBER(2,2) DEFAULT NULL,
                          H22 NUMBER(2,2) DEFAULT NULL,
                          H23 NUMBER(2,2) DEFAULT NULL,
                          CREATE_TIME TIMESTAMP DEFAULT NULL,
                          REVISE_TIME TIMESTAMP DEFAULT NULL,
                          PRIMARY KEY (ID)
);
