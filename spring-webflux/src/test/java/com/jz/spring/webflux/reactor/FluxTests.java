package com.jz.spring.webflux.reactor;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Subscription;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Arrays;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;

/**
 * @author xuwenbingfor
 * @version 2020/6/29 23:21
 * @description
 */
@Slf4j
public class FluxTests {
    @Test
    public void test21() {
        Flux<String> flux = Flux.generate(
                () -> 0,
                (state, sink) -> {
                    sink.next("3 x " + state + " = " + 3 * state);
                    if (state == 10) sink.complete();
                    return state + 1;
                });
        flux.subscribe(o -> {
            log.info("{}", o);
        });
    }

    @Test
    public void test22() {
        Flux<String> flux = Flux.generate(
                AtomicLong::new,
                (state, sink) -> {
                    long i = state.getAndIncrement();
                    sink.next("3 x " + i + " = " + 3 * i);
                    if (i == 10) sink.complete();
                    return state;
                }, (state) -> System.out.println("state: " + state));
        flux.subscribe(o -> {
            log.info("{}", o);
        });
    }

    @Test
    public void test13() {
        String key = "message";
        Mono<String> r = Mono.just("Hello")
                .flatMap(s -> Mono.subscriberContext()
                        .map(ctx -> s + " " + ctx.get(key)))
                .subscriberContext(ctx -> ctx.put(key, "Reactor"))
                .subscriberContext(ctx -> ctx.put(key, "World"));
        r.subscribe(o -> log.info("{}", o));
    }

    @Test
    public void test12() {
//        Flux.just(1, 3, 5, 2, 4, 6, 11, 12, 13)
//                .windowWhile(i -> i % 2 == 0)
//                .concatMap(g -> g.defaultIfEmpty(-1))
//                .subscribe(o -> log.info("{}", o));

//        Flux.just(1, 2, 5, 2, 4, 6, 11, 12, 13)
//                .windowWhile(i -> i % 2 == 0)
//                .concatMap(g -> g.defaultIfEmpty(-1))
//                .subscribe(o -> log.info("{}", o));

        Flux.just(2, 2, 5, 2, 4, 6, 11, 12, 13)
                .windowWhile(i -> i % 2 == 0)
                .concatMap(g -> g.defaultIfEmpty(-1))
                .subscribe(o -> log.info("{}", o));
    }

    @Test
    public void test11() throws InterruptedException {
        Flux<String> stringFlux1 = Flux.just("a", "b", "c", "d", "e", "f", "g", "h", "i");
        Flux<Flux<String>> stringFlux2 = stringFlux1.window(2);
        stringFlux2.concatMap(flux1 -> flux1.map(word -> word.toUpperCase())
                .delayElements(Duration.ofMillis(1000)))
                .subscribe(x -> log.info("->{}", x));
        Thread.sleep(20 * 1000);
    }

//15:37:27.152 [parallel-1] INFO com.jz.spring.webflux.reactor.FluxTests - ->A
//15:37:28.155 [parallel-2] INFO com.jz.spring.webflux.reactor.FluxTests - ->B
//15:37:29.156 [parallel-3] INFO com.jz.spring.webflux.reactor.FluxTests - ->C
//15:37:30.157 [parallel-4] INFO com.jz.spring.webflux.reactor.FluxTests - ->D
//15:37:31.159 [parallel-1] INFO com.jz.spring.webflux.reactor.FluxTests - ->E
//15:37:32.159 [parallel-2] INFO com.jz.spring.webflux.reactor.FluxTests - ->F
//15:37:33.161 [parallel-3] INFO com.jz.spring.webflux.reactor.FluxTests - ->G
//15:37:34.162 [parallel-4] INFO com.jz.spring.webflux.reactor.FluxTests - ->H
//15:37:35.162 [parallel-1] INFO com.jz.spring.webflux.reactor.FluxTests - ->I

    @Test
    public void test11_1() throws InterruptedException {
        Flux<String> stringFlux1 = Flux.just("a", "b", "c", "d", "e", "f", "g", "h", "i");
        Flux<Flux<String>> stringFlux2 = stringFlux1.window(2);
        stringFlux2.flatMap(flux1 -> flux1.map(word -> word.toUpperCase())
                .delayElements(Duration.ofMillis(1000)))
                .subscribe(x -> log.info("->{}", x));
        Thread.sleep(20 * 1000);
    }

//15:35:53.505 [parallel-2] INFO com.jz.spring.webflux.reactor.FluxTests - ->C
//15:35:53.509 [parallel-2] INFO com.jz.spring.webflux.reactor.FluxTests - ->A
//15:35:53.509 [parallel-2] INFO com.jz.spring.webflux.reactor.FluxTests - ->E
//15:35:53.509 [parallel-2] INFO com.jz.spring.webflux.reactor.FluxTests - ->G
//15:35:53.509 [parallel-2] INFO com.jz.spring.webflux.reactor.FluxTests - ->I
//15:35:54.506 [parallel-2] INFO com.jz.spring.webflux.reactor.FluxTests - ->H
//15:35:54.506 [parallel-2] INFO com.jz.spring.webflux.reactor.FluxTests - ->B
//15:35:54.507 [parallel-3] INFO com.jz.spring.webflux.reactor.FluxTests - ->F
//15:35:54.510 [parallel-4] INFO com.jz.spring.webflux.reactor.FluxTests - ->D

    @Test
    public void test10() {
        SampleSubscriber<Integer> ss = new SampleSubscriber<Integer>();
        Flux<Integer> ints = Flux.range(1, 4);
        ints.subscribe(i -> System.out.println(i),
                error -> System.err.println("Error " + error),
                () -> {
                    System.out.println("Done");
                },
                s -> s.request(10));
        ints.subscribe(ss);
    }

    public class SampleSubscriber<T> extends BaseSubscriber<T> {

        public void hookOnSubscribe(Subscription subscription) {
            System.out.println("Subscribed");
            request(1);
        }

        public void hookOnNext(T value) {
            System.out.println(value);
            request(1);
        }
    }


    @Test
    public void testCompose() {
        AtomicInteger ai = new AtomicInteger();
        Function<Flux<String>, Flux<String>> filterAndMap = f -> {
            if (ai.incrementAndGet() == 1) {
                return f.filter(color -> !color.equals("orange"))
                        .map(String::toUpperCase);
            }
            return f.filter(color -> !color.equals("purple"))
                    .map(String::toUpperCase);
        };

        Flux<String> composedFlux =
                Flux.fromIterable(Arrays.asList("blue", "green", "orange", "purple"))
                        .doOnNext(System.out::println)
                        .compose(filterAndMap);

        composedFlux.subscribe(d -> System.out.println("Subscriber 1 to Composed MapAndFilter :" + d));
        composedFlux.subscribe(d -> System.out.println("Subscriber 2 to Composed MapAndFilter: " + d));
    }

    @Test
    public void testTransform() {
        Function<Flux<String>, Flux<String>> filterAndMap =
                f -> f.filter(color -> !color.equals("orange"))
                        .map(String::toUpperCase);

        Flux.fromIterable(Arrays.asList("blue", "green", "orange", "purple"))
                .doOnNext(System.out::println)
                .transform(filterAndMap)
                .subscribe(d -> System.out.println("Subscriber to Transformed MapAndFilter: " + d));
    }

    @Test
    public void testGenerate1() {
        final AtomicInteger count = new AtomicInteger(1);   // 1
        Flux.generate(sink -> {
            sink.next(count.get() + " : " + new Date());   // 2
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (count.getAndIncrement() >= 5) {
                sink.complete();     // 3
            }
        }).subscribe(System.out::println);  // 4
    }

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
