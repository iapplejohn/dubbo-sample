package com.jemmy.dubbo.sample.service;

import com.google.common.base.Charsets;
import com.pingpongx.secret.api.IDecryptService;
import com.pingpongx.secret.api.IEncryptService;
import com.pingpongx.secret.api.exception.SecretException;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * @author zhujiang.cheng
 * @since 2020/7/24
 */
//@Service
@Slf4j
public class SecretConsumerServiceImpl {

    @Reference(version = "1.0.0")
    private IEncryptService encryptService;

    @Reference(version = "1.0.0")
    private IDecryptService decryptService;

    @Scheduled(cron = "0/10 0/1 * * * ?")
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

