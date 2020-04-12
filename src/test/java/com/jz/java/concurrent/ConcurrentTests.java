package com.jz.java.concurrent;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class ConcurrentTests {
    @Test
    public void test1() {
        // 获取逻辑处理器个数
        int availableProcessors = Runtime.getRuntime().availableProcessors();
        log.info("availableProcessors:{}", availableProcessors);
    }
}
