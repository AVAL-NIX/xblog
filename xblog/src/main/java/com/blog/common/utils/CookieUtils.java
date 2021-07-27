package com.blog.common.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import com.blog.common.constants.AppConstants;


/**
 * Cookie 工具类
 *
 * @author zx
 * @date 2019/2/10
 */
public class CookieUtils {

    public static Cookie setCookie(String name, String value, String signKey) {
        String sign = EncryptUtil.getInstance().encodeAes(value, signKey);

        Cookie cookie = new Cookie(name, sign);
        // 7天
        cookie.setMaxAge(AppConstants.COOKIE_EXPIRE);
        cookie.setHttpOnly(true);
        cookie.setPath("/");

        return cookie;
    }

    public static Cookie setCookie(String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setMaxAge(maxAge);
        cookie.setHttpOnly(true);
        cookie.setPath("/");

        return cookie;
    }

    public static Cookie getCookie(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null || cookies.length <= 0) {
            return null;
        }
        for (Cookie cookie : cookies) {
            if (cookie.getName().equalsIgnoreCase(cookieName)) {
                return cookie;
            }
        }
        return null;
    }

    public static Cookie getCookie(HttpServletRequest request, String cookieName, String signKey) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null || cookies.length <= 0) {
            return null;
        }
        for (Cookie cookie : cookies) {
            if (cookie.getName().equalsIgnoreCase(cookieName)) {
                String value = EncryptUtil.getInstance().decodeAes(cookie.getValue(), signKey);
                cookie.setValue(value);
                return cookie;
            }
        }
        return null;
    }
}
