package com.yun.rule.enums;

/**
 * @author 琪
 * 2023/7/31
 * 银行卡类型
 */
public enum BankCardType {
    ;

    //银行卡号第13位
    private final Character code;
    //银行卡类型
    private final String type;

    BankCardType(Character code, String type) {
        this.code = code;
        this.type = type;
    }

    public Character getCode() {
        return code;
    }

    public String getType() {
        return type;
    }
}
