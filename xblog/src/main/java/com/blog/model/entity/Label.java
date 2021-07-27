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
 * @since 2019-03-29
 */
@Entity
@Data
public class Label {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 类别名称 
     */
    private String name;

    /**
     * 父类别ID , 1级类别是0
     */
    private Long channelId;

    /**
     * 创建时间
     */
    private LocalDateTime createDate;

    /**
     * 更新时间
     */
    private LocalDateTime updateDate;

    /**
     * 是否可用, 0可用,-1不可用
     */
    private Integer available;

}
