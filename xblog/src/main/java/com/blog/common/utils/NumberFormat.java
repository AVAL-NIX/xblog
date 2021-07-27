package com.blog.common.utils;

import java.text.DecimalFormat;


/**
 * 数字格式化
 *
 * @author zx
 * @date 2019/2/10
 */
public class NumberFormat {

    /**
     * 中文大写数字
     */
    private static final char[] CN_NUMBERS = {'零', '壹', '贰', '叁', '肆', '伍', '陆', '柒', '捌', '玖'};

    /**
     * 中文大写金额单位
     */
    private static final char[] SERIES = {'分', '角', '元', '拾', '百', '仟', '万', '拾', '百', '仟', '亿'};

    /**
     * 四舍五入，由scale参数指 定精度
     *
     * @param val   原始数据
     * @param scale
     * @return
     * @author hys
     * @createDate 2015年12月1日 下午3:54:03
     */
    public static String round(int val, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("精确度不能小于0！");
        }

        String format = "%." + scale + "f";

        return String.format(format, val);
    }

    /**
     * 四舍五入，由scale参数指 定精度 (控制小数点位数)
     *
     * @param val   原始数据
     * @param scale
     * @return
     * @author hys
     * @createDate 2015年12月1日 下午3:54:03
     */
    public static String round(double val, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("精确度不能小于0！");
        }

        String format = "%." + scale + "f";

        return String.format(format, val);
    }

    /**
     * double数值格式化。该方法会进行四舍五入处理
     *
     * @param val    double数值
     * @param format 格式。   “0”表示“ 一个数字”；“#”表示“一个数字，不包括 0”
     * @return
     */
    public static String format(double val, String format) {
        DecimalFormat myformat = new DecimalFormat();
        myformat.applyPattern(format);

        return myformat.format(val);
    }

    /**
     * 金额格式化，格式：##,###.00 。该方法会对千分位进行四舍五入处理
     *
     * @param amount
     * @return
     * @author hys
     * @createDate 2015年12月1日 下午4:05:11
     */
    public static String financeEN(double amount) {
        DecimalFormat myformat = new DecimalFormat();
        myformat.applyPattern("##,##0.00");

        return myformat.format(amount);
    }

    /**
     * 金额格式化，格式：0.00 。该方法会对千分位进行四舍五入处理
     *
     * @param amount
     * @return
     * @author hys
     * @createDate 2015年12月1日 下午4:05:11
     */
    public static String finance(double amount) {
        DecimalFormat myformat = new DecimalFormat();
        myformat.applyPattern("0.00");

        return myformat.format(amount);
    }

    /**
     * 金额单位化输出。格式：####，####.00万，####.0000亿，####.0000万亿
     *
     * @param amount
     * @return
     */
    public static String financeShort(double amount) {
        String result = null;

        if (amount < 10000) {
            result = String.format("%.2f", amount);
        } else if (10000 <= amount && amount < 100000000) {
            result = ArithUtils.round(amount / 10000, 2) + "万";
        } else if (100000000 <= amount && amount < 1000000000000.00) {
            result = ArithUtils.round(amount / 100000000, 4) + "亿";
        } else {
            result = ArithUtils.round(amount / 1000000000000.00, 4) + "万亿";
        }

        return result;
    }

    /**
     * 金额格式化。格式：中文大写
     * <br>如：100 -> 壹佰元
     *
     * @param amount
     * @return
     */
    public static String financeCN(double amount) {

        StringBuffer result = new StringBuffer();

        /** 格式金额，这里会保留两位小数，四舍五入 */
        DecimalFormat myformat = new DecimalFormat();
        myformat.applyPattern("#.00");
        String amountStr = myformat.format(amount);

        String numStr = amountStr.replace(".", "");
        int length = numStr.length();

        for (int i = 0; i < length; i++) {
            int num = Integer.parseInt(String.valueOf(numStr.charAt(i)));

            result.append(CN_NUMBERS[num]);
            result.append(SERIES[length - 1 - i]);
        }

        return result.toString();
    }

}
