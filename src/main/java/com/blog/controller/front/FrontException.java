package com.blog.controller.front;

import com.blog.common.constants.AppConstants;
import com.blog.controller.base.BaseController;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * 描述：无权限页面
 *
 * @author Avalon
 * @date 2019/3/29
 */
@Slf4j
@ControllerAdvice(basePackages = "com.blog.controller.front" )
public class FrontException extends BaseController {

    @ExceptionHandler(UnauthorizedException.class)
    public String handleShiroException(Exception ex) {
        return "redirect:/front/error";
    }

    @ExceptionHandler(Exception.class)
    public String exception(Exception ex){
        log.error("异常",ex);
        return "redirect:/front/error";
    }
}
