package com.yun.rule.service.impl;

import com.alibaba.csp.sentinel.util.StringUtil;
import com.yun.rule.common.RegionZipCommon;
import com.yun.rule.common.RuleCommon;
import com.yun.rule.model.Value;
import com.yun.rule.service.RuleEngine;
import com.yun.rule.utils.LuhnUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.util.ObjectUtils;

import java.util.Map;

import static com.yun.rule.enums.RuleTypeEnum.BANKCARDID;
import static com.yun.rule.enums.RuleTypeEnum.IDCARD;

/**
 * @author 琪
 * 2023/7/31
 */
@Log4j2
public class RuleEngineRowsImpl implements RuleEngine<Map<String, Value>> {

    @Override
    public void ruleEngine(Map<String, Value> data) {
        checkIDCard(data);
        checkBankCard(data);
        RegionZipCommon.checkRegionZip(data);
    }

    /**
     * 校验身份证号是否正确
     *
     * @param data 数据
     */
    private void checkIDCard(Map<String, Value> data) {
        Value value = data.get(IDCARD.getCode());
        if (ObjectUtils.isEmpty(value.getVal())) {
            String IDCard = (String) value.getVal();
            if (StringUtil.isNotEmpty(IDCard)) {
                //身份证号不合法直接过滤掉
                if (!RuleCommon.isIDNumber(IDCard)) {
                    value.setVal(null);
                    log.error("身份证号错误 IDCard = {}", IDCard);
                }
            }
        }
    }

    /**
     * 校验银行卡号是否正确
     *
     * @param data 数据
     */
    private void checkBankCard(Map<String, Value> data) {
        Value value = data.get(BANKCARDID.getCode());
        if (ObjectUtils.isEmpty(value.getVal())) {
            String bankCard = (String) value.getVal();
            if (StringUtil.isNotEmpty(bankCard)) {
                //身份证号不合法直接过滤掉
                if (!LuhnUtil.checkString(bankCard)) {
                    value.setVal(null);
                    log.error("银行卡号错误 bankCard = {}", bankCard);
                }
            }
        }
    }


}
