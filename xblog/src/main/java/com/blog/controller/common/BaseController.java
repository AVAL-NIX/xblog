package com.blog.controller.common;

import com.blog.common.utils.CacheUtils;
import com.blog.common.utils.WebUtils;
import com.blog.model.bean.R;
import com.blog.model.entity.Admin;
import com.blog.service.AdminService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 基类控制器
 *
 * @author zx
 * @date 2019/1/28
 */
@Slf4j
@Controller
public class BaseController {

    @Autowired
    protected HttpServletRequest request;

    @Resource
    protected HttpServletResponse response;

    @Autowired
    protected HttpSession session;


    @Autowired
    protected CacheUtils cacheUtils;

    @Autowired
    AdminService adminService;

    /**
     * 获取管理员ID
     *
     * @return
     */
    public R<Admin> getAdminByAccessToken() {
        HttpServletRequest request = WebUtils.getRequest();
        String accessToken = request.getHeader("accessToken");
        return adminService.extis(accessToken);
    }


}
