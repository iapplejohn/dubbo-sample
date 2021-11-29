package com.jemmy.dubbo.sample.api;

import java.util.concurrent.CompletableFuture;

@FunctionalInterface
public interface GreetingsService {

    String sayHi(String name);

    default CompletableFuture<String> sayHi(String name, boolean placeHolder) {
        return CompletableFuture.completedFuture(sayHi(name));
    }
}
