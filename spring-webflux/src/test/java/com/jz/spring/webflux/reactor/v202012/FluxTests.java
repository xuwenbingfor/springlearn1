package com.jz.spring.webflux.reactor.v202012;

import com.jz.spring.webflux.util.JsonUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author xuwenbingfor
 * @version 2020/12/2 21:41
 * @description
 */
@Slf4j
public class FluxTests {


    @Test
    public void test1() throws InterruptedException {
        MyEventProcessor myEventProcessor = new MyEventProcessor();
        State state = new State();
        // 23:11:48.579 [main] INFO com.jz.spring.webflux.reactor.v202012.FluxTests - state: {"state":10000}
        Flux<String> bridge = Flux.create(sink -> {
        // 23:08:02.865 [main] INFO com.jz.spring.webflux.reactor.v202012.FluxTests - state: {"state":9958}
//        Flux<String> bridge = Flux.push(sink -> {
            myEventProcessor.register(
                    new MyEventListener() {
                        public void onDataChunk(List<String> chunk) {
                            String o = JsonUtil.toJson(chunk);
                            log.info("onDataChunk: threadId:{},o:{}", Thread.currentThread().getId(), o);
                            sink.next(o);
                        }

                        public void processComplete() {
                            sink.complete();
                        }
                    });
        });
        bridge.subscribe(o -> {
                    state.add1();
//                    try {
//                        TimeUnit.MILLISECONDS.sleep(5);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
                    log.info("subscribe: threadId:{},o:{}", Thread.currentThread().getId(), o);
                },
                o -> log.info("errorConsumer"),
                () -> log.info("completeConsumer,threadId:{}", Thread.currentThread().getId()));

        ExecutorService executorService = Executors.newFixedThreadPool(16);
        CountDownLatch countDownLatch = new CountDownLatch(10000);
        for (int index = 0; index < 10000; index++) {
            final int t = index;
            executorService.execute(() -> {
                log.info("execute: threadId:{},o:{}", Thread.currentThread().getId(), t);
                myEventProcessor.process(Arrays.asList(Integer.toString(t)));
                countDownLatch.countDown();
            });
        }
        countDownLatch.await();
        log.info("state: {}", JsonUtil.toJson(state));
    }


    interface MyEventListener {
        void onDataChunk(List<String> chunk);

        void processComplete();
    }

    static class MyEventProcessor {
        private MyEventListener myEventListener;

        public void register(MyEventListener myEventListener) {
            this.myEventListener = myEventListener;
        }

        public void process(List<String> list) {
            myEventListener.onDataChunk(list);
        }
    }

    @Data
    static class State {
        private int state = 0;

        public void add1() {
            state++;
        }
    }
}
