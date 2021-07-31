package com.blog.interceptor;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.blog.model.annotation.PassToken;
import com.blog.model.bean.ResultData;
import com.blog.model.entity.Admin;
import com.blog.service.AdminService;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;

@Slf4j
public class AuthenticationInterceptor implements HandlerInterceptor {

    @Autowired
    AdminService adminService;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object object) throws Exception {

        // 如果不是映射到方法直接通过
        if (!(object instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) object;
        Method method = handlerMethod.getMethod();
        //检查是否有passtoken注释，有则跳过认证
        if (method.isAnnotationPresent(PassToken.class)) {
            PassToken passToken = method.getAnnotation(PassToken.class);
            if (passToken.required()) {
                return true;
            }
        }
        try {
            // 从 http 请求头中取出 token
            String token = httpServletRequest.getHeader("login-token");
            // 执行认证
            if (token == null) {
                responseResult(httpServletResponse, ResultData.error("无token，请重新登录"));
                return false;
            }
            // 获取 token 中的 user id
            String id = JWT.decode(token).getAudience().get(0);
            ResultData<Admin> resultData = adminService.findById(NumberUtils.toLong(id));
            if (!resultData.isOk()) {
                responseResult(httpServletResponse, ResultData.error("用户不存在，请重新登录"));
                return false;
            }
            Admin admin = resultData.getData();
            // 验证 token
            JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(admin.getPassword())).build();
            jwtVerifier.verify(token);
        } catch (JWTDecodeException e) {
            log.error(" 异常", e);
            responseResult(httpServletResponse, ResultData.error("权限验证失败"));
            return false;
        } catch (JWTVerificationException e) {
            log.error(" 异常", e);
            responseResult(httpServletResponse, ResultData.error("权限验证失败"));
            return false;
        } catch (Exception e) {
            log.error(" 异常", e);
            responseResult(httpServletResponse, ResultData.error("用户登录失败"));
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest,
                           HttpServletResponse httpServletResponse,
                           Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest,
                                HttpServletResponse httpServletResponse,
                                Object o, Exception e) throws Exception {
    }


    private void responseResult(HttpServletResponse response, ResultData result) throws IOException {
        String temp = JSONObject.fromObject(result).toString();
        response.setCharacterEncoding("UTF-8");
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.getWriter().flush();
        response.getWriter().write(temp);
        response.getWriter().close();
        log.error(temp);
    }
}