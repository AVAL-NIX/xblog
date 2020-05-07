package com.blog.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author zhengxin
 * @since 2020-03-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Visitor extends Model<Visitor> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户名 
     */
    private String username;

    /**
     * 用户昵称 
     */
    private String nickname;

    /**
     * 用户密码 
     */
    private String password;

    /**
     * 访问凭证(可以凭此凭证留言) 
     */
    private String accessToken;

    /**
     * 用户盐 
     */
    private String salt;

    /**
     * 用户盐 
     */
    private String email;

    /**
     * 访客最后一次登录IP 
     */
    private String ip;

    /**
     * 创建时间
     */
    private LocalDateTime createDate;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
