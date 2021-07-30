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

    ResultData saveByName(Long channelId, String tags);

    ResultData list();

    /**
     * 获取所有的 topic 类型
     *
     * @return
     */
    ResultData listTopicType();
}
