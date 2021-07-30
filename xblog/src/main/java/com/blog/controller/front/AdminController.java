package com.blog.controller.front;

import com.blog.model.bean.ResultData;
import com.blog.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhengxin
 * @date 2021/7/29
 */
@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    ArticleService articleService;


    /**
     * 对某个用户进行随机
     *
     * @return
     */
    @PostMapping(value = "/random")
    public ResultData random(int count, String label, Long adminId) {

        return ResultData.ok("查询成功!", articleService.getTopicList(count, label, adminId));
    }


    /**
     * 对某个用户进行随机
     *
     * @return
     */
    @PostMapping(value = "/admin/info")
    public ResultData getAdminInfo(int count, String label, Long adminId) {

        return ResultData.ok("查询成功!", articleService.getTopicList(count, label, adminId));
    }
}
