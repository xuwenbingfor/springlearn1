package com.jz.spring.framework;

import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author xuwenbingfor
 * @version 2021/5/16 22:51
 * @description
 */
public class AopTest {
    @Test
    public void test1() {
        ApplicationContext ac = new ClassPathXmlApplicationContext("classpath:aop/applicationContext.xml");
        Flyable flyable = (Flyable) ac.getBean("wheel");
        flyable.fly();
    }
}
