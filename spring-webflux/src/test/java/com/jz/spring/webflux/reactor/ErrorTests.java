package com.jz.spring.webflux.reactor;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author xuwenbingfor
 * @version 2020/7/10 21:31
 * @description
 */
@Slf4j
public class ErrorTests {
    @Test
    public void test3() {
        AtomicInteger errorCount = new AtomicInteger();
        AtomicInteger transientHelper = new AtomicInteger();
        Flux<Integer> transientFlux = Flux.<Integer>generate(sink -> {
            int i = transientHelper.getAndIncrement();
            if (i == 10) {
                sink.next(i);
                sink.complete();
//            } else if (i % 3 == 0) {
            } else if (i % 4 == 0) {
                sink.next(i);
            } else {
                sink.error(new IllegalStateException("Transient error at " + i));
            }
        })
                .doOnError(e -> errorCount.incrementAndGet());

        transientFlux.retryWhen(Retry.max(2).transientErrors(true))
//        transientFlux.retryWhen(Retry.max(2))
                .blockLast();
        log.info("errorCount:{}", errorCount);
    }

    @Test
    public void test2() throws InterruptedException {
        Flux<String> flux = Flux
                .<String>error(new IllegalArgumentException())
                .doOnError(o -> {
                    log.info("thread:{},o:{}", Thread.currentThread().getName(), o.toString());
                })
//                .retryWhen(Retry.from(companion -> companion.take(3)));
                .retryWhen(Retry.fixedDelay(3, Duration.ofSeconds((long) 5)));
        flux.subscribe(o -> log.info("{}", o));
        TimeUnit.SECONDS.sleep(30);
    }

    /**
     * 方法降级
     */
    @Test
    public void test1() {
        Flux.just(1, 2, 3)
                .map(v -> {
                    if (v == 3) {
                        throw new RuntimeException();
                    }
                    return 10 + v;
                })
                .onErrorResume(e -> Flux.just(100, 101))
                .doFinally(v -> log.info("finally:{}", v))
                .subscribe(v -> log.info("{}", v));
    }
//    21:54:14.937 [main] INFO com.jz.spring.webflux.reactor.ErrorTests - 11
//    21:54:14.939 [main] INFO com.jz.spring.webflux.reactor.ErrorTests - 12
//    21:54:14.946 [main] INFO com.jz.spring.webflux.reactor.ErrorTests - 100
//    21:54:14.946 [main] INFO com.jz.spring.webflux.reactor.ErrorTests - 101
//    21:54:14.954 [main] INFO com.jz.spring.webflux.reactor.ErrorTests - finally:onComplete
}
