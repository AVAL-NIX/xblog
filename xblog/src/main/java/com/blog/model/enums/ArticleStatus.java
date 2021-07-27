package com.blog.model.enums;

import lombok.Getter;

/**
 * 文章枚举类 (-1 删除 ,0. 待发布 ,1.正常)
 * 
 * @{link com.back.model.entity.ArticleStatus}
 *
 * @author Avalon
 * @date 2019/3/30
 */
@Getter
public enum ArticleStatus {

    /** -1 删除 **/
    DELETE(-1, "删除"),

    /** 0. 待发布 **/
    WATING(0, "待发布"),

    /** 1.正常 **/
    PUBLISH(1, "正常"),

    ;

    public int code;

    public String value;

    ArticleStatus(int code, String value) {
        this.code = code;
        this.value = value;
    }

    public ArticleStatus getEnum(int code) {
        ArticleStatus[] values = ArticleStatus.values();
        for (ArticleStatus value : values) {
            if (value.code == code) {
                return value;
            }

        }
        return null;
    }
}
