package com.blog.init;

import com.blog.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @description: // TODO
 * <br>
 * @date: 2019/11/1 18:04
 * @author: zhengxin
 * @version: mall V1.0
 * @since: JDK 1.8
 */
@Component
@Slf4j
public class TestTaskAAA {


    @Autowired
    RoleService roleService;

    @Scheduled(cron = "* */30 * * * ?")
    public void run(){
        log.info(" task test");
        roleService.list();
    }
}
