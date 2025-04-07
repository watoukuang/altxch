package com.watoukuang.altxch.common.mybatis.interceptor;

import com.watoukuang.altxch.common.mybatis.constant.FieldConstant;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Invocation;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Map;

public class InsertUpdateInterceptor {
    /**
     * 调用拦截
     *
     * @param invocation 调用器
     * @param userId     当前用户 id
     */
    public static Object intercept(Invocation invocation, Long userId) throws Throwable {
        MappedStatement statement = (MappedStatement) invocation.getArgs()[0];
        // 获取执行的 SQL 语句类型
        SqlCommandType sqlCommandType = statement.getSqlCommandType();
        // 拦截插入和更新操作，如果不是，则跳过
        if (!SqlCommandType.INSERT.equals(sqlCommandType) && !SqlCommandType.UPDATE.equals(sqlCommandType)) {
            return invocation.proceed();
        }

        // 获取对应的参数类
        Object parameter = invocation.getArgs()[1];
        if (parameter == null) {
            return invocation.proceed();
        }

        // 获取实体对象，如果属于 ParamMap （单个操作），则从参数 param1 获取，否则为 parameter (批量操作)
        Object clazz = null;
        if (parameter instanceof MapperMethod.ParamMap) {
            // 单个操作
            String paramKey = "param1";
            String paramEtKey = "et";
            if (((Map) parameter).containsKey(paramKey)) {
                clazz = ((Map) parameter).get(paramKey);
            } else if (((Map) parameter).containsKey(paramEtKey)) {
                clazz = ((Map) parameter).get(paramEtKey);
            }
        } else {
            // 批量操作
            clazz = parameter;
        }
        setParameter(sqlCommandType, clazz, userId);

        return invocation.proceed();
    }

    /**
     * 设置参数
     *
     * @param sqlCommandType SQL 语句类型
     * @param clazz          参数对象
     * @param userId         操作人用户 id
     */
    private static void setParameter(SqlCommandType sqlCommandType, Object clazz, Long userId) {
        if (SqlCommandType.INSERT.equals(sqlCommandType)) {
            // 插入
            // 创建时间
            setParameterValue(clazz, FieldConstant.CREATED, LocalDateTime.now());
            // 创建人
            setParameterValue(clazz, FieldConstant.CREATE_BY, userId);
            // 修改时间
            setParameterValue(clazz, FieldConstant.UPDATED, LocalDateTime.now());
            // 修改用户
            setParameterValue(clazz, FieldConstant.UPDATE_BY, userId);

        } else if (SqlCommandType.UPDATE.equals(sqlCommandType)) {
            // 修改
            // 修改时间
            setParameterValue(clazz, FieldConstant.UPDATED, LocalDateTime.now());
            // 修改人用户 id
            setParameterValue(clazz, FieldConstant.UPDATE_BY, userId);
        }
    }

    /**
     * 设置参数值
     *
     * @param clazz     参数对象类
     * @param fieldName 属性名称
     * @param value     属性值
     */
    private static void setParameterValue(Object clazz, String fieldName, Object value) {
        Field field = null;
        try {
            field = clazz.getClass().getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            return;
        }
        // 设置允许访问
        field.setAccessible(true);
        try {
            field.set(clazz, value);
        } catch (IllegalAccessException e) {
            return;
        }
    }
}
