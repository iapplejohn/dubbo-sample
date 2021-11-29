package com.jemmy.dubbo.sample.service;

import com.jemmy.dubbo.sample.api.AsyncService;
import java.util.concurrent.CompletableFuture;
import org.apache.dubbo.config.annotation.Service;

//@Service
public class AsyncServiceImpl implements AsyncService {

    @Override
    public CompletableFuture<String> sayHello(String name) {
        System.out.println("New name: " + name);
        return CompletableFuture.completedFuture("Hello " + name);
    }
}
