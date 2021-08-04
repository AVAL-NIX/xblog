---
submitToken: C7661030C85A85F842A2098F0E75E486C0BB307164279626D82687DC75ED5564
title: 讲一下 synchronized 关键字的底层原理
channel: topic
labels: juc
---

- 第一步

先是偏向锁， 标记对象头，记为标记头 修改markword标记为 记为1

- 第二步

其他线程拿不到偏向锁 ，然后升级为轻量级锁，进行CAS自旋

- 第三步

进CAS一定次数 进行锁升级， 重量级锁， 底层是操作系统级别的锁了。 用了monitor对象


monitor对象 的解释：


> 在 Java 虚拟机(HotSpot)中，Monitor 是基于 C++实现的，由ObjectMonitor实现的。每个对象中都内置了一个 ObjectMonitor对象。
在执行monitorenter时，会尝试获取对象的锁，如果锁的计数器为 0 则表示锁可以被获取，获取后将锁计数器设为 1 也就是加 1。
在执行 monitorexit 指令后，将锁计数器设为 0，表明锁被释放。如果获取对象锁失败，那当前线程就要阻塞等待，直到锁被另外一个线程释放为止。
类似CAS操作