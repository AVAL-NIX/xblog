---
submitToken: C4DEEEC97A31E88CF32316F975C469C41FBAD0E17496FBBD5709A6D2E1C84BF1
title: ThreadPoolExecutor 类的重要参数了解吗？
channel: topic
labels: juc
---

7 个参数
<!-- title -->

ThreadPoolExecutor 3 个最重要的参数：

- corePoolSize : 核心线程数线程数定义了最小可以同时运行的线程数量。

- maximumPoolSize : 当队列中存放的任务达到队列容量的时候，当前可以同时运行的线程数量变为最大线程数。

- workQueue: 当新任务来的时候会先判断当前运行的线程数量是否达到核心线程数，如果达到的话，新任务就会被存放在队列中。

ThreadPoolExecutor其他常见参数:

- keepAliveTime:当线程池中的线程数量大于 corePoolSize 的时候，如果这时没有新的任务提交，核心线程外的线程不会立即销毁，而是会等待，直到等待的时间超过了 keepAliveTime才会被回收销毁；
- unit : keepAliveTime 参数的时间单位。
- threadFactory :executor 创建新线程的时候会用到。
- handler :饱和策略。关于饱和策略下面单独介绍一下。

