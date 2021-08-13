package com.blog.task;


import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 更新周线行情
 *
 * @author Administrator
 * @date 2020/9/14
 */
@Component
@Slf4j
public class WeekDataTask {

    @Scheduled(cron = "1 0/5 * * * ?")
    public void run() {
        log.info("开始执行 WeekDataTask ");

    }

}
