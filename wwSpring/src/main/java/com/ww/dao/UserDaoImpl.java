package com.ww.dao;

import org.spring.annotation.WWService;

@WWService("userDao")
public class UserDaoImpl implements UserDao {
    @Override
    public void query() {
        System.out.println("query=========");
    }
}
