package com.qf.spring.aop.service;

public interface UserService {

    /**
     * 保存用户方法
     * @return
     */
    int save();

    /**
     * 更新信息方法,目前只作为测试
     * @param info
     * @return
     */
    int update(String info);
}
