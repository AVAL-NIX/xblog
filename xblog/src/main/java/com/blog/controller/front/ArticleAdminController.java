package com.blog.controller.front;

import com.blog.model.bean.ResultData;
import com.blog.model.entity.ArticleAdminDTO;
import com.blog.service.ArticleAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    /**
     * 对某个用户进行重新开始
     *
     * @return
     */
    @GetMapping(value = "/del/all")
    public ResultData delAll(Long adminId) {

        return articleAdminService.deleteByAdminId(adminId);
    }

}
