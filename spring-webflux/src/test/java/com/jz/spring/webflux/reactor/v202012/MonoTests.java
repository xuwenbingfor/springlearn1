package com.jz.spring.webflux.reactor.v202012;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;

/**
 * @author xuwenbingfor
 * @version 2020/12/6 10:24
 * @description
 */
@Slf4j
public class MonoTests {
    @Test
    public void test1() throws InterruptedException {
        final Mono<String> mono = Mono.just("hello ");

        Thread t = new Thread(() -> mono
                .map(msg -> {
                    System.out.println(Thread.currentThread().getName());
                    return msg + "thread ";
                })
                .subscribe(v ->
                        System.out.println(v + Thread.currentThread().getName())
                )
        );
        t.start();
        t.join();
    }
}
