package com.jemmy.dubbo.sample.service;

import com.jemmy.dubbo.sample.api.AsyncService2;
import org.apache.dubbo.config.annotation.Service;
import org.apache.dubbo.rpc.AsyncContext;
import org.apache.dubbo.rpc.RpcContext;

/**
 * @author zhujiang.cheng
 * @since 2019/11/12
 */
//@Service
public class AsyncService2Impl implements AsyncService2 {

    @Override
    public String sayHello(String name) {
        AsyncContext asyncContext = RpcContext.startAsync();
        new Thread(() -> asyncContext.write("Hello " + name + ", response from provider.")).start();
        return null;
    }
}
