package com.blog.common.utils;


import org.apache.commons.lang3.StringUtils;

import java.security.InvalidParameterException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期工具类
 *
 * @author zx
 * @date 2018/8/8
 */
public class DateUtil {

    /**
     * 日期格式化
     *
     * @param date   日期
     * @param format 格式
     * @return
     */
    public static Date dateToDate(Date date, String format) {
        if (date == null) {
            throw new InvalidParameterException("date cannot be null!");
        }
        Date newDate = new Date();
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
            String dateStr = simpleDateFormat.format(date);
            newDate = simpleDateFormat.parse(dateStr);
        } catch (ParseException e) {
            throw new RuntimeException("parse error! [dateString: format:" + format + "]");
        }

        return newDate;
    }

    /**
     * 日期格式化
     *
     * @param date   日期
     * @return
     */
    public static Timestamp strToTimestamp(String date) {
        if (StringUtils.isBlank(date)) {
            throw new InvalidParameterException("date cannot be null!");
        }
        try {
            SimpleDateFormat sdf  = new SimpleDateFormat("yyyy-MM-dd");
            Date date2 = sdf.parse(date);
            return Timestamp.valueOf(dateToStr(date2 , "yyyy-MM-dd hh:mm:ss"));
        } catch (Exception e) {
            throw new RuntimeException("parse error! [dateString: date:" + date + "]");
        }
    }

    /**
     * 日期格式化 并输出字符串
     *
     * @param date   日期
     * @param format 格式
     * @return str
     */
    public static String dateToStr(Date date, String format) {
        return new SimpleDateFormat(format).format(date);
    }

    /**
     * 字符串转换成Date
     *
     * @param date 日期
     * @param fromat 格式
     * @return Date
     */
    public static Date strToDate(String date , String fromat){
        if (StringUtils.isBlank(date)) {
            throw new InvalidParameterException("date cannot be null!");
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(fromat);
        Date date2 = null;
        try {
            date2 = simpleDateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date2;
    }
}




