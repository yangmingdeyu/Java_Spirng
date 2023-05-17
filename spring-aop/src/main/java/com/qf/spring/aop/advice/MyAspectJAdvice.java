package com.qf.spring.aop.advice;

import org.aspectj.lang.ProceedingJoinPoint;

/**
 * 就是一个普通的类,我想让这个类作为我的增强类
 */
public class MyAspectJAdvice {

    public void before(){
        System.out.println("前置增强代码");
    }

    public void after(){
        System.out.println("最终增强代码, 类似于finally,不管目标方法有没有异常都要执行");
    }

    public Object around(ProceedingJoinPoint jp) throws Throwable {
        System.out.println("环绕增强前面的代码");
        //让目标方法继续执行
        Object result = jp.proceed();
        System.out.println("环绕增强后面的代码");
        return result;
    }

    public void throwing(Exception e){
        System.out.println("异常增强抛出执行代码,只有在目标方法抛出异常时,才能执行,异常信息是"+e.getMessage());

    }

    public void afterReturning(Object resut){
        System.out.println("后置增强,返回值是"+resut);
    }
}
