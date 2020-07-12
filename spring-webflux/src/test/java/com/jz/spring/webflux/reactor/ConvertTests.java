package com.jz.spring.webflux.reactor;

import com.jz.spring.webflux.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.Map;

/**
 * @author xuwenbingfor
 * @version 2020/6/27 18:03
 * @description
 */
@Slf4j
public class ConvertTests {
    @Test
    public void skip() {
        Flux<String> flux1 = Flux.just("a", "b", "c").skip(1);
        flux1.subscribe(o -> {
            log.info("o:{}", o);
        });
    }

    @Test
    public void filter() {
        Flux<String> flux1 = Flux.just("a", "a", "b", "c");
        flux1.filter(o -> o.contains("a"))
                .distinct()
                .subscribe(o -> log.info("o:{}", o));
    }

    @Test
    public void map() {
        Flux<String> flux1 = Flux.just("a", "a", "b", "c");
        flux1.map(o -> o.toUpperCase()).subscribe(o -> log.info("o:{}", o));
    }


    @Test
    public void flatMap() {
        Flux<String> flux1 = Flux.just("a", "a", "b", "c");
        Flux<String> flux2 = flux1.flatMap(o -> Flux.just(o)
                .map(e -> "A" + e)
                .subscribeOn(Schedulers.parallel())
                .log()
        );
        flux2.subscribe();
//        flux2.subscribe(o -> {
//            log.info("1-thread:{},o:{}"
//                    , Thread.currentThread().getId(), o);
//        });
//        flux2.subscribe(o -> {
//            log.info("2-thread:{},o:{}"
//                    , Thread.currentThread().getId(), o);
//        });
    }

    @Test
    public void buffer() {
        Flux<String> flux1 = Flux.just("a", "a", "b", "c");
        flux1.buffer(2).subscribe(list -> {
            log.info("list:{}", JsonUtil.toJson(list));
        });
    }

    @Test
    public void collectMap() {
        Flux<String> flux1 = Flux.just("apple", "banana", "city", "apply");
        Mono<Map<String, String>> mapMono = flux1.collectMap(o -> String.valueOf(o.charAt(0)));
        mapMono.subscribe(map -> {
            log.info("map:{}", JsonUtil.toJson(map));
        });
    }


}
