---
submitToken: B93690830EA70CED6C46ECDCCA5E134D5D635523FA79D2F1EC96F5E25E612535
title: 谈谈AQS原理？
channel: topic
labels: juc
---


- 直接尝试获取锁 ， 获取失败进入队列addwater。
- AQS 使用一个 int status 成员变量来表示同步状态
- 通过内置的 FIFO 队列来完成获取资源线程的排队工作
- 每个线程被包装成node对象 ， 有4种状态 -3 到1
sync对象获取锁
AQS 使用 CAS 对该同步状态进行原子操作实现对其值的修改。
排队机制 AQS 是用 （虚拟的双向队列）实现的 , 头节点是没有锁的。公平会判断队列头是否有线程，有就直接排队，没有就cas尝试一下
CAS操作类似atomic的操作。 底层都是一个unsafe对象。
底层使用了模板方法

原理细节直接看这篇
https://javadoop.com/post/java-concurrent-queue

Condition
一个lock可以拥有多个condition对象
condition对象也有一个队列链表
可以通过singleall唤醒所有的
signle唤醒队头
locksupport.lock(this)不释放锁
Object.await() 是释放锁的
Thread.interrupt()是会报错的。 sleep 下。 await下也会抛出中断异常  locksupport.lock不会抛异常,中断状态还是true


AQS 对资源的共享方式

Exclusive（独占）：只有一个线程能执行，如ReentrantLock。又可分为公平锁和非公平锁：
公平锁：按照线程在队列中的排队顺序，先到者先拿到锁
非公平锁：当线程要获取锁时，无视队列顺序直接去抢锁，谁抢到就是谁的（默认）
Share（共享）：多个线程可同时执行，如 CountDownLatch、Semaphore、 CyclicBarrier、ReadWriteLock 我们都会在后面讲到。

非公平锁会直接 CAS 抢锁，但是公平锁会判断等待队列是否有线程处于等待状态，如果有则不去抢锁，乖乖排到后面。

 Condition 的使用场景
Condition 经常可以用在生产者-消费者的场景中


1、我们可以看到，在使用 condition 时，必须先持有相应的锁。这个和 Object 类中的方法有相似的语义，需要先持有某个对象的监视器锁才可以执行 wait(), notify() 或 notifyAll() 方法。
2、ArrayBlockingQueue 采用这种方式实现了生产者-消费者，所以请只把这个例子当做学习例子，实际生产中可以直接使用 ArrayBlockingQueue


AQS 组件
Semaphore(信号量)-允许多个线程同时访问： synchronized 和 ReentrantLock 都是一次只允许一个线程访问某个资源，Semaphore(信号量)可以指定多个线程同时访问某个资源。
CountDownLatch （倒计时器）： CountDownLatch 是一个同步工具类，用来协调多个线程之间的同步。这个工具通常用来控制线程等待，它可以让某一个线程等待直到倒计时结束，再开始执行。
CyclicBarrier(循环栅栏)： CyclicBarrier 和 CountDownLatch 非常类似，它也可以实现线程间的技术等待，但是它的功能比 CountDownLatch 更加复杂和强大。主要应用场景和 CountDownLatch 类似。CyclicBarrier 的字面意思是可循环使用（Cyclic）的屏障（Barrier）。它要做的事情是，让一组线程到达一个屏障（也可以叫同步点）时被阻塞，直到最后一个线程到达屏障时，屏障才会开门，所有被屏障拦截的线程才会继续干活。CyclicBarrier 默认的构造方法是 CyclicBarrier(int parties)，其参数表示屏障拦截的线程数量，每个线程调用 await() 方法告诉 CyclicBarrier 我已经到达了屏障，然后当前线程被阻塞。