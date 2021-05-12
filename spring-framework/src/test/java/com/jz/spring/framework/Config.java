package com.jz.spring.framework;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author xuwenbingfor
 * @version 2021/5/12 23:39
 * @description
 */
@Slf4j
@Configuration
public class Config {
    @Bean
    public Wheel wheel() {
        log.info("init wheel");
        return new Wheel();
    }
}
