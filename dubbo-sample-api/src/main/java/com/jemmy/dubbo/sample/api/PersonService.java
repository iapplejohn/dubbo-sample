package com.jemmy.dubbo.sample.api;

import com.jemmy.dubbo.sample.dto.PersonDTO;
import java.util.List;
import java.util.Set;

/**
 * @author zhujiang.cheng
 * @since 2020/3/19
 */
public interface PersonService {

    PersonDTO getById(Integer id);

    List<PersonDTO> listByNames(List names);

    Set<PersonDTO> distinct(List names);

    /**
     * 泛化调用不支持泛型参数，调用会报错:
     * <code>NoClassDefFoundException: java.util.List<java.lang.String></code>
     *
     * @param names
     * @return
     */
    PersonDTO[] array(List<String> names);
}
