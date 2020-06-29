package com.jz.spring.webflux.reactor;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Subscription;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @author xuwenbingfor
 * @version 2020/6/29 23:21
 * @description
 */
@Slf4j
public class FluxTests {
    @Test
    public void testBackpressure() {
        Flux.range(1, 6)    // 1
                .doOnRequest(n -> System.out.println("Request " + n + " values..."))    // 2
                .subscribe(new BaseSubscriber<Integer>() {  // 3
                    @Override
                    protected void hookOnSubscribe(Subscription subscription) { // 4
                        System.out.println("Subscribed and make a request...");
                        request(1); // 5
                    }

                    @Override
                    protected void hookOnNext(Integer value) {  // 6
                        try {
                            TimeUnit.SECONDS.sleep(3);  // 7
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        System.out.println("Get value [" + value + "]");    // 8
                        request(1); // 9
                    }
                });
    }

    @Test
    public void test1() {
        Flux.range(1, 6)
                .map(i -> 10 / (i - 3))
                .onErrorReturn(0)   // 1
                .map(i -> i * i)
                .subscribe(System.out::println, System.err::println);

    }

    @Test
    public void test2() {
        int nextInt = new Random().nextInt(6);
        log.info("{}", nextInt);
    }

    @Test
    public void test3() {
        Flux.range(1, 6)
                .map(i -> 10 / (i - 3))
//                .onErrorResume(e -> Mono.just(new Random().nextInt(6))) // 提供新的数据流
                .onErrorResume(e -> Flux.just(1, 2, 3))
                .map(i -> i * i)
                .subscribe(System.out::println, System.err::println);
    }
}
