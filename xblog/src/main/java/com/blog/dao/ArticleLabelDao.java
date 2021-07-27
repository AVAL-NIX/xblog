package com.blog.dao;

import com.blog.model.entity.ArticleLabel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;


@RepositoryRestResource(path = "article-label")
public interface ArticleLabelDao extends JpaRepository<ArticleLabel, Long> {

    int deleteByArticleId(Long articleId);

    ArticleLabel findByArticleIdAndLabelId(long articleId, Long labelId);
}
