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


    @Query(nativeQuery = true, value = " select * from article a where a.lable like ?2 and  a.id not in (select b.article_id from article_admin b  where b.admin_id = ?3)  limit ?1 ")
    List<Article> getTopicList(int count, String label, Long adminId);

}
