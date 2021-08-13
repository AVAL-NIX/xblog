package com.blog.task;


import com.blog.dao.ArticleDao;
import com.blog.model.entity.Article;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 更新周线行情
 *
 * @author Administrator
 * @date 2020/9/14
 */
@Component
@Slf4j
public class DelNullArticle {


    @Autowired
    protected ArticleDao articleDao;

    @Scheduled(cron = "0 15 10 ? * * ")
    public void run() {
        log.info("开始执行 DelNullArticle ");

        List<Article> list = articleDao.findAll();
        for (Article article : list) {
            if (StringUtils.isBlank(article.getTitle())) {
                articleDao.delete(article);
            }
        }

        log.info("执行完毕 DelNullArticle ");
    }
    

}
