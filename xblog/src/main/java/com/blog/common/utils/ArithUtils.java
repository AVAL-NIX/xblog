package com.blog.common.utils;

import java.math.BigDecimal;

/**
 * 算术运算工具类
 *
 * @author zx
 * @date 2019/2/10
 */
public class ArithUtils {

    /**
     * 加法运算
     *
     * @param valA 加数
     * @param valB 加数
     * @return
     */
    public static double add(double valA, double valB) {
        BigDecimal a = new BigDecimal(Double.toString(valA));
        BigDecimal b = new BigDecimal(Double.toString(valB));
        return a.add(b).doubleValue();
    }

    /**
     * 加法运算
     *
     * @param valA 加数
     * @param valB 加数
     * @return
     */
    public static double add(double valA, double... valB) {
        BigDecimal a = new BigDecimal(Double.toString(valA));
        for (int i = 0; i < valB.length; i++) {
            a = a.add(new BigDecimal(Double.toString(valB[i])));
        }
        return a.doubleValue();
    }

    /**
     * 减法运算
     *
     * @param valA 被减数A
     * @param valB 减数B
     * @return 差
     */
    public static double sub(double valA, double valB) {
        BigDecimal a = new BigDecimal(Double.toString(valA));
        BigDecimal b = new BigDecimal(Double.toString(valB));
        return a.subtract(b).doubleValue();
    }

    /**
     * 乘法运算
     *
     * @param valA 被乘数A
     * @param valB 乘数B
     * @return 积
     */
    public static double mul(double valA, double valB) {
        BigDecimal a = new BigDecimal(Double.toString(valA));
        BigDecimal b = new BigDecimal(Double.toString(valB));
        return a.multiply(b).doubleValue();
    }

    /**
     * 除法运算。当发生除不尽的情况时，数字四舍五入，由scale参数指 定精度。
     *
     * @param valA  被除数
     * @param valB  除数
     * @param scale 精度
     * @return
     */
    public static double div(double valA, double valB, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("精确度不能小于0！");
        }

        BigDecimal a = new BigDecimal(Double.toString(valA));
        BigDecimal b = new BigDecimal(Double.toString(valB));
        return a.divide(b, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 除法运算。当发生除不尽的情况时，数字向上舍入，由scale参数指 定精度。此舍入模式始终不会减少计算值的大小。
     *
     * @param valA  被除数
     * @param valB  除数
     * @param scale 精度
     * @return
     */
    public static double divUp(double valA, double valB, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("精确度不能小于0！");
        }

        BigDecimal a = new BigDecimal(Double.toString(valA));
        BigDecimal b = new BigDecimal(Double.toString(valB));
        return a.divide(b, scale, BigDecimal.ROUND_UP).doubleValue();
    }

    /**
     * 除法运算。当发生除不尽的情况时，数字向下舍入，由scale参数指 定精度。此舍入模式始终不会增加计算值的大小。
     *
     * @param valA  被除数
     * @param valB  除数
     * @param scale 精度
     * @return
     */
    public static double divDown(double valA, double valB, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("精确度不能小于0！");
        }

        BigDecimal a = new BigDecimal(Double.toString(valA));
        BigDecimal b = new BigDecimal(Double.toString(valB));
        return a.divide(b, scale, BigDecimal.ROUND_DOWN).doubleValue();
    }

    /**
     * 四舍五入。由scale参数指 定精度。
     *
     * @param val   原始数字
     * @param scale
     * @return 四舍五入后的数字
     */
    public static double round(double val, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("精确度不能小于0！");
        }

        BigDecimal b = new BigDecimal(Double.toString(val));
        return b.setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }
}
