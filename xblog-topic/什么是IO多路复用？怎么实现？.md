---
submitToken: FA8564C8B163D42C1140782AF2194D9AFC51D71ED53AA66AFDB0D795156359D8
title: 什么是IO多路复用？怎么实现？
channel: topic
labels: 操作系统
---

> IO多路复用（IO Multiplexing）是指单个进程/线程就可以同时处理多个IO请求。

- **实现原理**：用户将想要监视的文件描述符（File Descriptor）添加到select/poll/epoll函数中，由内核监视，函数阻塞。一旦有文件描述符就绪（读就绪或写就绪），或者超时（设置timeout），函数就会返回，然后该进程可以进行相应的读/写操作。
