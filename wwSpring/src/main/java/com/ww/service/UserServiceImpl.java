package com.ww.service;

import com.ww.dao.UserDao;

public class UserServiceImpl implements UserService {
    /**
     * 依赖对象，注入
     */
    UserDao dao;

//    public UserServiceImpl(UserDao dao) {
//        this.dao = dao;
//    }

    @Override
    public void find() {
        System.out.println("find=========");
        dao.query();
    }

//    public void setDao(UserDao dao) {
//        this.dao = dao;
//    }
}
