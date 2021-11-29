package com.jemmy.dubbo.sample.dal.core;

import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.TableInfoHelper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.HashMap;
import java.util.Map;
import org.springframework.util.ReflectionUtils;

/**
 * @author chengzhujiang
 * @since 2019/7/20
 */
public abstract class BaseRepository<M extends BaseMapper<T>, T extends BaseDO> extends ServiceImpl<M, T> {

    @Override
    public boolean saveOrUpdate(T entity) {
        if (null != entity) {
            Class<?> cls = entity.getClass();
            TableInfo tableInfo = TableInfoHelper.getTableInfo(cls);
            if (null != tableInfo && StringUtils.isNotEmpty(tableInfo.getKeyProperty())) {
                /*
                 * 更新成功直接返回，失败执行插入逻辑
                 */
                return updateBySingleKey(entity) || save(entity);
            } else {
                throw new MybatisPlusException("Error:  Can not execute. Could not find @TableId.");
            }
        }
        return false;
    }

    /**
     * 可以通过重写此方法来改变 insert or update 时 所使用的判断条件
     * ( 此方法返回 true , 将不会执行 insert, 否则将会执行 insert)
     *
     * @param entity 对象
     * @return 有记录成功被更新 返回 true , 否则返回 false
     */
    public boolean updateBySingleKey(T entity) {
        Map<String, Object> where = new HashMap<>();
        ReflectionUtils.doWithFields(entity.getClass(),
            // 增加 where 条件
            field -> where.put(field.getName(), field.get(entity)),
            // 增加了 @SingleKey 注解的字段
            field -> {
                ReflectionUtils.makeAccessible(field);
                return field.isAnnotationPresent(SingleKey.class);
            });
        return where.isEmpty() ?
            super.updateById(entity) :
            update().allEq(where).update(entity);
    }
}
