package com.jemmy.dubbo.sample.version;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.common.utils.NetUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

/**
 * @author zhujiang.cheng
 * @since 2022/1/18
 */
@Slf4j
@Component
public class NetTest implements InitializingBean {

    @Override
    public void afterPropertiesSet() throws Exception {
        Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
        while (interfaces.hasMoreElements()) {
            NetworkInterface networkInterface = interfaces.nextElement();
            if (ignoreNetworkInterface(networkInterface)) { // docker0 不会被 ignore，因为 isVirtual 为 false
                continue;
            }
        }

        // dubbo 2.7.15 获取可用的 IP 地址，拿到了 docker0 的 IP，当应用所在的服务器有docker 时
        // 原因是: 2.7.15 先拿可用网卡，最后拿 localHost，这和之前的 2.7.8 正好相反
        InetAddress address = NetUtils.getLocalAddress();
        log.info("hostname: {}, address: {}", address.getHostName(), address.getHostAddress());

        // dubbo 2.7.15 拿到了真实的 IP 地址
        InetAddress localAddress = InetAddress.getLocalHost();
        log.info("Local hostname: {}, address: {}", localAddress.getHostName(), localAddress.getHostAddress());
    }

    private boolean ignoreNetworkInterface(NetworkInterface networkInterface) throws SocketException {
        boolean ignore;

        if (networkInterface == null
            || networkInterface.isLoopback()
            || networkInterface.isVirtual()
            || !networkInterface.isUp()) {
            ignore = true;
        } else {
            ignore = false;
        }
        log.info("network interface: {}, isLoopback: {}, isVirtual: {}, isUp: {}", networkInterface,
            networkInterface.isLoopback(), networkInterface.isVirtual(), networkInterface.isUp());
        return ignore;
    }
}
