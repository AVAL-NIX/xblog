package com.blog.service;

import com.blog.model.bean.R;

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

    R deleteByArticleId(Long id);

    R saveByTagsAndArticleId(List<Long> tagIds, long articleId);
}
