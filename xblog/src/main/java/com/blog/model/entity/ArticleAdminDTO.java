package com.blog.model.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 *
 * @author zhengxin
 * @since 2019-08-02
 */
@Data
public class ArticleAdminDTO {

    /**
     * 用户ID
     */
    private Long adminId;

    /**
     * 所属文章ID
     */
    private Long articleId;

    /**
     * 创建时间
     */
    private LocalDateTime createDate;

    /**
     * 更新时间
     */
    private LocalDateTime updateDate;

}
