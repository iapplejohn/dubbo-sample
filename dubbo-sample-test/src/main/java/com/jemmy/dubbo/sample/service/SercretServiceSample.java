package com.jemmy.dubbo.sample.service;

import com.google.common.base.Charsets;
import com.pingpongx.secret.api.IDecryptService;
import com.pingpongx.secret.api.IEncryptService;
import com.pingpongx.secret.api.exception.SecretException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * @author zhujiang.cheng
 * @since 2020/5/11
 */
//@Service
@Slf4j
public class SercretServiceSample {

//    @Reference(timeout = 60000, retries = 0, url = "dubbo://172.16.12.71:20886/com.pingpongx.secret.api.IEncryptService?anyhost=true&application=secret&dubbo=2.6.2&generic=false&interface=com.pingpongx.secret.api.IEncryptService&methods=encryptForJdbc,encrypt,encryptFieldsOfList,encryptFields&pid=7001&revision=1.0.0&side=provider&timeout=10000&timestamp=1589183695383&version=1.0.0")
    @Reference(version = "1.0.0")
    private IEncryptService encryptService;

//    @Reference(timeout = 60000, retries = 0, url = "dubbo://172.16.12.71:20886/com.pingpongx.secret.api.IDecryptService?anyhost=true&application=secret&dubbo=2.6.2&generic=false&interface=com.pingpongx.secret.api.IDecryptService&methods=decryptForJdbc,decryptFields,decrypt&pid=7001&revision=1.0.0&side=provider&timeout=10000&timestamp=1589183695055&version=1.0.0")
    @Reference(version = "1.0.0")
    private IDecryptService decryptService;

    @Scheduled(cron = "30 0/1 * * * ?")
    public void encryptTest() throws IOException, SecretException {
//        RandomAccessFile file = new RandomAccessFile("/Users/chengzhujiang/Documents/great.txt", "rw");
//        FileChannel fileChannel  = file.getChannel();
//        byte[] data = new byte[(int) fileChannel.size()];
//        file.read(data);
        byte[] data = "春天夏天1234fdafasb cfdafsda&$#````".getBytes(Charsets.UTF_8);
//        MappedByteBuffer buffer = fileChannel.map(MapMode.READ_WRITE, 0, fileChannel.size());
        byte[] cipherData = encryptService.encrypt(data);
        log.info("cipherData: {}", cipherData);
//        buffer.force();
//        fileChannel.close();

        byte[] plainData = decryptService.decrypt(cipherData);
        log.info("plainData: {}", plainData);
        log.info("plain string: {}", new String(plainData, Charsets.UTF_8));
    }
}
