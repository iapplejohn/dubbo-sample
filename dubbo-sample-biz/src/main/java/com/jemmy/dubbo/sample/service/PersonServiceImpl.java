package com.jemmy.dubbo.sample.service;

import com.jemmy.dubbo.sample.api.PersonService;
import com.jemmy.dubbo.sample.dto.PersonDTO;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.apache.dubbo.config.annotation.Service;

/**
 * @author zhujiang.cheng
 * @since 2020/3/19
 */
@Service(version = "1.0.0")
public class PersonServiceImpl implements PersonService {

    @Override
    public PersonDTO getById(Integer id) {
        PersonDTO dto = new PersonDTO();
        dto.setId(id);
        dto.setName("方新珠");
        try {
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return dto;
    }

    @Override
    public List<PersonDTO> listByNames(List names) {
        List<PersonDTO> list = new ArrayList<>();
        for (Object name : names) {
            PersonDTO dto = new PersonDTO();
            dto.setId(name.hashCode());
            dto.setName((String) name);
            list.add(dto);
        }
        return list;
    }

    @Override
    public Set<PersonDTO> distinct(List names) {
        Set<PersonDTO> set = new HashSet<>();
        for (Object name : names) {
            PersonDTO dto = new PersonDTO();
            dto.setId(name.hashCode());
            dto.setName((String) name);
            set.add(dto);
        }
        return set;
    }

    @Override
    public PersonDTO[] array(List<String> names) {
        PersonDTO[] array = new PersonDTO[names.size()];
        for (int i = 0; i < names.size(); i++) {
            PersonDTO dto = new PersonDTO();
            dto.setId(names.get(i).hashCode());
            dto.setName((String) names.get(i));
            array[i] = dto;
        }
        return array;
    }
}
