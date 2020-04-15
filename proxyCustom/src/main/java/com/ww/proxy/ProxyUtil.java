package com.ww.proxy;

import com.ww.dao.CustomInvocationHandler;
import com.ww.dao.UserDaoImpl;
import org.omg.CORBA.StringHolder;

import javax.tools.JavaCompiler;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

public class ProxyUtil {
    /**
     * 模拟动态代理，用字符串的形式新建一个java文件并编译
     *
     * @param
     * @return
     */
    public static Object newInstance(Class targetInterface, CustomInvocationHandler h) {
        Object proxy = null;
        //接口中定义的方法
        Method methods[] = targetInterface.getDeclaredMethods();
        //换行符
        String line = "\n";
        //缩进
        String tab = "\t";
        String infName = targetInterface.getSimpleName();
        String content = "";
        String packageContent = "package com.google;" + line;
        String importContent = "import " + targetInterface.getName() + ";" + line;
        String importHandler = "import com.ww.dao.CustomInvocationHandler;" + line;
        String importMethod = "import " + Method.class.getName() + ";" + line;
        String importException = "import java.lang.Exception;";
        String clazzFirstLineContent = "public class $Proxy implements " + infName + "{" + line;
        String filedContent = tab + "private CustomInvocationHandler h;" + line;
        //构造函数把目标对象传入
        String constructorContent = tab + "public $Proxy (CustomInvocationHandler h){" + line
                + tab + tab + "this.h =h;"
                + line + tab + "}" + line;
        String methodContent = "";
        for (Method method : methods) {
            String returnTypeName = method.getReturnType().getSimpleName();
            String methodName = method.getName();
            // Sting.class
            Class args[] = method.getParameterTypes();
            String argsContent = "";
            String paramsContent = "";
            int flag = 0;
            for (Class arg : args) {
                String temp = arg.getSimpleName();
                //String
                //String p0,Sting p1,
                //形参
                argsContent += temp + " p" + flag + ",";
                //实参
                paramsContent += "p" + flag + ",";
                flag++;
            }
            if (argsContent.length() > 0) {
                argsContent = argsContent.substring(0, argsContent.lastIndexOf(",") - 1);
                paramsContent = paramsContent.substring(0, paramsContent.lastIndexOf(",") - 1);
            }

            methodContent += tab + "public " + returnTypeName + " " + methodName + "(" + argsContent + ") throws Exception{" + line
                    + tab + tab + "Method method = Class.forName(" + "\"" + targetInterface.getName() + "\"" + ").getDeclaredMethod(" + "\"" + methodName + "\"" + ");" + line;
            if (returnTypeName.equals("void")) {
                methodContent += tab + tab + "h.invoke(method);" + line + tab + "}" + line;
            } else {
                methodContent += tab + tab + "return (" + returnTypeName + ")h.invoke(method);" + line + tab + "}" + line;
            }

//            if (returnTypeName.equals("void")) {
//                methodContent += tab + tab + "target." + methodName + "(" + paramsContent + ");" + line
//                        + tab + "}" + line;
//            } else {
//                //不是void返回类型，就直接返回目标对象的方法
//                methodContent += tab + tab + "return target." + methodName + "(" + paramsContent + ");" + line
//                        + tab + "}" + line;
//            }
        }

        content = packageContent + importException + importContent + importHandler + importMethod + clazzFirstLineContent + filedContent + constructorContent + methodContent + "}";

        File file = new File("d:\\com\\google\\$Proxy.java");
        try {
            if (!file.exists()) {
                file.createNewFile();
            }

            FileWriter fw = new FileWriter(file);
            fw.write(content);
            fw.flush();
            fw.close();


            JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

            StandardJavaFileManager fileMgr = compiler.getStandardFileManager(null, null, null);
            Iterable units = fileMgr.getJavaFileObjects(file);

            JavaCompiler.CompilationTask t = compiler.getTask(null, fileMgr, null, null, null, units);
            t.call();
            fileMgr.close();

            URL[] urls = new URL[]{new URL("file:D:\\\\")};
            URLClassLoader urlClassLoader = new URLClassLoader(urls);
            Class clazz = urlClassLoader.loadClass("com.google.$Proxy");

            Constructor constructor = clazz.getConstructor(CustomInvocationHandler.class);
            proxy = constructor.newInstance(h);
            //clazz.newInstance();
            //Class.forName()
        } catch (Exception e) {
            e.printStackTrace();
        }
        return proxy;
    }
}
