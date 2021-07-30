package com.blog.service.impl;

import com.blog.common.constants.CacheKey;
import com.blog.common.constants.SystemConstants;
import com.blog.common.utils.CacheUtils;
import com.blog.common.utils.EncryptUtil;
import com.blog.common.utils.StrUtil;
import com.blog.dao.ArticleAdminDao;
import com.blog.dao.ArticleDao;
import com.blog.model.bean.ResultData;
import com.blog.model.converter.ArticleConverter;
import com.blog.model.dto.request.ArticleDTO;
import com.blog.model.entity.Article;
import com.blog.model.enums.ArticleStatus;
import com.blog.service.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.util.ArrayList;
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

    @Autowired
    protected ArticleAdminDao articleUserDao;

    @Autowired
    protected CacheUtils cacheUtils;


    @Override
    @Transactional
    public ResultData updateById(ArticleDTO articleDTO) {
        // 新增频道
        ResultData<Long> r = channelService.saveByName(articleDTO.getChannel());
        if (r.getCode() < 1) {

            return r;
        }
        Long channelId = r.getData();
        // 新增标签 , 标签绑定的频道不删除
        ResultData<List<Long>> r1 = labelService.saveByName(channelId, articleDTO.getLabels());
        if (r.getCode() < 1) {

            return r;
        }
        List<Long> tagIds = r1.getData();
        // 修改文章
        Article article = articleDao.findBySubmitToken(articleDTO.getSubmitToken());
        if (article == null) {

            return ResultData.error("文章不存在!");
        }
        article.setTitle(articleDTO.getTitle());
        article.setUpdateDate(LocalDateTime.now());
        article.setLabels(articleDTO.getLabels());
        article.setChannel(articleDTO.getChannel());
        article.setImages(StrUtil.findImageByContent(articleDTO.getContent()));
        article.setIntro(StrUtil.findIntro(articleDTO.getContent()));
        Article article1 = articleDao.save(article);
        if (article1 == null) {

            return ResultData.error();
        }
        // 先全部删除文章标签,在添加
        r = articleLabelService.deleteByArticleId(article.getId());
        if (!r.isOk()) {

            return ResultData.error();
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

                return ResultData.error("新增文章详细失败!");
            }
        }

        return ResultData.ok("更新文章成功!", article.getSubmitToken());
    }

    @Override
    @Transactional
    public ResultData updateBySign(ArticleDTO articleDTO) {
        // 修改文章
        Article article = articleDao.findBySubmitToken(articleDTO.getSubmitToken());
        if (article == null) {

            return ResultData.error("文章不存在!");
        }
        article = ArticleConverter.dtoToObj(articleDTO, article);
        article.setUpdateDate(LocalDateTime.now());
        article.setImages(StrUtil.findImageByContent(articleDTO.getContent()));
        article.setIntro(StrUtil.findIntro(articleDTO.getContent()));
        Article article1 = articleDao.save(article);
        if (article1 == null) {

            return ResultData.error("更新文章失败!");
        }

        return ResultData.ok("更新文章成功!", article.getSubmitToken());
    }


    @Override
    @Transactional
    public ResultData save(ArticleDTO articleDTO) {
        // 新增文章
        Article article = ArticleConverter.dtoToObj(articleDTO);
        article.setCreateDate(LocalDateTime.now());
        article.setUpdateDate(null);
        article.setSalt(RandomStringUtils.randomAlphanumeric(16));
        article.setSubmitToken(EncryptUtil.getInstance().encodeAes(article.getSalt(), SystemConstants.DESKEY));
        article.setImages(StrUtil.findImageByContent(articleDTO.getContent()));
        article.setIntro(StrUtil.findIntro(articleDTO.getContent()));
        article.setStatus(ArticleStatus.PUBLISH.getCode());
        Article article1 = articleDao.save(article);
        if (article1 == null) {

            return ResultData.error();
        }

        // 新增文章详细
        ResultData r = articleDetailService.save(article.getId());
        if (!r.isOk()) {
            return ResultData.error("新增文章详细失败!");
        }

        return ResultData.ok("发布文章成功!", article.getSubmitToken());
    }

    @Override
    public ResultData page(PageRequest page, ArticleDTO article) {
        Page<Article> all = articleDao.findAll(
                new Specification<Article>() {
                    @Override
                    public Predicate toPredicate(Root<Article> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                        List<Predicate> predicates = new ArrayList<>();
                        if (StringUtils.isNotBlank(article.getChannel())) {
                            Predicate predicate = criteriaBuilder.like(root.get("channel").as(String.class), "%" + article.getChannel() + "%");
                            predicates.add(predicate);
                        }

                        if (StringUtils.isNotBlank(article.getTitle())) {
                            Predicate predicate = criteriaBuilder.like(root.get("title").as(String.class), "%" + article.getTitle() + "%");
                            predicates.add(predicate);
                        }

                        return criteriaBuilder.and(predicates.toArray(new Predicate[]{}));
                    }
                }, page);
        return ResultData.page(all);
    }


    @Override
    public ResultData listTopicCount() {
        Object result = cacheUtils.get(CacheUtils.TWENTYFOUR_HOURS, CacheKey.CACHE_TOPIC_COUNT_KEY);
        if (result != null) {
            return ResultData.data(NumberUtils.toInt(result.toString()));
        } else {
            int count = articleDao.countByTopic();
            cacheUtils.set(CacheUtils.TWENTYFOUR_HOURS, CacheKey.CACHE_TOPIC_COUNT_KEY, count);
            return ResultData.data(count);
        }
    }

    @Override
    public ResultData getTopicList(int count, String label, Long adminId) {
        List<Article> data = articleDao.getTopicList(count, label, adminId);
        if (data.size() == 0) {
            return ResultData.ok();
        }
        if (data.size() < count) {
            return ResultData.data(data);
        }
        //进行随机 
        Article[] numbers = data.stream().toArray(Article[]::new);
        // 结果集
        List<Article> result = new ArrayList<>();
        int n = count;
        for (int i = 0; i < n; i++) {
            int r = (int) (Math.random() * (data.size() - i));
            result.add(numbers[r]);
            // 排除已经取过的值
            numbers[r] = numbers[count - 1];
            count--;
        }

        return ResultData.data(result);
    }

    @Override
    public ResultData findById(Long id) {
        return ResultData.data(articleDao.findById(id));
    }

    @Override
    public ResultData list() {
        return ResultData.data(articleDao.findAll());
    }

    @Override
    public ResultData findBySubmitToken(String submitToken) {
        return ResultData.data(articleDao.findBySubmitToken(submitToken));
    }
}
