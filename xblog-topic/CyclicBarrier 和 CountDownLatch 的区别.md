---
submitToken: 39A312FEE7228F3AECD7A6DB841A431C8BAFBF8F297BED0AAAE3EAEF972CBF28
title: CyclicBarrier 和 CountDownLatch 的区别
channel: topic
labels: juc
---

两个看上去有点像的类，都在 java.util.concurrent 下，都可以用来表示代码运行到 某个点上，二者的区别在于：

-  1.CyclicBarrier (阻塞，全部阻塞完毕，主线程开始运行)的某个线程运行到某个点上之后，该线程即停止运行，直到所有的 线程都到达了这个点，所有线程才重新运行 ；
CountDownLatch （运行完-1，减到0主线程开始运行） 则不是，某线程运 行到某个点上之后，只是给某个数值-1 而已，该线程继续运行。
- 2.CyclicBarrier 只能唤起一个任务，CountDownLatch 可以唤起多个任务。
- 3.CyclicBarrier 可 重 用 ， CountDownLatch 不可重用 ， 计数值为 0 该CountDownLatch就不可再用了


> CountDownLatch 基于 AQS 的共享模式的使用，而 CyclicBarrier 基于 Condition 来实现。