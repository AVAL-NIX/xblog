package com.blog.service.impl;

import com.blog.dao.AdminDao;
import com.blog.model.bean.R;
import com.blog.model.entity.Admin;
import com.blog.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 用户Service 实现类
 *
 * @author zx
 * @date 2018/12/23
 */
@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    AdminDao adminDao;

    @Override
    public R extis(String submitToken) {
        Admin admin = adminDao.findBySubmitToken(submitToken);
        if (admin == null) {
            return R.error();
        }
        return R.data(admin);
    }
}
