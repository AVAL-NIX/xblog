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

    @ExceptionHandler(Exception.class)
    public @ResponseBody
    ResultData exception(Exception ex) {
        if (ex instanceof ResultException) {
            return ResultData.error(((ResultException) ex).getMsg());
        }
        return ResultData.error(ex.getMessage());
    }
}
