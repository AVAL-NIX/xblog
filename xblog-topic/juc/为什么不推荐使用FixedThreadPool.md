---
submitToken: C0D3A8FFDF967D1348026C7D45665B3064D1A6BC7C5DB541F90B50FBBD44B855
title: 为什么不推荐使用FixedThreadPool？
channel: topic
labels: juc
---

- newFixedThreadPool的阻塞队列大小是没有大小限制的，如果队列堆积数据太多会造成资源消耗。

- newCachedThreadPool是线程数量是没有大小限制的，当新的线程来了直接创建，同样会造成资源消耗殆尽。