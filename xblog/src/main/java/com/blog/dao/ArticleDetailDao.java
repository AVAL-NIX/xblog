package com.blog.dao;

import com.blog.model.entity.ArticleDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;


@RepositoryRestResource(path = "article-detail")
public interface ArticleDetailDao extends JpaRepository<ArticleDetail, Long> {


    // TODO
    //    @Query({"<script>", "  update article_detail set view_count = view_count + 1 where article_id in  ",
//            " <foreach collection='ids' item='item' index='index' open='(' separator=',' close=')' > ", "#{item}",
//            "</foreach> ", "</script>"})

    @Query(value = " update from article_detail  where 1=1 ", nativeQuery = true)
    int updateArticleViewCount(List<Long> ids);


    @Query(value = " update from article_detail  where 1=1 ", nativeQuery = true)
    int updateArticleUpCount(List<Long> ids);

    @Query(value = " update from article_detail  where 1=1 ", nativeQuery = true)
    int updateArticleDownCount(List<Long> ids);

    ArticleDetail findByArticleId(Long articleId);
}
