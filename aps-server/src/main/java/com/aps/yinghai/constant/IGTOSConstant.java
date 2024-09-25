package com.aps.yinghai.constant;

public interface IGTOSConstant {

    static interface ShipStatusConst{
        // 预排：7
        Integer TO_BE_PRE_PLAN = 7;
    }

    // 已删除
    int DELETE_YES = 1;
    // 未删除
    int DELETE_NO = 0;

    interface CargoType{
        int THIRD_CLASS = 3;
    }


//    interface TradeType{
//        /**
//         * 内贸
//         */
//        int IN = 1;
//        /**
//         * 外贸
//         */
//        int OUT = 2;
//    }


}
