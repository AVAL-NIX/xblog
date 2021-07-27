package com.blog.service;

import com.blog.model.bean.R;
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

    R updateById(ArticleDTO articleDTO);

    R updateBySign(ArticleDTO articleDTO);

    R save(ArticleDTO articleDTO);

    R page(PageRequest page, ArticleDTO articleDTO);

    R findById(Long id);

    R list();

    R findBySubmitToken(String submitToken);
}