package com.blog.dao;

import com.blog.model.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;


@RepositoryRestResource(path="article")
public interface ArticleDao extends JpaRepository<Article, Long> {

    Article findBySubmitToken(String submitToken);
}
