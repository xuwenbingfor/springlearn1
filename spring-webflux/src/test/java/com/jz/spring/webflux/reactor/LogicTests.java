package com.jz.spring.webflux.reactor;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author xuwenbingfor
 * @version 2020/6/27 21:48
 * @description
 */
@Slf4j
public class LogicTests {
    @Test
    public void all() {
        Flux<String> flux1 = Flux.just("apple", "banana", "city", "apply");
        Mono<Boolean> mono = flux1.all(o -> o.contains("a"));
        mono.subscribe(o -> log.info("boolean:{}", o));
    }

    @Test
    public void any() {
        Flux<String> flux1 = Flux.just("apple", "banana", "city", "apply");
        Mono<Boolean> mono = flux1.any(o -> o.contains("a"));
        mono.subscribe(o -> log.info("boolean:{}", o));
    }
}
