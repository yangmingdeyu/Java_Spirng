package com.qf.spring.aop.advice;

import org.springframework.aop.MethodBeforeAdvice;

import java.lang.reflect.Method;

/**
 * 基于Schema-based的aop实现方式
 * 测试前置增强
 *
 */
public class MyBeforeAdvice implements MethodBeforeAdvice {

    @Override
    public void before(Method method, Object[] objects, Object o) throws Throwable {
        System.out.println("前置通知正在执行");
    }
}
