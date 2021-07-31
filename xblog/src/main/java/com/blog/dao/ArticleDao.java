package com.blog.dao;

import com.blog.model.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;


@RepositoryRestResource(path = "article")
public interface ArticleDao extends JpaRepository<Article, Long>, JpaSpecificationExecutor<Article> {

    Article findBySubmitToken(String submitToken);

    @Query(value = " select count(1) from article where channel = 'topic' ", nativeQuery = true)
    int countByTopic();


    @Query(nativeQuery = true, value = " select * from article a where a.labels like ?1  and a.channel = 'topic'  and   a.id not in (select b.article_id from article_admin b  where b.admin_id = ?2) ")
    List<Article> getTopicList(String label, Long adminId);

    @Query(nativeQuery = true, value = " select * from article a where a.channel = 'topic'  and   a.id not in (select b.article_id from article_admin b  where b.admin_id = ?1)  ")
    List<Article> getTopicList(Long adminId);
}
