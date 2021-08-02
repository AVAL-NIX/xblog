package com.blog.service.impl;

import com.blog.common.constants.CacheKey;
import com.blog.common.constants.SystemConstants;
import com.blog.common.utils.CacheUtils;
import com.blog.common.utils.EncryptUtil;
import com.blog.common.utils.StrUtil;
import com.blog.dao.ArticleAdminDao;
import com.blog.dao.ArticleDao;
import com.blog.model.bean.ResultData;
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
    public ResultData save(ArticleDTO articleDTO) {
        // 新增频道
        Long channelId = null;
        if (StringUtils.isNotBlank(articleDTO.getChannel())) {
            ResultData<Long> r = channelService.saveByName(articleDTO.getChannel());
            if (r.getCode() < 1) {

                return r;
            }
            channelId = r.getData();
        }
        List<Long> tagIds = new ArrayList<>();
        if (channelId != null && StringUtils.isNotBlank(articleDTO.getLabels())) {
            // 新增标签 , 标签绑定的频道不删除
            ResultData<List<Long>> r = labelService.saveByName(channelId, articleDTO.getLabels());
            if (r.getCode() < 1) {

                return r;
            }
            tagIds = r.getData();
        }

        // 修改文章
        Article article = articleDao.findBySubmitToken(articleDTO.getSubmitToken());
        //如果数据库没有，是新增
        boolean addFlag = false;
        if (article == null) {
            addFlag = true;
            article = new Article();
            article.setSalt(RandomStringUtils.randomAlphanumeric(16));
            article.setSubmitToken(EncryptUtil.getInstance().encodeAes(article.getSalt(), SystemConstants.DESKEY));
            article.setStatus(ArticleStatus.PUBLISH.getCode());
            article.setAdminId(articleDTO.getAdminId());
        }
        article.setTitle(articleDTO.getTitle());
        article.setContent(articleDTO.getContent());
        article.setLabels(articleDTO.getLabels());
        article.setChannel(articleDTO.getChannel());
        article.setUpdateDate(LocalDateTime.now());
        article.setImages(StrUtil.findImageByContent(articleDTO.getContent()));
        article.setIntro(StrUtil.findIntro(articleDTO.getContent()));
        Article article1 = articleDao.save(article);
        if (article1 == null) {

            return ResultData.error(" 文章保存失败 ");
        }
        // 先全部删除文章标签,在添加
        ResultData<Long> r = articleLabelService.deleteByArticleId(article1.getId());
        if (!r.isOk()) {

            return ResultData.error();
        }
        //添加
        r = articleLabelService.saveByTagsAndArticleId(tagIds, article1.getId());
        if (r.getCode() < 1) {

            return r;
        }
        //检查有没有文章详细
        r = articleDetailService.findByArticleId(article1.getId());
        if (r.getData() == null) {
            // 新增文章详细
            r = articleDetailService.save(article1.getId());
            if (!r.isOk()) {

                return ResultData.error("新增文章详细失败!");
            }
        }

        return ResultData.ok(addFlag ? "保存文章成功! " : "更新文章成功 !", article1.getSubmitToken());
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
        List<Article> data = new ArrayList<>();
        if (StringUtils.isBlank(label)) {
            data = articleDao.getTopicList(adminId);
        } else {
            data = articleDao.getTopicList(label, adminId);
        }
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
