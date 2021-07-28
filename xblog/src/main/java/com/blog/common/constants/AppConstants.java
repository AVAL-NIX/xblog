package com.blog.common.constants;

/**
 * 静态常量类
 *
 * @author zx
 * @date 2019/1/28
 */
public final class AppConstants {

    /**
     * session 有效期
     **/
    public static final String USER_SESSION = "admin_serssion";

    /**
     * 翻页默认参数
     **/
    public static int PAGE_DEFAULT_PAGE = 1;
    public static int PAGE_DEFAULT_LIMIT = 20;

    /**
     * 登录
     **/
    public static final String LOGIN = "/back/login";

    /**
     * 主页
     **/
    public static final String HOME = "/home/";

    /**
     * cookie 有效期
     **/
    public static int COOKIE_EXPIRE = 7 * 24 * 60 * 60;


    /**
     * 时间格式
     **/
    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";


    /**
     * 上传文件路径
     **/
    public static final String UPLOAD_PATH = "/static/static-front/upload/";


    /**
     * 密码盐
     */
    public static final String PASS_WORD_SALT = "AJFDKLASJFLSADF";


    private AppConstants() {
    }

}
