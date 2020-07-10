package com.jz.spring.webflux.reactor;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

/**
 * @author xuwenbingfor
 * @version 2020/6/27 16:36
 * @description
 */
@Slf4j
public class CreateTests {
    @Test
    public void test10() {
        Flux<String> flux = Flux.generate(
                () -> 0,
                (state, sink) -> {
                    sink.next("3 x " + state + " = " + 3 * state);
                    if (state == 10) sink.complete();
                    return state + 1;
                },
                (state) -> System.out.println("state: " + state));
        flux.subscribe(s -> log.info("{}", s));
    }

    @Test
    public void just() {
        Flux<String> just = Flux.just("a", "b", "c");
//        just.subscribe(o -> {
//            log.info("element:{}", o);
//        });
        StepVerifier.create(just)
                .expectNext("a")
                .expectNext("b")
                .expectNext("c")
                .verifyComplete();
    }

    @Test
    public void fromIterable() {
        ArrayList<String> arrayList = Lists.newArrayList("a", "b", "c");
        Flux<String> stringFlux = Flux.fromIterable(arrayList);
        StepVerifier.create(stringFlux)
                .expectNext("a")
                .expectNext("b")
                .expectNext("c")
                .verifyComplete();
    }

    @Test
    public void fromStream() {
        Stream<String> stream = Stream.of("a", "b", "c");
        Flux<String> stringFlux = Flux.fromStream(stream);
        StepVerifier.create(stringFlux)
                .expectNext("a")
                .expectNext("b")
                .expectNext("c")
                .verifyComplete();
    }

    @Test
    public void range() {
        Flux<Integer> range = Flux.range(1, 5);
        range.subscribe((o) -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info("element:{}", o);
        });
    }

    @Test
    public void interval() throws InterruptedException {
        Flux<Long> take = Flux.interval(Duration.ofSeconds(1)).take(5);
        take.subscribe((o) -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info("element:{}", o);
        });
        TimeUnit.SECONDS.sleep(10);
//        StepVerifier.create(take)
//                .expectNext(0L)
//                .expectNext(1L)
//                .expectNext(2L)
//                .expectNext(3L)
//                .expectNext(4L)
//                .verifyComplete();
    }
}
