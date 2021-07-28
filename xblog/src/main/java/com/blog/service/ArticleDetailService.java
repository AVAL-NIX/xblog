package com.blog.service;

import com.blog.model.bean.ResultData;

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


    ResultData findByArticleId(Long id);

    ResultData save(Long id);

    ResultData updateViewCount(List<Long> ids);

    ResultData updateUpCount(List<Long> ids);

    ResultData updateDownCount(List<Long> ids);
}
