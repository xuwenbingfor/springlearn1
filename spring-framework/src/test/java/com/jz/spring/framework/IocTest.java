package com.jz.spring.framework;

import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author xuwenbingfor
 * @version 2021/5/12 23:03
 * @description
 */
public class IocTest {
    @Test
    public void test1() {
        ApplicationContext ac = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
        Car car = ac.getBean(Car.class);
        car.showInfo();
    }
}
