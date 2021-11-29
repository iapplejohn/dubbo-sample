package com.jemmy.dubbo.sample;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableDubbo
public class SampleTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(SampleTestApplication.class, args);
    }

}
