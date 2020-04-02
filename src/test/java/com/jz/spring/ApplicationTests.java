package com.jz.spring;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.support.SpringFactoriesLoader;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;

@SpringBootTest
@Slf4j
class ApplicationTests {

    @Test
    void test1() throws IOException {
        // 1、获取类路径下对应资源文件，会扫描所有jar包
        Enumeration<URL> resources = this.getClass()
                .getClassLoader()
                .getResources(SpringFactoriesLoader.FACTORIES_RESOURCE_LOCATION);
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            log.info(resource.toString());
        }
    }

}
