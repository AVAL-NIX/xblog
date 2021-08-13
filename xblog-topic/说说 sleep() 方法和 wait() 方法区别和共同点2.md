---
submitToken: 9AD963A7D3B41BE3D73A09B06CDB6DCA3ADC7F2EB901CAA3A58663E171997617
title: 说说 sleep() 方法和 wait() 方法区别和共同点?
channel: topic
labels: javaGuide
---

- 两者最主要的区别在于：**sleep() 方法没有释放锁，而 wait() 方法释放了锁** 。

- 两者都可以暂停线程的执行。

- wait() 通常被用于线程间交互/通信，sleep() 通常被用于暂停执行。

- wait() 方法被调用后，线程不会自动苏醒，需要别的线程调用同一个对象上的 notify() 或者notifyAll() 方法。sleep() 方法执行完成后，线程会自动苏醒。或者可以使用 wait(long timeout) 超时后线程会自动苏醒。