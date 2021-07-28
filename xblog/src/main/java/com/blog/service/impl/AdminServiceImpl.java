package com.blog.service.impl;

import com.blog.common.constants.AppConstants;
import com.blog.common.utils.EncryptUtil;
import com.blog.common.utils.JWTUtils;
import com.blog.dao.AdminDao;
import com.blog.exception.ResultException;
import com.blog.model.bean.ResultData;
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
    public ResultData extis(String submitToken) {
        Admin admin = adminDao.findBySubmitToken(submitToken);
        if (admin == null) {

            return ResultData.error("用户不存在");
        }

        return ResultData.data(admin);
    }

    @Override
    public ResultData findById(Long id) {
        return ResultData.data(adminDao.findById(id));
    }


    @Override
    public ResultData findByName(String username) {
        return ResultData.data(adminDao.findByUsername(username));
    }

    @Override
    public ResultData login(Admin admin) {
        // 校验密码
        Admin admin1 = adminDao.findByUsername(admin.getUsername());
        if (admin1 == null) {
            throw new ResultException(" 用户不存在！");
        }
        if (!admin1.getPassword().equals(EncryptUtil.getInstance().MD5(admin.getPassword(), AppConstants.PASS_WORD_SALT))) {
            throw new ResultException(" 密码不正确！");
        }

        return ResultData.data(JWTUtils.getToken(admin1));
    }
}
