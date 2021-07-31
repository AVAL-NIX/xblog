package com.blog.dao;

import com.blog.model.entity.ArticleAdmin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;


@RepositoryRestResource(path = "article-user")
public interface ArticleAdminDao extends JpaRepository<ArticleAdmin, Long>, JpaSpecificationExecutor<ArticleAdmin> {

    List<ArticleAdmin> findByAdminId(Long userId);

    /**
     * 获取用户完成的 试题数量
     *
     * @param adminId
     * @return
     */
    @Query(nativeQuery = true, value = " select count(1) from article_admin a " +
            " where a.admin_id = ?1   ")
    int getCountByAdminId(Long adminId);

    ArticleAdmin findByAdminIdAndArticleId(Long adminId, Long articleId);
}
