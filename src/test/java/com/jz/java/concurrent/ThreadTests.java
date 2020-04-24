package com.jz.java.concurrent;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

@Slf4j
public class ThreadTests {
    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(() -> {
            log.info("thread name is {}", Thread.currentThread().getName());
            try {
                TimeUnit.SECONDS.sleep(60);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info("thread is over");
        }, "测试线程");
        // 设置为守护线程
        thread.setDaemon(true);
        thread.start();
        log.info("{}", Thread.currentThread().isDaemon());
        log.info("main thread is start");
        TimeUnit.SECONDS.sleep(30);
        log.info("main thread is over");
    }

    @Test
    public void test1() {
        new Thread(() -> {
            log.info("thread name is {}", Thread.currentThread().getName());
        }, "测试线程").start();
    }
}
