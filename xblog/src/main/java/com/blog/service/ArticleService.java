package com.blog.service;

import com.blog.model.bean.ResultData;
import com.blog.model.dto.request.ArticleDTO;
import org.springframework.data.domain.PageRequest;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author zhengxin
 * @since 2019-03-29
 */
public interface ArticleService {

    ResultData updateById(ArticleDTO articleDTO);

    ResultData updateBySign(ArticleDTO articleDTO);

    ResultData save(ArticleDTO articleDTO);

    ResultData page(PageRequest page, ArticleDTO articleDTO);

    ResultData findById(Long id);

    ResultData list();

    ResultData findBySubmitToken(String submitToken);
}