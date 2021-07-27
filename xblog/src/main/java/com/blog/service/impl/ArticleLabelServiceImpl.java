package com.blog.service.impl;

import com.blog.dao.ArticleLabelDao;
import com.blog.model.bean.R;
import com.blog.model.entity.ArticleLabel;
import com.blog.service.ArticleLabelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author zhengxin
 * @since 2019-03-29
 */
@Service
@Slf4j
public class ArticleLabelServiceImpl implements ArticleLabelService {

    @Autowired
    ArticleLabelDao articleLabelDao;

    @Override
    public R deleteByArticleId(Long id) {
        int count = articleLabelDao.deleteByArticleId(id);
        log.info(" 删除的行： {0} ", count);
        return R.ok();
    }

    @Override
    public R saveByTagsAndArticleId(List<Long> tagIds, long articleId) {
        boolean flag = false;
        //标签与文章的关系
        for (Long tagId : tagIds) {
            ArticleLabel articleLabel = articleLabelDao.findByArticleIdAndLabelId(articleId, tagId);
            if (articleLabel == null) {
                articleLabel = new ArticleLabel();
                articleLabel.setArticleId(articleId);
                articleLabel.setLabelId(tagId);
                articleLabel = articleLabelDao.save(articleLabel);
                if (articleLabel == null) {
                    return R.error();
                }
            } else {
                articleLabel.setArticleId(articleId);
                articleLabel.setUpdateDate(LocalDateTime.now());
                articleLabel = articleLabelDao.save(articleLabel);
                if (articleLabel == null) {
                    return R.error();
                }
            }

        }

        return R.ok("添加标签成功!");
    }
}
