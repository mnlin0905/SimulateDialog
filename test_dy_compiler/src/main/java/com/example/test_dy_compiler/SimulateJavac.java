package com.example.test_dy_compiler;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

/**************************************
 * function : 模拟 javac 由 java源码 生成 class 字节码
 *
 * Created on $DATE$  $TIME$
 * @author $USER$
 **************************************/
public class SimulateJavac {
    public static void main(String[] args) {
        System.out.println(Arrays.asList(args));

        // 等同 javac
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

        // 编译时信息收集
        DiagnosticCollector<JavaFileObject> collector = new DiagnosticCollector<>();

        //文件管理对象
        StandardJavaFileManager manager = compiler.getStandardFileManager(collector, null, null);

        // 编译参数
        List<String> options = Arrays.asList("-d", System.getProperty("user.dir") + "/test_dy_compiler/out", "-sourcepath", "<到何源码指定的非全路径,到某处查找源码>");

        // 源码
        Iterable<? extends JavaFileObject> sourceFiles = manager.getJavaFileObjectsFromFiles(Arrays.asList(new File(System.getProperty("user.dir") + "/test_dy_compiler/src/main/java/com/example/test_dy_compiler/TestOne.java")));

        // 生成编译任务
        JavaCompiler.CompilationTask task = compiler.getTask(null, // 制定目录文件输出位置
                manager, // 文件管理器实例,将java源码转换为getTask所需的编译单元, TODO
                collector, // 编译时信息收集(包括错误警告等)
                options,  // 编译命令选项(同 javac 命令使用时指定的参数信息)
                null, // 类名称 TODO
                sourceFiles // 需要编译的源码,可经由 manager 转为可被getTask使用的编译单元
        );

        // 运行编译任务
        Boolean call = task.call();

        System.out.println("success:" + call);
    }
}
