package com.jemmy.dubbo.sample.service;

import com.jemmy.dubbo.sample.api.GreetingsService;
import org.apache.dubbo.config.annotation.Service;

/**
 * @author zhujiang.cheng
 * @since 2019/11/12
 */
//@Service
public class GreetingsServiceImpl implements GreetingsService {

    @Override
    public String sayHi(String name) {
        System.out.println("New name: " + name);
        return "Hi " + name;
    }
}
