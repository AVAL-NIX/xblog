package com.blog.controller.front;

import com.blog.model.bean.ResultData;
import com.blog.model.entity.ArticleAdminDTO;
import com.blog.service.ArticleAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhengxin
 * @date 2021/7/29
 */
@RestController
@RequestMapping("/article-admin")
public class ArticleAdminController {


    @Autowired
    ArticleAdminService articleAdminService;


    /**
     * 对某个用户进行随机
     *
     * @return
     */
    @PostMapping(value = "/save")
    public ResultData random(@RequestBody ArticleAdminDTO articleAdminDTO) {

        return articleAdminService.save(articleAdminDTO);
    }

}
