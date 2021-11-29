package com.jemmy.dubbo.sample.service;

import com.jemmy.dubbo.sample.api.FooService;
import org.apache.dubbo.config.annotation.Service;

//@Service
public class FooServiceImpl implements FooService {

    @Override
    public String findFoo(String name) {
        return "Response from provider " + name;
    }
}
