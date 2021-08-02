package com.blog.service.impl;

import com.blog.dao.ArticleAdminDao;
import com.blog.model.bean.ResultData;
import com.blog.model.converter.ArticleAdminConverter;
import com.blog.model.entity.ArticleAdmin;
import com.blog.model.entity.ArticleAdminDTO;
import com.blog.service.ArticleAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

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


    @Override
    public ResultData save(ArticleAdminDTO articleAdminDTO) {
        ArticleAdmin temp = articleAdminDao.findByAdminIdAndArticleId(articleAdminDTO.getAdminId(), articleAdminDTO.getArticleId());
        if (temp != null) {
            return ResultData.ok();
        }
        ArticleAdmin articleAdmin = ArticleAdminConverter.dtoToObj(articleAdminDTO);
        articleAdmin.setCreateDate(LocalDateTime.now());
        return ResultData.data(articleAdminDao.save(articleAdmin));
    }


    @Override
    public ResultData deleteByAdminId(Long adminId) {
        return ResultData.data(articleAdminDao.deleteByAdminId(adminId));
    }
}
