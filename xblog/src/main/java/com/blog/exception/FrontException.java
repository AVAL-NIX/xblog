package com.blog.exception;

import com.blog.model.bean.ResultData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 描述：异常页面
 *
 * @author Avalon
 * @date 2019/3/29
 */
@Slf4j
@ControllerAdvice(basePackages = "com.blog.controller.front")
public class FrontException {

    @ExceptionHandler(ResultException.class)
    public @ResponseBody
    ResultData handleShiroException(ResultException ex) {
        return ResultData.error(ex.getMsg());
    }

    @ExceptionHandler(Exception.class)
    public @ResponseBody
    ResultData exception(Exception ex) {
        return ResultData.error(ex.getMessage());
    }
}
