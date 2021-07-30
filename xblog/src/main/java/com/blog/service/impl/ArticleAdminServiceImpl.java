package com.blog.service.impl;

import com.blog.dao.ArticleAdminDao;
import com.blog.model.bean.ResultData;
import com.blog.service.ArticleAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author zhengxin
 * @since 2019-08-02
 */
@Service
public class ArticleAdminServiceImpl implements ArticleAdminService {

    @Autowired
    ArticleAdminDao articleAdminDao;

    @Override
    public ResultData getCountByAdminId(Long adminId) {
        return ResultData.data(articleAdminDao.getCountByAdminId(adminId));
    }
}
