---
submitToken: 4E2DCA30D79D80EBA7951AC7F2A2B8600A22091056A73B607E89C62F459F07A4
title: shutdown与shutdownNow 区别
channel: topic
labels: juc
---

- shutdown（） :关闭线程池，线程池的状态变为 SHUTDOWN。线程池不再接受新任务了，但是队列里的任务得执行完毕。

- shutdownNow（） :关闭线程池，线程的状态变为 STOP。线程池会终止当前正在运行的任务，并停止处理排队的任务并返回正在等待执行的 List。