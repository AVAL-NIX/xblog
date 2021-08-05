---
submitToken: 961F854B5D653420F047C45D3E8BCF80466702ED339DDA913EE3EB64BF65602A
title: 重排序与 happens-before 原则了解吗?
channel: topic
labels: juc
---

JMM可以通过happens-before关系向程序员提供跨线程的内存可见性保证（如果A线程的写操作a与B线程的读操作b之间存在happens-before关系，尽管a操作和b操作在不同的线程中执行，但JMM向程序员保证a操作将对b操作可见）。

遵循下面6个规则，就能保证内存可见。

- (1)程序顺序规则：一个线程中的每个操作，happens-before于该线程中的任意后续操作。

```
double pi = 3.14; // A
double r = 1.0; // B
double area = pi * r * r; // C
```

- (2)监视器锁规则：对一个锁的解锁，happens-before于随后对这个锁的加锁。

```
synchronized (this) { //此处自动加锁
  // x是共享变量,初始值=10
  if (this.x < 12) {
    this.x = 12;
  }
} //此处自动解锁
```

- (3)volatile变量规则：对一个volatile域的写，happens-before于任意后续对这个volatile域的读。

```
对一个volatile域的写，happens-before于任意后续对这个volatile域的读

这个就有点费解了，对一个 volatile 变量的写操作相对于后续对这个 volatile 变量的读操作可见，这怎么看都是禁用缓存的意思啊，貌似和 1.5 版本以前的语义没有变化啊（前面讲的1.5版本前允许volatile变量和普通变量之间重排序）？如果单看这个规则，的确是这样，但是如果我们关联一下规则 4，你就能感受到变化了
```

- (4)传递性：如果A happens-before B，且B happens-before C，那么A happens-before C。

```
class VolatileExample {
  int x = 0;
  volatile boolean v = false;
  public void writer() {
    x = 42;
    v = true;
  }
  public void reader() {
    if (v == true) {
      // 这里x会是多少呢？
    }
  }
}
```

![](https://image.avalon-zheng.xin/02efe79d-1f25-455c-8e82-936b2abd59cf "")

从图中，我们可以看到：

> “x=42” Happens-Before 写变量 “v=true” ，这是规则 1 的内容；
写变量“v=true” Happens-Before 读变量 “v=true”，这是规则 3 的内容 。
再根据这个传递性规则，我们得到结果：“x=42” Happens-Before 读变量“v=true”。这意味着什么呢？
如果线程 B 读到了“v=true”，那么线程 A 设置的“x=42”对线程 B 是可见的。也就是说，线程 B 能看到 “x == 42” ，有没有一种恍然大悟的感觉？这就是 1.5 版本对 volatile 语义的增强，这个增强意义重大，1.5 版本的并发工具包（java.util.concurrent）就是靠 volatile 语义来搞定可见性的。


- (5)start()规则：如果线程A执行操作ThreadB.start()（启动线程B），那么A线程的ThreadB.start()操作happens-before于线程B中的任意操作。

```
这条是关于线程启动的。它是指主线程 A 启动子线程 B 后，子线程 B 能够看到主线程在启动子线程 B 前的操作。
```

- (6)Join()规则：如果线程A执行操作ThreadB.join()并成功返回，那么线程B中的任意操作happens-before于线程A从ThreadB.join()操作成功返回。

```
如果线程A执行操作ThreadB.join()并成功返回，那么线程B中的任意操作happens-before于线程A从ThreadB.join()操作成功返回。

通过上面6中 happens-before 规则的组合就能为我们程序员提供一致的内存可见性。 常用的就是规则1和其他规则结合，为我们编写并发程序提供可靠的内存可见性模型。
```

### 总结
在 Java 语言里面，Happens-Before 的语义本质上是一种可见性，A Happens-Before B 意味着 A 事件对 B 事件来说是可见的，无论 A 事件和 B 事件是否发生在同一个线程里。例如 A 事件发生在线程 1 上，B 事件发生在线程 2 上，Happens-Before 规则保证线程 2 上也能看到 A 事件的发生。

JMM的设计分为两部分，一部分是面向我们程序员提供的，也就是happens-before规则，它通俗易懂的向我们程序员阐述了一个强内存模型，我们只要理解 happens-before规则，就可以编写并发安全的程序了。 另一部分是针对JVM实现的，为了尽可能少的对编译器和处理器做约束，从而提高性能，JMM在不影响程序执行结果的前提下对其不做要求，即允许优化重排序。 我们只需要关注前者就好了，也就是理解happens-before规则。毕竟我们是做程序员的，术业有专攻，能写出安全的并发程序就好了。
