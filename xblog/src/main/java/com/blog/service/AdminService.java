package com.blog.service;

import com.blog.model.bean.R;
import com.blog.service.impl.AdminServiceImpl;

/**
 * {@link AdminServiceImpl}
 * 用户Servcie
 *
 * @author zx
 * @date 2018/12/23
 */
public interface AdminService {


    R extis(String submitToken);
}
