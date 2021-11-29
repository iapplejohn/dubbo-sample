package com.jemmy.dubbo.sample.dal.core;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * @author chengzhujiang
 * @since 2019/7/20
 */
@Data
public class BaseDO implements Serializable {

    private static final long serialVersionUID = 2606585891103103720L;

    @TableId(type = IdType.AUTO)
    private Integer id;
    private Date created;
    private Date modified;
//    @TableLogic(value = "1", delval = "0")
    private Integer flag;
}
