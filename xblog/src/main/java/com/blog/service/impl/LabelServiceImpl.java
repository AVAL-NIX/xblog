package com.blog.service.impl;

import com.blog.common.constants.CacheKey;
import com.blog.common.utils.CacheUtils;
import com.blog.dao.ChannelDao;
import com.blog.dao.LabelDao;
import com.blog.model.bean.ResultData;
import com.blog.model.entity.Channel;
import com.blog.model.entity.Label;
import com.blog.service.ArticleLabelService;
import com.blog.service.LabelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author zhengxin
 * @since 2019-03-29
 */
@Service
public class LabelServiceImpl implements LabelService {

    @Autowired
    protected ArticleLabelService articleLabelService;

    @Autowired
    protected LabelDao labelDao;

    @Autowired
    protected ChannelDao channelDao;

    @Autowired
    protected CacheUtils cacheUtils;

    @Override
    public ResultData<List<Long>> saveByName(Long channelId, String names) {
        boolean flag = false;
        String[] tags = names.split(",");
        List<Long> tagIds = new ArrayList<>();
        for (String tag : tags) {
            Label label = labelDao.findByName(tag);
            if (label == null) {
                label = new Label();
                label.setName(tag);
                label.setChannelId(channelId);
                label = labelDao.save(label);
                if (label == null) {
                    return ResultData.error();
                }
                tagIds.add(label.getId());
            } else {
                label.setChannelId(channelId);
                label.setUpdateDate(LocalDateTime.now());
                label = labelDao.save(label);
                if (label == null) {
                    return ResultData.error();
                }
                tagIds.add(label.getId());
            }
        }
        return ResultData.data(tagIds);
    }

    @Override
    public ResultData list() {
        return ResultData.data(labelDao.findAll());
    }


    @Override
    public ResultData listTopicType() {
        Channel channel = channelDao.findByName("topic");
        if (channel == null) {
            return ResultData.ok();
        }
        Object result = cacheUtils.get(CacheUtils.TWENTYFOUR_HOURS, CacheKey.CACHE_TOPIC_TYPE_COUNT_KEY + channel.getId());
        if (result != null) {
            return ResultData.data(result);
        } else {
            List<Label> list = labelDao.listTopicType(channel.getId());
            cacheUtils.set(CacheUtils.TWENTYFOUR_HOURS, CacheKey.CACHE_TOPIC_TYPE_COUNT_KEY + channel.getId(), list);
            return ResultData.data(list);
        }
    }
}
