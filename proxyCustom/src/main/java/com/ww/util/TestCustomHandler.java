package com.ww.util;

import com.ww.dao.CustomInvocationHandler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class TestCustomHandler implements CustomInvocationHandler {

    private Object target;

    public TestCustomHandler(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Method method) {
        try {
            return method.invoke(target);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }
}
