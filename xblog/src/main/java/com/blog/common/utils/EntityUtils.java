package com.blog.common.utils;

import com.blog.common.constants.SystemConstants;
import org.apache.commons.lang.StringUtils;

/**
 * 实体工具类
 *
 * @author zx
 * @date 2019/2/10
 */
public class EntityUtils {

    /** 加密KEY **/
    private static String signKey = SystemConstants.DESKEY;

    /** 有效时长30分钟 **/
    private static long validTime = 1000 * 60 * 60 * 30;

    private EntityUtils(){}

    /**
     * ID 加签
     * 
     * @param id 主键ID
     * @return 签名
     */
    public static String encodeSign(long id) {
        if (id <= 0) {
            return null;
        }
        long expire = System.currentTimeMillis() + validTime;
        return EncryptUtil.getInstance().encodeAes(id + "," + expire, signKey);
    }

    /**
     * ID 解签
     *
     * @param sign 签名
     * @return ID
     */
    public static long decodeSign(String sign) {
        if (StringUtils.isBlank(sign)) {
            return 0L;
        }

        String plainTxt = EncryptUtil.getInstance().decodeAes(sign, signKey);
        if (StringUtils.isBlank(plainTxt)) {
            return 0L;
        }

        String[] split = StringUtils.split(plainTxt, ",");
        long expire = Long.valueOf(split[1]);
        if (System.currentTimeMillis() > expire) {
            return 0L;
        }

        return Long.valueOf(split[0]);
    }
}
