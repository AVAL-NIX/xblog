package com.blog.service.impl;

import com.blog.common.constants.SystemConstants;
import com.blog.common.utils.EncryptUtil;
import com.blog.common.utils.StrUtil;
import com.blog.dao.ArticleDao;
import com.blog.model.bean.R;
import com.blog.model.converter.ArticleConverter;
import com.blog.model.dto.request.ArticleDTO;
import com.blog.model.entity.Article;
import com.blog.service.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    protected LabelService labelService;
    @Autowired
    protected ChannelService channelService;
    @Autowired
    protected ArticleLabelService articleLabelService;
    @Autowired
    protected ArticleDao articleDao;
    @Autowired
    protected ArticleDetailService articleDetailService;

    @Override
    @Transactional
    public R updateById(ArticleDTO articleDTO) {
        // 新增频道
        R<Long> r = channelService.saveByName(articleDTO.getChannel());
        if (r.getCode() < 1) {

            return r;
        }
        Long channelId = r.getData();
        // 新增标签 , 标签绑定的频道不删除
        R<List<Long>> r1 = labelService.saveByName(channelId, articleDTO.getLabels());
        if (r.getCode() < 1) {

            return r;
        }
        List<Long> tagIds = r1.getData();
        // 修改文章
        Article article = articleDao.findBySubmitToken(articleDTO.getSubmitToken());
        if (article == null) {

            return R.error("文章不存在!");
        }
        article.setUpdateDate(LocalDateTime.now());
        article.setLabels(articleDTO.getLabels());
        article.setChannel(articleDTO.getChannel());
        article.setImages(StrUtil.findImageByContent(articleDTO.getContent()));
        article.setIntro(StrUtil.findIntro(articleDTO.getContent()));
        Article article1 = articleDao.save(article);
        if (article1 == null) {

            return R.error();
        }
        // 先全部删除文章标签,在添加
        r = articleLabelService.deleteByArticleId(article.getId());
        if (!r.isOk()) {

            return R.error();
        }
        //添加
        r = articleLabelService.saveByTagsAndArticleId(tagIds, article.getId());
        if (r.getCode() < 1) {

            return r;
        }
        //检查有没有文章详细
        r = articleDetailService.findByArticleId(article.getId());
        if (r.getData() == null) {
            // 新增文章详细
            r = articleDetailService.save(article.getId());
            if (!r.isOk()) {

                return R.error("新增文章详细失败!");
            }
        }

        return R.ok("更新文章成功!", article.getSubmitToken());
    }

    @Override
    @Transactional
    public R updateBySign(ArticleDTO articleDTO) {
        // 修改文章
        Article article = articleDao.findBySubmitToken(articleDTO.getSubmitToken());
        if (article == null) {

            return R.error("文章不存在!");
        }
        article = ArticleConverter.dtoToObj(articleDTO, article);
        article.setUpdateDate(LocalDateTime.now());
        article.setImages(StrUtil.findImageByContent(articleDTO.getContent()));
        article.setIntro(StrUtil.findIntro(articleDTO.getContent()));
        Article article1 = articleDao.save(article);
        if (article1 == null) {

            return R.error("更新文章失败!");
        }

        return R.ok("更新文章成功!", article.getSubmitToken());
    }


    @Override
    @Transactional
    public R save(ArticleDTO articleDTO) {
        // 新增文章
        Article article = ArticleConverter.dtoToObj(articleDTO);
        article.setCreateDate(LocalDateTime.now());
        article.setUpdateDate(null);
        article.setSalt(RandomStringUtils.randomAlphanumeric(16));
        article.setSubmitToken(EncryptUtil.getInstance().encodeAes(article.getSalt(), SystemConstants.DESKEY));
        article.setImages(StrUtil.findImageByContent(articleDTO.getContent()));
        article.setIntro(StrUtil.findIntro(articleDTO.getContent()));
        Article article1 = articleDao.save(article);
        if (article1 == null) {

            return R.error();
        }

        // 新增文章详细
        R r = articleDetailService.save(article.getId());
        if (!r.isOk()) {
            return R.error("新增文章详细失败!");
        }

        return R.ok("发布文章成功!", article.getSubmitToken());
    }

    @Override
    public R page(PageRequest page, ArticleDTO article) {
        Page<Article> all = articleDao.findAll(page);
        return R.page(all);
    }


    @Override
    public R findById(Long id) {
        return R.data(articleDao.findById(id));
    }


    @Override
    public R list() {
        return R.data(articleDao.findAll());
    }

    @Override
    public R findBySubmitToken(String submitToken) {
        return R.data(articleDao.findBySubmitToken(submitToken));
    }
}
