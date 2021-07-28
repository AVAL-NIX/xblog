package com.blog.service;

import com.blog.model.bean.ResultData;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author zhengxin
 * @since 2019-04-28
 */
public interface ChannelService {


    /**
     * 保存频道
     *
     * @param name
     * @return
     */
    public ResultData saveByName(String name);

    ResultData list();
}
