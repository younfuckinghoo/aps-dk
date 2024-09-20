package com.aps.yinghai.enums;

import com.aps.yinghai.exception.NotFindEnumException;

public enum MachineTypeEnum {

    //    ME:门机，SR：流程、堆取料机，ZC：装载机,SL：装船机,WJ：挖掘机,ZH：卸船机
    ME("ME", "门机"),
    SR("SR", "堆取料机"),
    ZC("ZC", "装载机"),
    SL("SL", "装船机"),
    WJ("WJ", "挖掘机"),
    ZH("ZH", "卸船机")
    ;


    private String code;
    private String name;

    MachineTypeEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }


    public String getCode() {
        return code;
    }


    public String getName() {
        return name;
    }

    public static MachineTypeEnum getInstanceByCode(String code) {
        MachineTypeEnum[] values = MachineTypeEnum.values();
        for (MachineTypeEnum value : values) {
            if (value.getCode().equals(code))
                return value;
        }
        throw new NotFindEnumException();
    }
}
