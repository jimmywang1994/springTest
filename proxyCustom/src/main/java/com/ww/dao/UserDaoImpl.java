package com.ww.dao;

public class UserDaoImpl implements UserDao {
    @Override
    public void query() throws Exception{
        System.out.println("假装查询数据库============");
    }

    @Override
    public String proxy(String name) throws Exception{
        System.out.println("aaaaaaa=======" + name);
        return name;
    }
}
