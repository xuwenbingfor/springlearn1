package com.jz.spring.framework;

import lombok.extern.slf4j.Slf4j;

/**
 * @author xuwenbingfor
 * @version 2021/5/16 22:53
 * @description
 */
@Slf4j
public class Plane implements Flyable {
    public void fly() {
        log.info("plane is flying1");
    }
}
