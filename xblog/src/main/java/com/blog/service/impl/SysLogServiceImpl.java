package com.blog.service.impl;

import com.blog.dao.SysLogDao;
import com.blog.model.bean.ResultData;
import com.blog.model.entity.SysLog;
import com.blog.service.SysLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 系统日志 服务实现类
 * </p>
 *
 * @author zhengxin
 * @since 2019-06-24
 */
@Service
public class SysLogServiceImpl implements SysLogService {

    @Autowired
    SysLogDao sysLogDao;

    @Override
    public ResultData save(SysLog sysLog) {
        return ResultData.data(sysLogDao.save(sysLog));
    }
}
