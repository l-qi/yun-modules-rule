package com.yun.rule.service.impl;

import com.yun.rule.service.RuleCreateService;

import javax.tools.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author 琪
 * 2023/7/29
 */
public class RuleCreateServiceImpl implements RuleCreateService {

    private final static String REFERENCE = "com.yun.rule.custom.";

    private final static String CLASSESPATH = "yun-rule-server/target/classes";

    private final static String JAVAFILEPATH = "yun-rule-server/src/main/java/com/yun/rule/custom/";

    /**
     * @param className  类名
     * @param sourceCode 代码块
     */
    @Override
    public void createRule(String className, String sourceCode) {
        // 定义Java源代码
        // 将源代码包装成JavaFileObject对象
        JavaFileObject javaFile = new DynamicJavaFileObject(className, sourceCode);

        // 获取Java编译器对象
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

        // 将JavaFileObject对象加入到编译任务中
        List<JavaFileObject> sourceList = new ArrayList<>();
        sourceList.add(javaFile);
        Iterable<? extends JavaFileObject> compilationUnits = sourceList;

        // 初始化DiagnosticCollector，用于收集编译期间的诊断信息
        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();

        // 定义编译参数
        List<String> options = Arrays.asList("-d", CLASSESPATH);

        // 创建编译任务
        JavaCompiler.CompilationTask task = compiler.getTask(null, null, diagnostics, options, null, compilationUnits);

        // 执行编译任务
        boolean success = task.call();

        // 输出编译信息
        for (Diagnostic<? extends JavaFileObject> diagnostic : diagnostics.getDiagnostics()) {
            System.out.println(diagnostic.getMessage(null));
        }

        if (success) {
            // 加载编译后的类
            try {
                Class<?> newClass = Class.forName(REFERENCE + className);
                newClass.getMethod("text").invoke(null);
//                newClass.getMethod("main", String[].class).invoke(null, (Object) null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 实现JavaFileObject对象，用于封装Java源文件。
     */
    static class DynamicJavaFileObject extends SimpleJavaFileObject {

        private String sourceCode;

        private DynamicJavaFileObject(String name, String sourceCode) {
            super(URI.create("string:///" + name.replaceAll("\\.", "/") + JavaFileObject.Kind.SOURCE.extension), JavaFileObject.Kind.SOURCE);
            this.sourceCode = sourceCode;
        }

        @Override
        public CharSequence getCharContent(boolean ignoreEncodingErrors) {
            return sourceCode;
        }
    }

    @Override
    public void createRule(File file) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException {
        // 1.获取JavaCompiler
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

        // 2.获取StandardJavaFileManager
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);

        // 3.创建Java源文件
        File sourceFile = new File(JAVAFILEPATH + "HelloWorld.java");
        FileWriter writer = new FileWriter(sourceFile);
        writer.write("public class HelloWorld { public static void main(String[] args) { System.out.println(\"Hello World!\"); } }");
        writer.close();

        // 4.编译Java源文件
        Iterable<? extends JavaFileObject> compilationUnits = fileManager.getJavaFileObjectsFromFiles(Arrays.asList(sourceFile));
        JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, null, null, null, compilationUnits);
        boolean success = task.call();

        // 5.加载编译后的类
        if (success) {
            URLClassLoader classLoader = new URLClassLoader(new URL[]{new File(".").toURI().toURL()});
            Class<?> clazz = classLoader.loadClass(sourceFile.getName());
            Method method = clazz.getMethod("main", String[].class);
            method.invoke(null, new Object[]{null});
        }
    }
}
