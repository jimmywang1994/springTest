package com.ww.test;

import com.ww.dao.UserDao;
import com.ww.dao.UserDaoImpl;
import com.ww.proxy.ProxyUtil;
import com.ww.util.TestCustomHandler;

public class Test {
    public static void main(String[] args) throws Exception{
        //第一版模拟动态代理的调用方式
        //UserDao dao = (UserDao) ProxyUtil.newInstance(new UserDaoImpl());
        //第二版动态代理
        UserDao proxy= (UserDao) ProxyUtil.newInstance(UserDao.class,new TestCustomHandler(new UserDaoImpl()));
        proxy.query();
        System.out.println(proxy.proxy("xxtt"));
//        dao.query();
//        System.out.println(dao.proxy("tyty"));
    }
}
