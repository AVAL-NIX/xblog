package com.blog.service;

import com.blog.model.bean.ResultData;
import com.blog.model.dto.request.ArticleDTO;
import org.springframework.data.domain.PageRequest;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author zhengxin
 * @since 2019-03-29
 */
public interface ArticleService {

    ResultData save(ArticleDTO articleDTO);

    ResultData page(PageRequest page, ArticleDTO articleDTO);

    ResultData findById(Long id);

    ResultData list();

    ResultData findBySubmitToken(String submitToken);

    /**
     * 获取目前有的 topic 数量
     *
     * @param userId
     * @return
     */
    ResultData listTopicCount();

    /**
     * 某个用户获取随机多少道题
     *
     * @param count
     * @return
     */
    ResultData getTopicList(int count, String label, Long adminId);


}