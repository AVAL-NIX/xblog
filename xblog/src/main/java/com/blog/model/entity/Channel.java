package com.blog.model.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author zhengxin
 * @since 2019-04-28
 */
@Entity
@Data
public class Channel  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 频道名称 
     */
    private String name;

    /**
     * 创建时间
     */
    private LocalDateTime createDate;

    /**
     * 是否可用, 0可用,-1不可用
     */
    private Boolean available;

    /**
     * 更新时间
     */
    private LocalDateTime updateDate;

}
