package com.blog.controller.front;

import com.blog.controller.common.BaseController;
import com.blog.model.annotation.PassToken;
import com.blog.model.annotation.SysLog;
import com.blog.model.bean.ResultData;
import com.blog.model.converter.ArticleConverter;
import com.blog.model.dto.request.ArticleDTO;
import com.blog.model.entity.Admin;
import com.blog.model.entity.Article;
import com.blog.service.AdminService;
import com.blog.service.ArticleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 文章前端控制器, 用于VSCODE编辑器
 * </p>
 *
 * @author zhengxin
 * @since 2019-03-29
 */
@RestController
@Slf4j
@RequestMapping("/article")
public class ArticleController extends BaseController {

    @Autowired
    private ArticleService articleService;
    @Autowired
    private AdminService adminService;

    /**
     * 添加文章数据
     *
     * @param articleDTO
     * @return
     */
    @PostMapping(value = "/public")
    @SysLog("添加文章数据")
    @PassToken
    public ResultData addArticle(ArticleDTO articleDTO) {
        // 获取用户
        ResultData r = getAdminByAccessToken();
        if (!r.isOk()) {
            r.setData(articleDTO);

            return r;
        }

        articleDTO.setAdminId(((Admin) r.getData()).getId());
        ResultData result = articleService.save(articleDTO);
        articleDTO.setSubmitToken(result.getData().toString());
        result.setData(articleDTO);
        return result;
    }


    /**
     * 搜索文章数据
     *
     * @return
     */
    @PostMapping(value = "/search")
    @SysLog("搜索文章数据")
    @PassToken
    public ResultData searchArticle(ArticleDTO articleDTO) {
        // 获取用户
        ResultData r = getAdminByAccessToken();
        if (!r.isOk()) {
            r.setData(articleDTO);

            return r;
        }

        ResultData<Page<Article>> r2 = articleService.page(PageRequest.of(0, 10), articleDTO);
        List<ArticleDTO> articleVOList = new ArrayList<>();
        if (r2.getData().getContent().size() > 0) {
            for (Article article : r2.getData().getContent()) {
                articleVOList.add(ArticleConverter.objToDto(article));
            }
        }
        return ResultData.ok("查询成功!", articleVOList);
    }


}
