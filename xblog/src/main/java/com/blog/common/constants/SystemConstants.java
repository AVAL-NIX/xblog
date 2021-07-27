package com.blog.common.constants;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 系统配置
 *
 * @author zx
 * @date 2019/1/29
 */
@Component
public class SystemConstants {
    /** 加密KEY **/
    public static String DESKEY;

    @Value("${blog.desKey}")
    public void setDesKey(String desKey) {
        SystemConstants.DESKEY = desKey;
    }

    /** 是否是本地测试环境 **/
    public static boolean ISLOCAL;

    @Value("${spring.profiles.active}")
    public void setIsLocal(String active) {
        if(active.equals("dev")|| active.equals("prod")){
            SystemConstants.ISLOCAL = true;
        }else{
            SystemConstants.ISLOCAL = false;
        }
    }

}
