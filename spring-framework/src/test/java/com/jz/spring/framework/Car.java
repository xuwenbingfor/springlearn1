package com.jz.spring.framework;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author xuwenbingfor
 * @version 2021/5/12 22:56
 * @description
 */
@Slf4j
@Data
public class Car {
    private String name = "default";
    @Autowired
    private Wheel wheel;

    public void showInfo() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("car name: " + this.name + "; ");
        stringBuffer.append("wheel name: " + this.wheel.getName());
        log.info("{}", stringBuffer);
    }
}
