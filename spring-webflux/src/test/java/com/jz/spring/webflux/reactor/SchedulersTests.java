package com.jz.spring.webflux.reactor;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

/**
 * @author xuwenbingfor
 * @version 2020/6/29 22:41
 * @description
 */
@Slf4j
public class SchedulersTests {
    @Test
    public void test1() {
        Flux.just("tom")
                .map(s -> {
                    System.out.println("[map] Thread name: " + Thread.currentThread().getName());
                    return s.concat("@mail.com");
                })
                .publishOn(Schedulers.newElastic("thread-publishOn"))
                .filter(s -> {
                    System.out.println("[filter] Thread name: " + Thread.currentThread().getName());
                    return s.startsWith("t");
                })
//                .publishOn(Schedulers.newElastic("thread-publishOn-add"))
                .subscribeOn(Schedulers.newElastic("thread-subscribeOn"))
                .subscribe(s -> {
                    System.out.println("[subscribe] Thread name: " + Thread.currentThread().getName());
                    System.out.println(s);
                });
    }
}
