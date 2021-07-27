package com.blog.service;

import com.blog.model.bean.R;
import com.blog.model.entity.SysLog;

/**
 * <p>
 * 系统日志 服务类
 * </p>
 *
 * @author zhengxin
 * @since 2019-06-24
 */
public interface SysLogService {

    R save(SysLog sysLog);
}
