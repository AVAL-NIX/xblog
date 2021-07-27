package com.blog.common.utils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;

/**
 * 获取超类泛型接口
 *
 * @author zx
 * @date 2019/1/31
 */
public class GenericsUtils {

    private GenericsUtils() {}

    public static Class getSuperClassGenricType(Class clazz) {
        return getInheritChainGenricType(clazz);

    }

    /**
     * 获取继承链中泛型(父类没有，索引父类的父类，顶多线上索引4层)
     * 
     * @param clazz
     * @return
     */
    public static Class getInheritChainGenricType(Class clazz) {
        Class super1Clazz = getSuperClassGenricType(clazz, 0);
        if (super1Clazz == null) {
            Class super1 = clazz.getSuperclass();
            Class super2Clazz = getSuperClassGenricType(super1, 0);
            if (super2Clazz == null) {
                Class super2 = super1.getSuperclass();
                Class super3Clazz = getSuperClassGenricType(super2, 0);
                if (super3Clazz == null) {
                    Class super3 = super2.getSuperclass();
                    Class super4Clazz = getSuperClassGenricType(super3, 0);
                    if (super4Clazz == null) {
                        return null;
                    } else {
                        return super4Clazz;
                    }
                } else {
                    return super3Clazz;
                }
            } else {
                return super2Clazz;
            }
        }
        return super1Clazz;
    }

    /**
     * 超早某个类的父类的泛型
     * 
     * @param clazz
     * @param index
     * @return
     */
    public static Class getSuperClassGenricType(Class clazz, int index) {
        Type genType = clazz.getGenericSuperclass();
        if (!(genType instanceof ParameterizedType)) {
            return null;
        }
        Type[] params = ((ParameterizedType)genType).getActualTypeArguments();
        if (index >= params.length || index < 0) {
            return Object.class;
        }
        if (!(params[index] instanceof Class)) {
            return Object.class;
        }
        return (Class)params[index];
    }

    /**
     * 提取泛型模型,多泛型的时候请将泛型T放在第一位
     *
     * @param mapperClass mapper 接口
     * @return mapper 泛型
     */
    public static Class<?> extractModelClass(Class<?> mapperClass) {
        Type[] types = mapperClass.getGenericInterfaces();
        ParameterizedType target = null;
        for (Type type : types) {
            if (type instanceof ParameterizedType) {
                Type[] typeArray = ((ParameterizedType) type).getActualTypeArguments();
                if (typeArray!=null && typeArray.length != 0) {
                    for (Type t : typeArray) {
                        if (t instanceof TypeVariable || t instanceof WildcardType) {
                            break;
                        } else {
                            target = (ParameterizedType) type;
                            break;
                        }
                    }
                }
                break;
            }
        }
        return target == null ? null : (Class<?>) target.getActualTypeArguments()[0];
    }
}
