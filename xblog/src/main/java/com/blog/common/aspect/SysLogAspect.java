/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package com.blog.common.aspect;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import com.blog.common.annotation.SysLog;
import com.blog.common.utils.JsonUtil;
import com.blog.common.utils.WebUtils;
import com.blog.service.SysLogService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * 系统日志，切面处理类
 *
 */
@Aspect
@Component
@Slf4j
public class SysLogAspect {
	@Autowired
	private SysLogService sysLogService;

	@Pointcut("@annotation(com.blog.common.annotation.SysLog)")
	public void logPointCut() {

	}

	@Around("logPointCut()")
	public Object around(ProceedingJoinPoint point) throws Throwable {
		long beginTime = System.currentTimeMillis();
		//执行方法
		Object result = point.proceed();
		//执行时长(毫秒)
		long time = System.currentTimeMillis() - beginTime;

		//保存日志
		saveSysLog(point, time);

		return result;
	}

	private void saveSysLog(ProceedingJoinPoint joinPoint, long time) {
        try {
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            Method method = signature.getMethod();

            com.blog.model.entity.SysLog sysLog = new com.blog.model.entity.SysLog();
            SysLog syslog = method.getAnnotation(SysLog.class);
            if(syslog != null){
                //注解上的描述
                sysLog.setOperation(syslog.value());
            }

            //请求的方法名
            String className = joinPoint.getTarget().getClass().getName();
            String methodName = signature.getName();
            sysLog.setMethod(className + "." + methodName + "()");

            //请求的参数
            Object[] args = joinPoint.getArgs();
            if(args!=null && args.length > 0) {
                String params = JsonUtil.bean2Json(args[0]);
                sysLog.setParams(params);
            }

            //获取request
            HttpServletRequest request = WebUtils.getRequest();
            //设置IP地址
            sysLog.setIp(WebUtils.getIp(request));

            //管理员名
            String username = ""; //ShiroUtils.getUserEntity().getUsername();
            sysLog.setUsername(username);

            sysLog.setTime(time);
            sysLog.setCreateDate(LocalDateTime.now());
            //保存系统日志
            sysLogService.save(sysLog);
        } catch (Exception e) {
            log.error(" 保存系统日记异常 !" , e);
        }
    }
}
