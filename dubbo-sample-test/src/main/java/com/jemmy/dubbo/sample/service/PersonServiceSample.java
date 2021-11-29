package com.jemmy.dubbo.sample.service;

import com.jemmy.dubbo.sample.api.PersonService;
import com.jemmy.dubbo.sample.dto.PersonDTO;
import java.util.Arrays;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * @author zhujiang.cheng
 * @since 2020/3/19
 */
//@Service
@Slf4j
public class PersonServiceSample {

    @Reference
    private PersonService personService;

//    @Scheduled(cron = "30 0/1 * * * ?")
    public void getByIdTest() {
        Integer id = 15;
        PersonDTO dto = personService.getById(id);
        log.info("dto: {}", dto);
    }

//    @Scheduled(cron = "30 0/1 * * * ?")
    public void listByNamesTest() {
        List<String> names = Arrays.asList("john", "tina");
        List<PersonDTO> list = personService.listByNames(names);
        log.info("list: {}", list);
    }

}
