package com.example.test_dy_compiler;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created on 2019/10/10  10:56
 * function : 加载class 到虚拟机中
 * <p>
 * Android的Dalvik/ART虚拟机如同标准JAVA的JVM虚拟机一样，在运行程序时首先需要将对应的类加载到内存中。
 * <p>
 * 个运行的Android应用至少有2个ClassLoader: dalvik.system.PathClassLoader ;  java.lang.BootClassLoader
 * <p>
 * 同一个Class = 相同的 ClassName + PackageName + ClassLoader
 * <p>
 * 双亲模型加载class,注意:子Loader加载的class,通过父 Loader 是无法查到的
 *
 * @author mnlin
 */
public class SimulateLoadProcess {
    public static void main(String[] args) {
        // 获取当前类的 class-loader
        ClassLoader parentLoader = SimulateLoadProcess.class.getClassLoader();
        MyClassLoader childLoader = new MyClassLoader(parentLoader);
        try {
            childLoader.loadClass("com.example.test_dy_compiler.TestOneOne");
            parentLoader.loadClass("com.example.test_dy_compiler.TestOneOne");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            Class a = Class.forName("com.example.test_dy_compiler.TestOne",true,childLoader);
            Class b = Class.forName("com.example.test_dy_compiler.TestOne",true,parentLoader);

            // false
            System.out.println(a == b);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println();
    }
}

/**
 * 自定义class-loader
 * <p>
 * 指定父 loader
 */
class MyClassLoader extends ClassLoader {
    MyClassLoader(ClassLoader classLoader) {
        super(classLoader);
    }

    @Override
    protected Class<?> findClass(String s) throws ClassNotFoundException {
        try {
            byte[] bytes = Files.readAllBytes(Paths.get(new URI("file:///C:/android_space/SimulateDialog" + "/test_dy_compiler/out/com/example/test_dy_compiler/TestOne.class")));
            return defineClass("com.example.test_dy_compiler.TestOne", bytes, 0, bytes.length);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        return null;
    }
}