package com.jemmy.dubbo.sample.service;

import com.jemmy.dubbo.sample.registry.metadata.MetaDataCollector;
import javax.annotation.Resource;
import org.apache.dubbo.metadata.identifier.MetadataIdentifier;
import org.springframework.stereotype.Service;

/**
 * @author zhujiang.cheng
 * @since 2020/3/19
 */
//@Service
public class MetaDataService {

    @Resource
    private MetaDataCollector metaDataCollector;

    public String findByService(String serviceInterface, String version, String group, String side, String application) {
        MetadataIdentifier key = new MetadataIdentifier(
            serviceInterface,
            version,
            group,
            side,
            application
        );
        String metadata = metaDataCollector.getProviderMetaData(key);
        return metadata;
    }

}
