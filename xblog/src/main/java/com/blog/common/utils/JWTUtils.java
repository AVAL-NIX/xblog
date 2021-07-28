package com.blog.common.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.blog.model.entity.Admin;

/**
 * @author zhengxin
 * @date 2021/7/27
 */
public final class JWTUtils {

    public static String getToken(Admin admin) {
        return JWT.create().withAudience(admin.getId().toString())
                .sign(Algorithm.HMAC256(admin.getPassword()));
    }
}
