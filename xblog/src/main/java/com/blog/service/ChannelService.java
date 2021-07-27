package com.blog.service;

import com.blog.model.bean.R;

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
    public R saveByName(String name);

    R list();
}
