package com.yun.rule.service.impl;

import com.yun.rule.model.Value;
import com.yun.rule.service.RuleEngine;
import com.yun.rule.service.RuleEngine;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * @author 琪
 * 2023/7/31
 */
public class RuleEngineListImpl implements RuleEngine <List<Map<String, Value>>> {

    @Autowired
    private RuleEngine ruleEngine;

    @Override
    public void ruleEngine(List<Map<String, Value>> dataList) {
        //创建线程池
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(3, 10, 30, TimeUnit.SECONDS, new SynchronousQueue<>());
        //向线程池提交任务 无返回值
        for (Map<String, Value> valueMap : dataList) {
            threadPoolExecutor.execute(() -> {
                ruleEngine.ruleEngine(valueMap);
            });
        }
        //关闭线程池
        threadPoolExecutor.shutdown();
    }

    public RuleEngine getRuleEngineRows() {
        return ruleEngine;
    }
}
