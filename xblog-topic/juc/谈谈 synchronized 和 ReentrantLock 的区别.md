---
submitToken: A92096566B4DF2B1D75E65591B9735875A66D1C85DA715F21E75591494534499
title: 谈谈 synchronized 和 ReentrantLock 的区别
channel: topic
labels: juc
---

特性:

- 两者都是可重入锁

- synchronized 依赖于 JVM 而 ReentrantLock 依赖于 API

- ReentrantLock 比 synchronized 增加了一些高级功能

  - 等待可中断 : ReentrantLock提供了一种能够中断等待锁的线程的机制，通过 lock.lockInterruptibly() 来实现这个机制。也就是说正在等待的线程可以选择放弃等待，改为处理其他事情。
  - 可实现公平锁 : ReentrantLock可以指定是公平锁还是非公平锁。而synchronized只能是非公平锁。所谓的公平锁就是先等待的线程先获得锁。ReentrantLock默认情况是非公平的，可以通过 ReentrantLock类的ReentrantLock(boolean fair)构造方法来制定是否是公平的。
  - 可实现选择性通知（锁可以绑定多个条件）: synchronized关键字与wait()和notify()/notifyAll()方法相结合可以实现等待/通知机制。ReentrantLock类当然也可以实现，但是需要借助于Condition接口与newCondition()方法。
