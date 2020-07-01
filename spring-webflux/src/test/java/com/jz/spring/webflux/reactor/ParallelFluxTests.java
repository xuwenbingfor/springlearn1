package com.jz.spring.webflux.reactor;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

/**
 * @author xuwenbingfor
 * @version 2020/7/1 22:47
 * @description
 */
@Slf4j
public class ParallelFluxTests {
    @Test
    public void test1() {
        Flux.range(1, 10)
                .parallel(2)
                .runOn(Schedulers.parallel())
                .subscribe(i -> System.out.println(Thread.currentThread().getName() + " -> " + i));
    }

}
