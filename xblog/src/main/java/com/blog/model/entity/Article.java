package com.blog.model.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;


@Entity
@Data
public class Article  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 文章标题 
     */
    private String title;

    /**
     * 文章简介
     */
    private String intro;

    /**
     * 文章提交凭证
     */
    private String submitToken;

    /**
     * 盐
     */
    private String salt;

    /**
     * 用户ID
     */
    private Long adminId;

    /**
     * 频道名称[冗余]
     */
    private String channel;

    /**
     * 标签[冗余]
     */
    private String labels;
    /**
     * 文章内容
     */
    private String content;

    /**
     * 状态(-1 删除 ,0. 待发布 ,1.正常)
     */
    private Integer status;


    /**
     * 创建时间
     */
    private LocalDateTime createDate;

    /**
     * 更新时间
     */
    private LocalDateTime updateDate;

    /**
     * 图片集
     */
    private String images;

}
