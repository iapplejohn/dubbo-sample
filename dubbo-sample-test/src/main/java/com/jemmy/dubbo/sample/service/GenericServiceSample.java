package com.jemmy.dubbo.sample.service;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.rpc.service.GenericException;
import org.apache.dubbo.rpc.service.GenericService;
import org.springframework.stereotype.Service;

/**
 * @author zhujiang.cheng
 * @since 2020/3/18
 */
@Slf4j
//@Service
public class GenericServiceSample {

    @Resource
    private ApplicationConfig applicationConfig;

    @Resource
    private MetaDataService metaDataService;

//    @Scheduled(cron = "20 0/1 * * * ?")
    public void returnStringTest() {
        String group = "";
        String interfaze = "com.pingpongx.secret.api.IDecryptService";
        String version = "1.0.0";
        String registryAddress = "dev-zk1.pingpongx.com";
        String method = "decryptForJdbc";
        String[] parameterTypes = new String[] { "java.lang.String" };
        Object[] params = new Object[] { "yQ0cbre1vHFYAeJvu+Zdvw\\=\\=" };
        Object result = invoke(group, interfaze, version, registryAddress, method, parameterTypes, params);
        log.info("result: {}", result);
    }

//    @Scheduled(cron = "20 0/1 * * * ?")
    public void returnDtoTest() {
        String group = "";
        String interfaze = "com.pingpongx.secret.api.IDecryptService";
        String version = "1.0.0";
        String registryAddress = "dev-zk1.pingpongx.com";
        String method = "decrypt";
        String[] parameterTypes = new String[] { "java.util.List<java.lang.String>" };
        ArrayList<String> list = new ArrayList<>();
        list.add("yQ0cbre1vHFYAeJvu+Zdvw\\=\\=");
        Object[] params = new Object[] { list };

        Object result = invoke(group, interfaze, version, registryAddress, method, parameterTypes, params);
        log.info("result: {}", result);
    }

    public Object invoke(String group, String interfaze, String version, String registryAddress, String method,
        String[] parameterTypes, Object[] params) {

        GenericServiceCache genericServiceCache = GenericServiceCache.getCache();
        String key = GenericServiceCache.generateKey(group, interfaze, version, registryAddress);
        GenericService genericService = null;

        try {
            ReferenceConfig<?> referenceConfig = genericServiceCache.get(key, () -> {
                ReferenceConfig<GenericService> reference = buildReferenceConfig(interfaze, version, group);
                genericServiceCache.put(key, reference);
                return reference;
            });
            genericService = (GenericService) referenceConfig.get();
        } catch (ExecutionException e) {
            log.error("get generic service", e);
        }

        Object result;
        try {
            long start = System.currentTimeMillis();
            result = genericService.$invoke(method, parameterTypes, params);
            if (result != null) {
                eliminateClassProperty(result);
            }
            String resultStr = null;
            if (result instanceof Map) {
                Map<?, ?> map = (Map) result;
                resultStr = mapToString(map);
            } else if (result != null) {
                resultStr = result.toString();
            }
            log.info("Invoke {}.{}(),cost {} ms,params:{},result:{}", interfaze, method, (System.currentTimeMillis() - start), params, resultStr);
        } catch (GenericException e) {
            log.error("Invoke {}.{}(),params:{},error:", interfaze, method, params, e);
            String exceptionClass = e.getExceptionClass();
            Object instance = null;
            try {
                Class<?> clazz = Class.forName(exceptionClass);
                Constructor<?> constructor = clazz.getConstructor(String.class);
                instance = constructor.newInstance(e.getExceptionMessage());
            } catch (Exception ex) {
                log.error("Instantiate the exception", ex);
            }
            if (instance instanceof RuntimeException) {
                throw (RuntimeException) instance;
            } else {
                throw new RuntimeException((Exception) instance);
            }
        }
        return result;
    }

    public ReferenceConfig<GenericService> buildReferenceConfig(String interfaze, String version, String group) {
        ReferenceConfig<GenericService> reference = new ReferenceConfig<>();

        reference.setGeneric(true);
        reference.setApplication(applicationConfig);
        reference.setInterface(interfaze);
        reference.setVersion(version);
        reference.setGroup(group);
        reference.setRetries(0);
        return reference;
    }

    public <K, V> String mapToString(Map<K, V> map) {
        Iterator<Entry<K, V>> i = map.entrySet().iterator();
        if (!i.hasNext())
            return "{}";

        StringBuilder sb = new StringBuilder();
        sb.append('{');
        for (; ; ) {
            Entry<K, V> e = i.next();
            K key = e.getKey();
            V value = e.getValue();
            sb.append(key);
            sb.append('=');
            if (value instanceof byte[]) {
                sb.append("[byte array]");
            } else {
                sb.append(value);
            }
            if (!i.hasNext())
                return sb.append('}').toString();
            sb.append(',').append(' ');
        }
    }

    private void eliminateClassProperty(Object obj) {
        if (obj instanceof Map) {
            Map<?, ?> map = (Map) obj;
            map.remove("class");
            for (Entry<?, ?> entry : map.entrySet()) {
                Object value = entry.getValue();
                if (value != null) {
                    handleEliminate(value);
                }
            }
        } else if (obj instanceof List) {
            List<?> list = (List<?>) obj;
            for (Object elem : list) {
                if (elem != null) {
                    handleEliminate(elem);
                }
            }
        } else if (obj instanceof Set) {
            Set<?> set = (Set<?>) obj;
            set.remove("class");
            for (Object elem : set) {
                if (elem != null) {
                    handleEliminate(elem);
                }
            }
        } else if (obj.getClass().isArray()) {
            Object[] array = (Object[]) obj;
            for (Object elem : array) {
                if (elem != null) {
                    handleEliminate(elem);
                }
            }
        }
    }

    private void handleEliminate(Object elem) {
        Class<?> clazz = elem.getClass();
        if (elem instanceof String || clazz.isPrimitive()) {
            return;
        }
        eliminateClassProperty(elem);
    }
}
