package com.jz.spring.webflux.reactor;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.util.function.Tuple2;

import java.time.Duration;

/**
 * @author xuwenbingfor
 * @version 2020/6/27 17:11
 * @description
 */
@Slf4j
public class ComposeTests {
    @Test
    public void mergeWith() {
        Flux<String> flux1 = Flux.just("a", "b", "c");
        Flux<String> flux2 = Flux.just("A", "B", "C");
        Flux<String> flux = flux1.mergeWith(flux2);
        flux.subscribe((o) -> {
            log.info("element:{}", o);
        });
        flux2.mergeWith(flux1).subscribe((o) -> {
            log.info("element:{}", o);
        });
    }

    @Test
    public void zip() {
        Flux<String> flux1 = Flux.just("a", "b", "c");
        Flux<String> flux2 = Flux.just("A", "B", "C");
        Flux<Tuple2<String, String>> flux = Flux.zip(flux1, flux2);
        flux.subscribe((o -> {
            log.info("T1:{}", o.getT1());
            log.info("T2:{}", o.getT2());
        }));
    }

    @Test
    public void first() {
        Flux<String> flux1 = Flux.just("a", "b", "c").delaySubscription(Duration.ofSeconds(1));
        Flux<String> flux2 = Flux.just("A", "B", "C");
        Flux.first(flux1, flux2).subscribe(o -> {
            log.info("element:{}", o);
        });
    }
}
