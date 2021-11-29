package com.jemmy.dubbo.sample.service;

import com.jemmy.dubbo.sample.api.AsyncService;
import com.jemmy.dubbo.sample.api.FooService;
import com.jemmy.dubbo.sample.api.GreetingsService;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import org.apache.dubbo.config.annotation.Reference;
//import org.apache.dubbo.remoting.exchange.ResponseCallback;
//import org.apache.dubbo.remoting.exchange.ResponseFuture;
import org.apache.dubbo.rpc.RpcContext;
import org.apache.dubbo.rpc.protocol.dubbo.FutureAdapter;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

//@Service
public class ConsumerServiceImpl {

    @Reference(async = false)
    private FooService fooService;

    @Reference(async = true)
    private AsyncService asyncService;

    @Reference(async = true)
    private GreetingsService greetingsService;

//    @Scheduled(cron = "20 0/1 * * * ?")
    public void testAsyncWithFuture() throws ExecutionException, InterruptedException {
        // 此调用会立即返回null
        String result = fooService.findFoo("brilliant");
        // 拿到调用的Future引用，当结果返回后，会被通知和设置到此Future
        Future<String> fooFuture = RpcContext.getContext().getFuture();
        String response = fooFuture.get();
        System.out.println(response);
    }

//    @Scheduled(cron = "30 0/1 * * * ?")
    public void testAsyncWithResponseFuture() {
        // 此调用会立即返回null
        String result = fooService.findFoo("brilliant");
        // 拿到Dubbo内置的ResponseFuture并设置回调
//        ResponseFuture fooFuture = ((FutureAdapter)RpcContext.getContext().getFuture()).getFuture();
//        fooFuture.setCallback(new ResponseCallback() {
//            @Override
//            public void done(Object response) {
//                System.out.println(response);
//            }
//
//            @Override
//            public void caught(Throwable exception) {
//                exception.printStackTrace();
//            }
//        });
    }

//    @Scheduled(cron = "20 0/1 * * * ?")
    public void testAsyncWithCompletable() throws ExecutionException, InterruptedException {
        // 此调用会立即返回null
        CompletableFuture<String> future = asyncService.sayHello("awesome boy");
        // 拿到调用的Future引用，当结果返回后，会被通知和设置到此Future
        String response = future.get();
        System.out.println(response);
    }

//    @Scheduled(cron = "20 0/1 * * * ?")
    public void testAsyncWithDefault() throws ExecutionException, InterruptedException {
        // 此调用会立即返回null
        CompletableFuture<String> future = greetingsService.sayHi("Fabulous boy", true);
        // 拿到调用的Future引用，当结果返回后，会被通知和设置到此Future
        String response = future.get();
        System.out.println(response);
    }
}
