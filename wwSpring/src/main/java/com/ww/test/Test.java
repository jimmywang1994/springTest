package com.ww.test;

import com.ww.service.UserService;
import org.spring.util.AnnotationConfigApplicationContext;
import org.spring.util.BeanFactory;

public class Test {
    public static void main(String[] args) {
//        BeanFactory beanFactory=new BeanFactory("spring.xml");
//        UserService userService= (UserService) beanFactory.getBean("service");
//        userService.find();
        AnnotationConfigApplicationContext annotationConfigApplicationContext=new AnnotationConfigApplicationContext();
        annotationConfigApplicationContext.scan("com.ww.dao");
    }
}
