package com.yun.rule.enums;

/**
 * @author 琪
 * 2023/7/26
 */
public enum RuleTypeEnum {

    IDCARD("IDCard", "身份证号","String", String.class),
    BANKCARDID("bankCardId","银行卡号","String",String.class),
    BANKCARDTYPE("bankCardType","银行卡类型","String",String.class),
    ISSUINGBANK("issuingBank","发卡银行","String",String.class),
    ADDRESS("address","地址","String",String.class),
    ZIP("zip","邮编","String",String.class),
    ;

    private final String code;
    private final String value;
    private final String type;
    private final Class<?> classType;

    public static RuleTypeEnum getByCode(String code) {
        for(RuleTypeEnum e : RuleTypeEnum.values()){
            if(e.code.equals(code)){
                return e;
            }
        }
        return null;
    }

    RuleTypeEnum(String code, String value, String type, Class<?> classType) {
        this.code = code;
        this.value = value;
        this.type = type;
        this.classType = classType;
    }

    public String getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }

    public String getType() {
        return type;
    }

    public Class<?> getClassType() {
        return classType;
    }
}
