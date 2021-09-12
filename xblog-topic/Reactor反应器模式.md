---
submitToken: D5A975B4CC6A58A0D318198003C2E80DE2230482AF256EB66E3D9DFB0FB3DC0D
title: 什么中Reactor反应器模式？
channel: topic
labels: redis
---

- 说到reactor反应器模式
- 从BIO 开始介绍，轮训的套接字
  - 性能差
- 然后从BIO，轮训创建线程的套接字
  - 耗费系统资源开销大
- 然后NIO，单reactor模型 - 》 多reactor
- 拆分acceptor 和NIO线程池

## Reactor编程的优点和缺点
```
6.1. 优点
1）响应快，不必为单个同步时间所阻塞，虽然Reactor本身依然是同步的；

2）编程相对简单，可以最大程度的避免复杂的多线程及同步问题，并且避免了多线程/进程的切换开销；

3）可扩展性，可以方便的通过增加Reactor实例个数来充分利用CPU资源；

4）可复用性，reactor框架本身与具体事件处理逻辑无关，具有很高的复用性；

6.2. 缺点
1）相比传统的简单模型，Reactor增加了一定的复杂性，因而有一定的门槛，并且不易于调试。

2）Reactor模式需要底层的Synchronous Event Demultiplexer支持，比如Java中的Selector支持，操作系统的select系统调用支持，如果要自己实现Synchronous Event Demultiplexer可能不会有那么高效。

3） Reactor模式在IO读写数据时还是在同一个线程中实现的，即使使用多个Reactor机制的情况下，那些共享一个Reactor的Channel如果出现一个长时间的数据读写，会影响这个Reactor中其他Channel的相应时间，比如在大文件传输时，IO操作就会影响其他Client的相应时间，因而对这种操作，使用传统的Thread-Per-Connection或许是一个更好的选择，或则此时使用改进版的Reactor模式如Proactor模式。
```

