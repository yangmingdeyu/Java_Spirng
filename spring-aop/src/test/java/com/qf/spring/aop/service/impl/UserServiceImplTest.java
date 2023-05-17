package com.qf.spring.aop.service.impl;

import com.qf.spring.aop.service.UserService;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import static org.junit.Assert.*;

public class UserServiceImplTest {

    @Test
    public void save() {
        //不使用.var自动补全.ctrl+alt +v
        ApplicationContext context = new ClassPathXmlApplicationContext("/beans.xml");
        UserService us = context.getBean(UserService.class);
        int save = us.save();
        System.out.println(save);
    }

    @Test
    public void save02(){
        ApplicationContext context = new ClassPathXmlApplicationContext("/aspectj-beas.xml");
        UserService us = context.getBean(UserService.class);
        us.save();
    }

    /**
     * 测试多个方法的aop调用
     */
    @Test
    public void save03(){
        ApplicationContext context = new ClassPathXmlApplicationContext("/aspectj-beas.xml");
        UserService us = context.getBean(UserService.class);
        us.save();
        int update = us.update("你好");
        System.out.println(update);
    }

}