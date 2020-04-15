package com.ww.app;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

/**
 * 切面
 */
@Component
@Aspect
public class AppAspectJ {
    /**
     * 切点
     */
    @Pointcut("execution(* com.ww.dao..*.*(..))")
    public void pointCut() {

    }

    /**
     * 前置通知
     */
    @Before("pointCut()")
    public void before() {
        System.out.println("before========");
    }
}
