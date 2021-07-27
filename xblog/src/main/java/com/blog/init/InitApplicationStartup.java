package com.blog.init;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * spring boot 启动时执行的初始化类
 *
 * @author zx
 * @date 2019/1/28
 */
@Component
@Slf4j
public class InitApplicationStartup implements ApplicationListener<ContextRefreshedEvent> {


    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        log.info("------------------执行初始化方法-----------------");


    }
}