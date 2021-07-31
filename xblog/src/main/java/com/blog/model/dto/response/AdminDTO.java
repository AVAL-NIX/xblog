package com.blog.model.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * Admin 实体类
 *
 * @author zx
 * @date 2018/12/18
 */
@Data
public class AdminDTO {

    /**
     * 用户ID
     **/
    private Long id;

    /**
     * 用户名
     **/
    private String username;

    /**
     * 昵称
     **/
    private String nickname;

    /**
     * 用户名头像
     **/
    private String headImg;

    /**
     * 密码
     **/
    private String password;

    /**
     * 状态
     **/
    private Integer status;

    /**
     * 创建时间
     **/
    private LocalDateTime createDate;

    /**
     * 更新时间
     **/
    private LocalDateTime updateDate;

    /**
     * 登录凭证
     */
    private String token;

}
