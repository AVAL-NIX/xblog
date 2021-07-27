package com.blog.model.converter;

import com.blog.model.dto.request.ArticleDTO;
import com.blog.model.entity.Article;
import org.springframework.beans.BeanUtils;

/**
 * 文章类转换
 *
 * @author Avalon
 * @date 2019/1/30
 */
public class ArticleConverter {


    public static Article dtoToObj(ArticleDTO articleDTO) {
        Article article = new Article();
        BeanUtils.copyProperties(articleDTO, article);
        return article;
    }

    public static Article dtoToObj(ArticleDTO articleDTO, Article article) {
        BeanUtils.copyProperties(articleDTO, article);
        return article;
    }

    public static ArticleDTO objTodto(Article article) {
        ArticleDTO articleDTO = new ArticleDTO();
        BeanUtils.copyProperties(article, articleDTO);
        return articleDTO;
    }

}
