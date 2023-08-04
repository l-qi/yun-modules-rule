package com.yun.rule.test;

import com.yun.rule.service.impl.RuleCreateServiceImpl;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 * @author 琪
 * 2023/7/31
 */
@SpringBootTest
public class RuleTest {


    public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, IOException, IllegalAccessException, InvocationTargetException {
        createRuleByString();
//        createRuleFile();
    }

    private static void createRuleFile() {

    }

    private static void createRuleByString() {
        RuleCreateServiceImpl ruleCreateService = new RuleCreateServiceImpl();
        ruleCreateService.createRule("HelloWorld",
                "package com.yun.rule.custom;" +
                        "public class HelloWorld {" +
                        " public static void text() {" +
                        " int a = 1;" +
                        " int b = 2;" +
                        "System.out.println(a+b);" +
                        "int sum = 0;" +
                        "for(int i=0;i<100;i++){" +
                        "sum+=i;" +
                        "}" +
                        "System.out.println(sum);" +
                        "System.out.println(\"Hello World!\");" +
                        " } " +
                        "public static void test(){" +
                        "System.out.println(\"test11111111\");}" +
                        "}");
    }


}
