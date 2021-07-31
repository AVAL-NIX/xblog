package com.blog.model.converter;

import com.blog.model.entity.ArticleAdmin;
import com.blog.model.entity.ArticleAdminDTO;
import org.springframework.beans.BeanUtils;

public class ArticleAdminConverter {


    public static ArticleAdmin dtoToObj(ArticleAdminDTO articleAdminDTO) {
        ArticleAdmin articleAdmin = new ArticleAdmin();
        BeanUtils.copyProperties(articleAdminDTO, articleAdmin);
        return articleAdmin;
    }


}
