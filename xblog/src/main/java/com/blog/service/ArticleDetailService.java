package com.blog.service;

import com.blog.model.bean.R;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author zhengxin
 * @since 2019-08-02
 */
public interface ArticleDetailService {


    R findByArticleId(Long id);

    R save(Long id);

    R updateViewCount(List<Long> ids);

    R updateUpCount(List<Long> ids);

    R updateDownCount(List<Long> ids);
}
