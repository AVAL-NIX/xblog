package com.blog.service.impl;

import com.blog.dao.ArticleDetailDao;
import com.blog.model.bean.ResultData;
import com.blog.model.entity.ArticleDetail;
import com.blog.service.ArticleDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author zhengxin
 * @since 2019-08-02
 */
@Service
public class ArticleDetailServiceImpl implements ArticleDetailService {

    @Autowired
    protected ArticleDetailDao articleDetailDao;

    @Override
    public ResultData updateViewCount(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return ResultData.error("IDS为空!");
        }

        int row = articleDetailDao.updateArticleViewCount(ids);
        if (row <= 0) {
            return ResultData.error("更新文章浏览量失败");
        }

        return ResultData.ok("成功!");
    }

    @Override
    public ResultData updateUpCount(List<Long> ids) {
        int row = articleDetailDao.updateArticleUpCount(ids);
        if (row <= 0) {
            return ResultData.error("更新点赞失败");
        }

        return ResultData.ok("成功!");
    }

    @Override
    public ResultData updateDownCount(List<Long> ids) {
        int row = articleDetailDao.updateArticleDownCount(ids);
        if (row <= 0) {
            return ResultData.error("更新踩失败");
        }

        return ResultData.ok("成功!");
    }

    @Override
    public ResultData save(Long articleId) {
        ArticleDetail articleDetail = new ArticleDetail();
        articleDetail.setArticleId(articleId);
        articleDetail = articleDetailDao.save(articleDetail);
        if (articleDetail == null) {

            return ResultData.error("保存文章失败!");
        }
        return ResultData.ok("保存文章成功!");
    }

    @Override
    public ResultData findByArticleId(Long id) {
        ArticleDetail articleDetail = articleDetailDao.findByArticleId(id);
        if (articleDetail == null) {

            return ResultData.error("查询文章失败!");
        }
        return ResultData.data(articleDetail);
    }
}
