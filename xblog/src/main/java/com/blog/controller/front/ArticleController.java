package com.blog.controller.front;

import com.blog.common.annotation.SysLog;
import com.blog.controller.common.BaseController;
import com.blog.model.bean.R;
import com.blog.model.converter.ArticleConverter;
import com.blog.model.dto.request.ArticleDTO;
import com.blog.model.entity.Admin;
import com.blog.model.entity.Article;
import com.blog.service.AdminService;
import com.blog.service.ArticleService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
    public R addArticle(ArticleDTO articleDTO) {
        // 获取用户
        R r = getAdminByAccessToken();
        if (!r.isOk()) {
            r.setData(articleDTO);

            return r;
        }

        articleDTO.setAdminId(((Admin) r.getData()).getId());
        // 如果频道或者标签是空, 则直接对文章进行保存
        if (StringUtils.isBlank(articleDTO.getSubmitToken()) && (StringUtils.isBlank(articleDTO.getChannel())
                || StringUtils.isBlank(articleDTO.getLabels()))) {
            r = articleService.save(articleDTO);
            articleDTO.setSubmitToken(r.getData() + "");
            r.setData(articleDTO);

            return r;
        }

        if (StringUtils.isNotBlank(articleDTO.getSubmitToken()) && (StringUtils.isBlank(articleDTO.getChannel())
                || StringUtils.isBlank(articleDTO.getLabels()))) {
            r = articleService.updateBySign(articleDTO);
            r.setData(articleDTO);

            return r;
        }

        // 频道,标签不是空
        R r1 = articleService.findBySubmitToken(articleDTO.getSubmitToken());
        if (r1.getData() == null) {
            // 新增文章
            r = articleService.save(articleDTO);
            articleDTO.setSubmitToken(r.getData() + "");
            r.setData(articleDTO);
        } else {
            // 修改
            r = articleService.updateById(articleDTO);
        }
        r.setData(articleDTO);
        return r;
    }


    /**
     * 搜索文章数据
     *
     * @return
     */
    @PostMapping(value = "/search")
    @SysLog("搜索文章数据")
    public R searchArticle(ArticleDTO articleDTO) {
        // 获取用户
        R r = getAdminByAccessToken();
        if (!r.isOk()) {
            r.setData(articleDTO);

            return r;
        }

        R<List<Article>> r2 = articleService.page(PageRequest.of(1, 10), articleDTO);
        List<ArticleDTO> articleVOList = new ArrayList<>();
        if (r2.getData().size() > 0) {
            for (Article article : r2.getData()) {
                articleVOList.add(ArticleConverter.objTodto(article));
            }
        }
        return R.ok("查询成功!", articleVOList);
    }


}
