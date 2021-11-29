package com.jemmy.dubbo.sample.service;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import java.util.Map.Entry;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.common.utils.StringUtils;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.utils.ReferenceConfigCache.KeyGenerator;

/**
 * @author chengzhujiang
 * @date 2019/4/11
 */
@Slf4j
public class GenericServiceCache {

    public static final String DEFAULT_NAME = "_DEFAULT_";

    private static final ConcurrentMap<String, GenericServiceCache> cacheHolder = new ConcurrentHashMap<>();

    private final String name;
    private final KeyGenerator generator;

    private final Cache<String, ReferenceConfig<?>> serviceCache = CacheBuilder.newBuilder()
        .expireAfterAccess(60L, TimeUnit.MINUTES)
        .maximumSize(300)
        .removalListener(notification -> {
            String cause = notification.getCause().name();
            if ("EXPIRED".equals(cause) || "SIZE".equals(cause)) {
                ReferenceConfig referenceConfig = (ReferenceConfig) notification.getValue();
                referenceConfig.destroy();
                log.info("Remove ReferenceConfig[group={},interface={},version={}], cause: {}",
                    referenceConfig.getGroup(), referenceConfig.getInterface(), referenceConfig.getVersion(), cause);
            }
        })
        .build();

    private GenericServiceCache(String name, KeyGenerator generator) {
        this.name = name;
        this.generator = generator;
    }

    public static GenericServiceCache getCache() {
        return getCache(DEFAULT_NAME);
    }

    public static GenericServiceCache getCache(String name) {
        return getCache(name, DEFAULT_KEY_GENERATOR);
    }

    private static GenericServiceCache getCache(String name, KeyGenerator keyGenerator) {
        GenericServiceCache cache = cacheHolder.get(name);
        if (cache != null) {
            return cache;
        }
        cacheHolder.put(name, new GenericServiceCache(name, keyGenerator));
        return cacheHolder.get(name);
    }

    public static final KeyGenerator DEFAULT_KEY_GENERATOR = referenceConfig -> {
        String iName = referenceConfig.getInterface();
        if (StringUtils.isBlank(iName)) {
            iName = referenceConfig.getInterfaceClass().getName();
        }
        if (StringUtils.isBlank(iName)) {
            throw new IllegalArgumentException("No interface info in ReferenceConfig" + referenceConfig);
        }
        String group = referenceConfig.getGroup();
        String version = referenceConfig.getVersion();
        String registryAddress = referenceConfig.getRegistry().getAddress();
        return generateKey(group, iName, version, registryAddress);
    };

    public ReferenceConfig<?> get(String key, Callable<ReferenceConfig<?>> callable) throws ExecutionException {
        ReferenceConfig<?> referenceConfig = serviceCache.get(key, callable);
        return referenceConfig;
    }

    public void put(String key, ReferenceConfig<?> referenceConfig) {
        serviceCache.put(key, referenceConfig);
    }

    public <T> void destroy(ReferenceConfig<T> referenceConfig) {
        String key = generator.generateKey(referenceConfig);

        destroyKey(key);
    }

    private void destroyKey(String key) {
        ReferenceConfig<?> config = serviceCache.getIfPresent(key);
        if (config == null) {
            return;
        }
        serviceCache.invalidate(key);
        config.destroy();
    }

    /**
     * clear and destroy all {@link ReferenceConfig} in the cache.
     */
    public void destroyAll() {
        ConcurrentMap<String, ReferenceConfig<?>> map = serviceCache.asMap();
        for (Entry<String, ReferenceConfig<?>> entry : map.entrySet()) {
            String key = entry.getKey();
            destroyKey(key);
        }
    }

    @Override
    public String toString() {
        return "GenericServiceCache(name: " + name + ")";
    }

    public static String generateKey(String group, String interfaze, String version, String registryAddress) {
        StringBuilder ret = new StringBuilder();
        if (!StringUtils.isBlank(group)) {
            ret.append(group).append("/");
        }
        ret.append(interfaze);
        if (!StringUtils.isBlank(version)) {
            ret.append(":").append(version);
        }
        if (!StringUtils.isBlank(registryAddress)) {
            ret.append(':').append(registryAddress);
        }
        return ret.toString();
    }
}
