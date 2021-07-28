package com.blog.service;

import com.blog.model.bean.ResultData;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author zhengxin
 * @since 2019-03-29
 */
public interface LabelService {


    /**
     * 添加标签
     *
     * @param channelId
     * @param tags
     * @return List<Long>
     */
    ResultData saveByName(Long channelId, String tags);

    ResultData list();
}
