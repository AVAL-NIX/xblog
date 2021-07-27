package com.blog.model.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

/**
 * Admin 实体类
 *
 * @author zx
 * @date 2018/12/18
 */
@Entity
@Data
public class Admin {

    /** 用户ID **/
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 用户名 **/
    private String username;

    /** 昵称 **/
    private String nickname;

    /** 用户名头像 **/
    private String headImg;

    /** 密码 **/
    private String password;

    /** 状态 **/
    private Integer status;

    /** 创建时间 **/
    private LocalDateTime createDate;

    /** 更新时间 **/
    private LocalDateTime updateDate;

    /** 提交凭证**/
    private String submitToken;

    /** 盐 **/
    private String salt;


}
