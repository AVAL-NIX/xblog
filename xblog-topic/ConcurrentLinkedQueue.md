---
submitToken: F67889201301729FB40405F37AF66477AE2BEA0FB0840E6A9F17B8BF856A1E25
title: ConcurrentLinkedQueue数据结构
channel: topic
labels: data
---

底层结构：Node链表

 高效的并发队列，使用链表实现。可以看做一个线程安全的 LinkedList，这是一个非阻塞队列。

 > Java 提供的线程安全的 Queue 可以分为阻塞队列和非阻塞队列，其中阻塞队列的典型例子是 BlockingQueue，非阻塞队列的典型例子是 ConcurrentLinkedQueue，在实际应用中要根据实际需要选用阻塞队列或者非阻塞队列。 阻塞队列可以通过加锁来实现，非阻塞队列可以通过 CAS 操作实现。
