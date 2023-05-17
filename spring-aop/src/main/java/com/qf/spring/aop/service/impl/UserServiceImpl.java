package com.qf.spring.aop.service.impl;

import com.qf.spring.aop.service.UserService;

public class UserServiceImpl implements UserService {
    @Override
    public int save() {
        System.out.println("新增数据成功");
        // int a = 1/0;
        return 1;
    }

    @Override
    public int update(String info) {
        System.out.println("正在更新用户");
        return 1;
    }

}
