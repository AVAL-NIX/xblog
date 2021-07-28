package com.blog.service.impl;

import com.blog.dao.ChannelDao;
import com.blog.model.bean.ResultData;
import com.blog.model.entity.Channel;
import com.blog.service.ChannelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author zhengxin
 * @since 2019-04-28
 */
@Service
public class ChannelServiceImpl implements ChannelService {
    @Autowired
    ChannelDao channelDao;

    @Override
    public ResultData saveByName(String name) {
        Channel channel = channelDao.findByName(name);
        if (channel == null) {
            channel = new Channel();
            channel.setName(name);
            channel = channelDao.save(channel);
            if (channel == null) {
                return ResultData.error();
            }
        }
        return ResultData.data(channel.getId());
    }


    @Override
    public ResultData list() {
        return ResultData.data(channelDao.findAll());
    }
}
