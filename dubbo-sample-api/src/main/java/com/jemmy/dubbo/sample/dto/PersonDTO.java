package com.jemmy.dubbo.sample.dto;

import java.io.Serializable;
import lombok.Data;

/**
 * @author zhujiang.cheng
 * @since 2020/3/19
 */
@Data
public class PersonDTO implements Serializable {

    private int id;

    private String name;

}
