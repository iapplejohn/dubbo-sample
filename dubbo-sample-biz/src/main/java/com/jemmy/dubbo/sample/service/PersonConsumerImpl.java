package com.jemmy.dubbo.sample.service;

import com.jemmy.dubbo.sample.api.PersonService;
import com.jemmy.dubbo.sample.dto.PersonDTO;
import com.pingpongx.secret.api.exception.SecretException;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * @author zhujiang.cheng
 * @since 2021/11/18
 */
@Slf4j
@Service
public class PersonConsumerImpl {

    @Reference()
    private PersonService personService;

    @Scheduled(cron = "0/20 0/1 * * * ?")
    public void encryptTest() throws IOException, SecretException {
        PersonDTO personDTO = personService.getById(123);
        log.info("person: {}", personDTO);
    }
}
