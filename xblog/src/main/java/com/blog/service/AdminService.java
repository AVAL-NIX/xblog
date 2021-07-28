package com.blog.service;

import com.blog.model.bean.ResultData;
import com.blog.model.entity.Admin;
import com.blog.service.impl.AdminServiceImpl;

/**
 * {@link AdminServiceImpl}
 * 用户Servcie
 *
 * @author zx
 * @date 2018/12/23
 */
public interface AdminService {


    ResultData extis(String submitToken);

    ResultData findById(Long id);

    ResultData findByName(String username);

    ResultData login(Admin admin);
}
