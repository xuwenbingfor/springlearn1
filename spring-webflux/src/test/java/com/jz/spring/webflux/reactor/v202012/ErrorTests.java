package com.jz.spring.webflux.reactor.v202012;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import reactor.core.Exceptions;
import reactor.core.publisher.Flux;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author xuwenbingfor
 * @version 2020/12/7 22:07
 * @description
 */
@Slf4j
public class ErrorTests {
    @Test
    public void test8() {
        AtomicInteger errorCount = new AtomicInteger();
        Flux<String> flux =
                Flux.<String>error(new IllegalArgumentException())
                        .doOnError(e -> {
                            System.out.println(e);
                            errorCount.incrementAndGet();
                        })
                        .retryWhen(Retry.from(companion ->
                                companion.map(rs -> {
                                    log.info("totalRetries:{}", rs.totalRetries());
                                    log.info("totalRetriesInARow:{}", rs.totalRetriesInARow());
                                    if (rs.totalRetries() < 3) return rs.totalRetries();
                                    else throw Exceptions.propagate(rs.failure());
                                })
                        ));
        flux.subscribe(o -> {
                    System.out.println("--> " + o);
                },
                e -> e.printStackTrace());
    }

    @Test
    public void test7() {
        Flux.just(1)
                .take(3)
                .subscribe(System.out::println);
    }

    @Test
    public void test6() {
        Flux<String> flux = Flux
                .<String>error(new IllegalArgumentException())
                .doOnError(System.out::println)
                .retryWhen(Retry.from(companion -> {
                            return companion.take(3);
                        }
                ));
        flux.subscribe(o -> {
                    System.out.println("--> " + o);
                },
                e -> e.printStackTrace());
    }

    @Test
    public void test5() {
        Flux<String> flux = Flux
                .<String>error(new IllegalArgumentException())
                .doOnError(System.out::println)
                .retryWhen(Retry.from(companion ->
                        companion.take(3)));
    }

    @Test
    public void test() throws InterruptedException {
        Flux.interval(Duration.ofMillis(250))
                .subscribe(System.out::println);
        Thread.sleep(2100 * 10);
    }

    @Test
    public void test0() throws InterruptedException {
        AtomicInteger count = new AtomicInteger();
        Flux.interval(Duration.ofMillis(250))
                .map(input -> {
                    int andIncrement = count.getAndIncrement();
                    if (andIncrement % 3 == 0) {
                        throw new RuntimeException("boom");
                    }
                    return input;
                })
                .retryWhen(Retry.from(companion ->
                        companion.map(rs -> {
                            log.info("totalRetries:{}", rs.totalRetries());
                            log.info("totalRetriesInARow:{}", rs.totalRetriesInARow());
                            return 0;
                        })
                ))
                .subscribe(System.out::println, System.err::println);

        Thread.sleep(2100 * 10);
    }

    @Test
    public void test4() throws InterruptedException {
        Flux.interval(Duration.ofMillis(250))
                .map(input -> {
                    if (input < 3) return "tick " + input;
                    throw new RuntimeException("boom");
                })
                .retry(1)
                .elapsed()
                .subscribe(System.out::println, System.err::println);

        Thread.sleep(2100);
    }

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
