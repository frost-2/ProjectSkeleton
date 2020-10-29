package com.frost2.skeleton.common.aspect;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * @author 陈伟平
 * @date 2020-10-27 11:06:30
 */
@Aspect
@Component
public class LogAspect {

    //定义一个切入点
    @Pointcut("execution(public * com.frost2.skeleton.controller.thread.SingletonController.testVar(..))")
    private void getRecentTriggerTime() {
    }

    @Before("getRecentTriggerTime()")
    public void before1() {
        System.out.println("------------------testVar 方法执行前-------------------");
    }

    @After("getRecentTriggerTime()")
    public void after() {
        System.out.println("------------------testVar 方法执行后-------------------");
    }

}
