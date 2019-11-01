package com.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.blog.common.constants.SystemConstants;
import com.blog.common.utils.EncryptUtil;
import com.blog.common.utils.StrUtil;
import com.blog.model.converter.ArticleConverter;
import com.blog.model.entity.Article;
import com.blog.mapper.ArticleMapper;
import com.blog.model.entity.ArticleDetail;
import com.blog.model.entity.ArticleLabel;
import com.blog.model.form.ArticleForm;
import com.blog.model.dto.ArticleDTO;
import com.blog.model.vo.ArticleVO;
import com.blog.model.bean.R;
import com.blog.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author zhengxin
 * @since 2019-03-29
 */
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {

    @Autowired
    protected LabelService labelService;
    @Autowired
    protected ChannelService channelService;
    @Autowired
    protected ArticleLabelService articleLabelService;
    @Autowired
    protected ArticleMapper articleMapper;

    @Autowired
    protected ArticleDetailService articleDetailService;

    @Override
    @Transactional
    public R updateById(ArticleForm articleForm) {
        // 新增频道
        R<Long> r = channelService.saveByName(articleForm.getChannel());
        if (r.getCode() < 1) {

            return r;
        }
        Long channelId = r.getData();
        // 新增标签 , 标签绑定的频道不删除
        R<List<Long>> r1 = labelService.saveByName(channelId, articleForm.getLabels());
        if (r.getCode() < 1) {

            return r;
        }
        List<Long> tagIds = r1.getData();
        // 修改文章
        QueryWrapper<Article> articleQueryWrapper = new QueryWrapper<>();
        articleQueryWrapper.lambda().eq(Article::getSubmitToken, articleForm.getSubmitToken());
        Article article = this.getOne(articleQueryWrapper);
        if (article == null) {

            return R.error("文章不存在!");
        }
        articleForm.setId(article.getId());
        article = ArticleConverter.formToObj(articleForm, article);
        article.setUpdateDate(LocalDateTime.now());
        article.setLabels(articleForm.getLabels());
        article.setChannel(articleForm.getChannel());
        article.setImages(StrUtil.findImageByContent(articleForm.getContent()));
        article.setIntro(StrUtil.findIntro(articleForm.getContent()));
        boolean flag = this.updateById(article);
        if (!flag) {

            return R.error();
        }
        // 先全部删除文章标签,在添加
        QueryWrapper<ArticleLabel> articleLabelQueryWrapper = new QueryWrapper<>();
        articleLabelQueryWrapper.lambda().eq(ArticleLabel::getArticleId, article.getId());
        flag = articleLabelService.remove(articleLabelQueryWrapper);
        if (!flag) {

            return R.error();
        }
        //添加
        r = articleLabelService.saveByTagsAndArticleId(tagIds, article.getId());
        if (r.getCode() < 1) {

            return r;
        }
        //检查有没有文章详细
        QueryWrapper<ArticleDetail> articleDetailQueryWrapper = new QueryWrapper<>();
        articleDetailQueryWrapper.lambda().eq(ArticleDetail::getArticleId, article.getId());
        ArticleDetail articleDetail = articleDetailService.getOne(articleDetailQueryWrapper);
        if(articleDetail == null){
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
        QueryWrapper<Article> articleQueryWrapper = new QueryWrapper<>();
        articleQueryWrapper.lambda().eq(Article::getSubmitToken, articleDTO.getSubmitToken());
        Article article = this.getOne(articleQueryWrapper);
        if (article == null) {

            return R.error("文章不存在!");
        }
        article = ArticleConverter.dtoToObj(articleDTO, article);
        article.setUpdateDate(LocalDateTime.now());
        article.setImages(StrUtil.findImageByContent(articleDTO.getContent()));
        article.setIntro(StrUtil.findIntro(articleDTO.getContent()));
        boolean flag = this.updateById(article);
        if (!flag) {

            return R.error("更新文章失败!");
        }

        return R.ok("更新文章成功!", article.getSubmitToken());
    }

    @Override
    @Transactional
    public R save(ArticleForm articleForm) {
        // 新增频道
        R<Long> r = channelService.saveByName(articleForm.getChannel());
        if (r.getCode() < 1) {
            return r;
        }
        Long channelId = r.getData();
        // 新增标签
        R<List<Long>> r1 = labelService.saveByName(channelId, articleForm.getChannel());
        if (r1.getCode() < 1) {
            return r1;
        }
        List<Long> tagIds = r1.getData();
        // 新增文章
        Article article = ArticleConverter.formToObj(articleForm);
        article.setCreateDate(LocalDateTime.now());
        article.setUpdateDate(null);
        article.setSalt(RandomStringUtils.randomAlphanumeric(16));
        article.setSubmitToken(EncryptUtil.getInstance().encodeAes(article.getSalt(), SystemConstants.DESKEY));
        article.setLabels(articleForm.getLabels());
        article.setChannel(articleForm.getChannel());
        article.setImages(StrUtil.findImageByContent(articleForm.getContent()));
        article.setIntro(StrUtil.findIntro(articleForm.getContent()));
        boolean flag = this.save(article);
        if (!flag) {

            return R.error("更新文章失败!");
        }
        r = articleLabelService.saveByTagsAndArticleId(tagIds, article.getId());
        if (r.getCode() < 1) {

            return r;
        }

        return R.ok("发布文章成功!", article.getSubmitToken());
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
        boolean flag = this.save(article);
        if (!flag) {
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
    public Page<ArticleVO> pageFrontArticleVO(Page<ArticleVO> page, ArticleVO articleVO) {
        Map<String,Object> params = new HashMap<>(4);
        params.put("page", (page.getCurrent()-1) * page.getSize());
        params.put("size",page.getSize());
        params.put("articleVO",articleVO);
        List<ArticleVO> list = articleMapper.pageFrontArticleVO(params);
        page.setTotal(articleMapper.count(params));
        return page.setRecords(list);
    }


    @Override
    public ArticleVO findById(Long id) {
        return articleMapper.getOne(id);
    }


}
