---
submitToken: AD13E1413DC6DA75D9CB5E7E1A635DF6C070DB220451AA653821252E290C6FD5
title: 什么是AQS？
channel: topic
labels: juc
---


全称是AbstractQueuedSynchronizer

> AQS 是一个用来构建锁和同步器的框架，使用 AQS 能简单且高效地构造出应用广泛的大量的同步器，比如我们提到的 ReentrantLock，Semaphore，其他的诸如 ReentrantReadWriteLock，SynchronousQueue，FutureTask 等等皆是基于 AQS 的。当然，我们自己也能利用 AQS 非常轻松容易地构造出符合我们自己需求的同步器。

## 简单步骤

- 直接尝试获取锁 ， 获取失败进入队列addwater。
- AQS 使用一个 int status 成员变量来表示同步状态
- 通过内置的 FIFO 队列来完成获取资源线程的排队工作
  - 每个线程被包装成node对象 ， 有4种状态 -3 到1
  - sync对象获取锁
- AQS 使用 CAS 对该同步状态进行原子操作实现对其值的修改。
- 排队机制 AQS 是用 （虚拟的双向队列）实现的 , 头节点是没有锁的。公平会判断队列头是否有线程，有就直接排队，没有就cas尝试一下
- CAS操作类似atomic的操作。 底层都是一个unsafe对象。
- 底层使用了模板方法

具体方法

```
isHeldExclusively()//该线程是否正在独占资源。只有用到condition才需要去实现它。
tryAcquire(int)//独占方式。尝试获取资源，成功则返回true，失败则返回false。
tryRelease(int)//独占方式。尝试释放资源，成功则返回true，失败则返回false。
tryAcquireShared(int)//共享方式。尝试获取资源。负数表示失败；0表示成功，但没有剩余可用资源；正数表示成功，且有剩余资源。
tryReleaseShared(int)//共享方式。尝试释放资源，成功则返回true，失败则返回false。
```

## Condition(类似Thread.wait(), notify之类的方法)

- 一个lock可以拥有多个condition对象
- condition对象也有一个队列链表
- 可以通过singleall唤醒所有的
- signle唤醒队头
- locksupport.lock(this)不释放锁
- Object.await() 是释放锁的
- Thread.interrupt()是会报错的。 sleep 下。 await下也会抛出中断异常  - - - locksupport.lock不会抛异常,中断状态还是true


## AQS 组件
- Semaphore(信号量)-允许多个线程同时访问： synchronized 和 ReentrantLock 都是一次只允许一个线程访问某个资源，Semaphore(信号量)可以指定多个线程同时访问某个资源。
- CountDownLatch （倒计时器）： CountDownLatch 是一个同步工具类，用来协调多个线程之间的同步。这个工具通常用来控制线程等待，它可以让某一个线程等待直到倒计时结束，再开始执行。
- CyclicBarrier(循环栅栏)： CyclicBarrier 和 CountDownLatch 非常类似，它也可以实现线程间的技术等待，但是它的功能比 CountDownLatch 更加复杂和强大。主要应用场景和 CountDownLatch 类似。CyclicBarrier 的字面意思是可循环使用（Cyclic）的屏障（Barrier）。它要做的事情是，让一组线程到达一个屏障（也可以叫同步点）时被阻塞，直到最后一个线程到达屏障时，屏障才会开门，所有被屏障拦截的线程才会继续干活。CyclicBarrier 默认的构造方法是 CyclicBarrier(int parties)，其参数表示屏障拦截的线程数量，每个线程调用 await() 方法告诉 CyclicBarrier 我已经到达了屏障，然后当前线程被阻塞。