package com.blog.model.dto.response;

import lombok.Data;

/**
 * @author zhengxin
 * @date 2021/7/29
 */
@Data
public class AdminResponseDTO {

    /**
     * 总的题目数量
     */
    public Integer allCount;

    /**
     * 已经处理的题目数量
     */
    public Integer processCount;

    public AdminResponseDTO(Integer allCount, Integer processCount) {
        this.allCount = allCount;
        this.processCount = processCount;
    }
}
