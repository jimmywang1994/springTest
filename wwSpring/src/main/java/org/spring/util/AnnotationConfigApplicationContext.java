package org.spring.util;

import org.spring.annotation.WWService;

import java.io.File;

/**
 * 模拟spring的注解注入方法
 */
public class AnnotationConfigApplicationContext {
    public void scan(String basePackage) {
        String rootPath = this.getClass().getResource("/").getPath();
        String basePackagePath = basePackage.replaceAll("\\.", "\\\\");
        File file = new File(rootPath + "//" + basePackagePath);
        String[] names = file.list();
        for (String name : names) {
            name = name.replaceAll(".class", "");
            try {
                Class<?> clazz = Class.forName(basePackage + "." + name);
                //类上面有没有加自定义的注解
                if (clazz.isAnnotationPresent(WWService.class)) {
                    WWService wService = clazz.getAnnotation(WWService.class);
                    System.out.println(wService.value());
                    System.out.println(clazz.newInstance());
                }
                //判断是否属于@service,@xxx
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
        }
    }
}
