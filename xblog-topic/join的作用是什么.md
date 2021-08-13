---
submitToken: AB04438C4317CEC3A8AC44DA4977AB9C6CE15C7B3DA9E3918768F52D1640C809
title: thread中的join的作用是什么?
channel: topic
labels: juc
---

> Thread类中的join方法的主要作用就是同步，它可以使得线程之间的并行执行变为串行执行。

```
  ThreadJoinTest t1 = new ThreadJoinTest("小明");
        ThreadJoinTest t2 = new ThreadJoinTest("小东");
        t1.start();
        t1.join();
        t2.start();
```

上面的例子中， t1执行完了 t2才会执行


### 原理解析

```
public final synchronized void join(long millis)
    throws InterruptedException {
        long base = System.currentTimeMillis();
        long now = 0;

        if (millis < 0) {
            throw new IllegalArgumentException("timeout value is negative");
        }

        if (millis == 0) {
            while (isAlive()) {
                wait(0);
            }
        } else {
            while (isAlive()) {
                long delay = millis - now;
                if (delay <= 0) {
                    break;
                }
                wait(delay);
                now = System.currentTimeMillis() - base;
            }
        }
    }

```
从源码中可以看到：join方法的原理就是调用相应线程的wait方法进行等待操作的，例如A线程中调用了B线程的join方法，则相当于在A线程中调用了B线程的wait方法，当B线程执行完（或者到达等待时间），B线程会自动调用自身的notifyAll方法唤醒A线程，从而达到同步的目的。