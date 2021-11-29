package com.jemmy.dubbo.sample.service;

import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author zhujiang.cheng
 * @since 2020/3/19
 */
//@Service
@Slf4j
public class MetaDataServiceSample {

    @Resource
    private MetaDataService metaDataService;

//    @Scheduled(cron = "20 0/1 * * * ?")
    public void findByServiceTest() {
        String group = null;
        String interfaze = "com.jemmy.dubbo.sample.api.PersonService";
        String version = "1.0.0";
        String side = "provider";
        String application = "dubbo-sample-biz";

        String metadata = metaDataService.findByService(interfaze, version, group, side, application);
        log.info("metadata: {}", metadata);
    }
}
