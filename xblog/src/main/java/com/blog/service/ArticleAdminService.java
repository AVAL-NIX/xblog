package com.blog.service;

import com.blog.model.bean.ResultData;

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
}
