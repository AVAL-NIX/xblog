package com.blog.controller.front;

import com.blog.model.bean.ResultData;
import com.blog.model.dto.response.AdminResponseDTO;
import com.blog.service.ArticleAdminService;
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
    @Autowired
    ArticleAdminService articleAdminService;


    /**
     * 对某个用户进行随机
     *
     * @return
     */
    @PostMapping(value = "/random")
    public ResultData random(int count, String label, Long adminId) {

        return articleService.getTopicList(count, label, adminId);
    }

    /**
     * 获取用户信息
     *
     * @return
     */
    @PostMapping(value = "/topic/info")
    public ResultData getTopicInfo(Long adminId) {
        ResultData<Integer> resultData = articleService.listTopicCount();
        ResultData<Integer> resultData2 = articleAdminService.getCountByAdminId(adminId);
        return ResultData.ok("查询成功!", new AdminResponseDTO(resultData.getData(), resultData2.getData()));
    }
}
