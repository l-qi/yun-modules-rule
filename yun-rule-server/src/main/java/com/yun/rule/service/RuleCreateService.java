package com.yun.rule.service;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 * @author 琪
 * 2023/7/29
 */
public interface RuleCreateService {

    void createRule(String className, String sourceCode) throws IOException;

    void createRule(File file) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException;
}
