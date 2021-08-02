package com.blog.service;

import com.blog.model.bean.ResultData;
import com.blog.model.entity.ArticleAdminDTO;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author zhengxin
 * @since 2019-08-02
 */
public interface ArticleAdminService {


    ResultData getCountByAdminId(Long adminId);

    ResultData save(ArticleAdminDTO articleAdminDTO);

    ResultData deleteByAdminId(Long adminId);
}
