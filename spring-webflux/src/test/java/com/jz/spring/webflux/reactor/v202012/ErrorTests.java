package com.jz.spring.webflux.reactor.v202012;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

/**
 * @author xuwenbingfor
 * @version 2020/12/7 22:07
 * @description
 */
@Slf4j
public class ErrorTests {
    @Test
    public void test3() {
        Flux<Integer> s = Flux.just(1, 3, 0, 9)
                .map(o -> 99 / o)
                .onErrorMap(e -> new RuntimeException());
        // CAUGHT java.lang.ArithmeticException
        // CAUGHT java.lang.RuntimeException
        s.subscribe(value -> System.out.println("RECEIVED " + value),
                error -> System.err.println("CAUGHT " + error.getClass().getName())
        );
    }

    @Test
    public void test2() {
        Flux<Integer> s = Flux.just(1, 3, 0, 9)
                .map(o -> 99 / o)
                .onErrorResume(e -> Flux.just(1000));
        s.subscribe(value -> System.out.println("RECEIVED " + value),
                error -> System.err.println("CAUGHT " + error)
        );
    }

    @Test
    public void test1() {
        Flux<Integer> s = Flux.just(1, 3, 0, 9)
                .map(o -> 99 / o)
                .onErrorReturn(100);
        s.subscribe(value -> System.out.println("RECEIVED " + value),
                error -> System.err.println("CAUGHT " + error)
        );
    }
}
