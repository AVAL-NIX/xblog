package com.blog.service;

import com.blog.model.bean.ResultData;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author zhengxin
 * @since 2019-03-29
 */
public interface ArticleLabelService {

    ResultData deleteByArticleId(Long id);

    ResultData saveByTagsAndArticleId(List<Long> tagIds, long articleId);
}
