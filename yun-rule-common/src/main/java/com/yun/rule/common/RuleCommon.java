package com.yun.rule.common;


import com.alibaba.csp.sentinel.util.StringUtil;


/**
 * @author 琪
 * 2023/7/26
 */
public class RuleCommon {

    /**
     * 对字符串进行正则校验
     * @param value 需要校验的字符串
     * @param code 正则编码
     * @return boolean
     */
    public static boolean checkPattern(String value, String code) {
        if (StringUtil.isEmpty(value) || StringUtil.isEmpty(code)){
            return false;
        }
        return value.matches(code);
    }

    /**
     * 校验身份证号
     * @param IDNumber 身份证号
     * @return boolean
     */
    public static boolean isIDNumber(String IDNumber) {
        if (IDNumber == null || "".equals(IDNumber)) {
            return false;
        }
        // 定义判别用户身份证号的正则表达式（15位或者18位，最后一位可以为字母）
        String regularExpression = "(^[1-9]\\d{5}(18|19|20)\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$)|(^[1-9]\\d{5}\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}$)";
        boolean matches = checkPattern(IDNumber,regularExpression);

        //判断第18位校验值
        if (matches) {

            if (IDNumber.length() == 18) {
                try {
                    char[] charArray = IDNumber.toCharArray();
                    //前十七位加权因子
                    int[] idCardWi = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};
                    //这是除以11后，可能产生的11位余数对应的验证码
                    String[] idCardY = {"1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2"};

                    int sum = 0;
                    for (int i = 0; i < idCardWi.length; i++) {
                        int current = Integer.parseInt(String.valueOf(charArray[i]));
                        int count = current * idCardWi[i];
                        sum += count;
                    }

                    char idCardLast = charArray[17];
                    int idCardMod = sum % 11;

                    return idCardY[idCardMod].toUpperCase().equals(String.valueOf(idCardLast).toUpperCase());
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
            }

        }
        return matches;
    }


}
