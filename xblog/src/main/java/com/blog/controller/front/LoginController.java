package com.blog.controller.front;

import com.blog.common.constants.AppConstants;
import com.blog.common.utils.EncryptUtil;
import com.blog.controller.common.BaseController;
import com.blog.model.annotation.PassToken;
import com.blog.model.bean.ResultData;
import com.blog.model.entity.Admin;
import com.blog.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhengxin
 * @date 2021/7/27
 */
@RestController
public class LoginController extends BaseController {

    @Autowired
    AdminService adminService;


    @PostMapping("/login")
    @PassToken
    public ResultData login(@RequestBody Admin admin) {
        return adminService.login(admin);
    }

    @PostMapping("/pwd")
    @PassToken
    public ResultData login(@RequestBody String password) {
        return ResultData.data(EncryptUtil.getInstance().MD5(password, AppConstants.PASS_WORD_SALT));
    }

}
