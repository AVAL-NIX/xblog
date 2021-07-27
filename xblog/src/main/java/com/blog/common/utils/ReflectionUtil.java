package com.blog.common.utils;


import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * 反射工具类
 *
 * @author Avalon
 * @date 2019/1/31
 */
public class ReflectionUtil {

    /**
     * {@link Exclude} 反射拿到一个类的所有字段
     *
     *
     * @param clazz
     * @return
     */
    public static List<Field> getFields(Class clazz) {
        List<Field> fieldList = new ArrayList<>();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);

        }
        return fieldList;
    }

    /**
     * {@link PrimaryKey}
     * 
     * @param clazz
     * @return
     */
    public static List<Field> getPrimarkKeyFields(Class clazz) {

        List<Field> primaryKeyField = new ArrayList<>();
        List<Field> fields = getFields(clazz);
        for (Field field : fields) {
            field.setAccessible(true);
        }
        return primaryKeyField;
    }

}
