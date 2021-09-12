---
submitToken: 22587223F87D2C8BEEB925034DA92FB1BDFE78DA63D848A459876B33B3FE9CDD
title: IO模型
channel: topic
labels: redis
---

## 同步阻塞IO (BIO)

用户态需要等待内核态返回消息

## 同步非阻塞IO (NIO）

用户态不需要等待内核态返回消息，但是需要不断轮训内核态，浪费CPU资源

## IO多路复用（JAVA中的NIO）

如何避免同步非阻塞IO模型中轮询等待的问题呢？

答案是采用IO多路复用模型。

目前支持IO多路复用的系统调用有select、poll,epoll等。几乎所有的操作系统都支持select系统调用，

它具有良好的跨平台特性。epoll是在Linux 2.6内核中提出的，是select系统调用的Linux增强版本。在IO多路复用模型中通过select/epoll系统调用，单个应用程序的线程可以不断地轮询成百上千的socket连接的就绪状态，当某个或者某些socket网络连接有IO就绪状态时就返回这些就绪的状态（或者说就绪事件）。


举个例子来说明IO多路复用模型的流程。发起一个多路复用IO的read操作的系统调用，流程如下：

（1）选择器注册。首先，将需要read操作的目标文件描述符（socket连接）提前注册到Linux的select/epoll选择器中，在Java中所对应的选择器类是Selector类。然后，开启整个IO多路复用模型的轮询流程。

（2）就绪状态的轮询。通过选择器的查询方法，查询所有提前注册过的目标文件描述符（socket连接）的IO就绪状态。通过查询的系统调用，内核会返回一个就绪的socket列表。当任何一个注册过的socket中的数据准备好或者就绪了就说明内核缓冲区有数据了，内核将该socket加入就绪的列表中，并且返回就绪事件。

（3）用户线程获得了就绪状态的列表后，根据其中的socket连接发起read系统调用，用户线程阻塞。内核开始复制数据，将数据从内核缓冲区复制到用户缓冲区。

（4）复制完成后，内核返回结果，用户线程才会解除阻塞的状态，用户线程读取到了数据，继续执行。

![](https://image.avalon-zheng.xin/0d549bff-0a90-43b4-bbf2-8d2d2195e7e6 "")

> 在多路复用IO模型中，会有一个线程（Java中的Selector）不断去轮询多个socket的状态，只有当socket真正有读写事件时，才真正调用实际的IO读写操作。因为在多路复用IO模型中，只需要使用一个线程就可以管理多个socket，系统不需要建立新的进程或者线程，也不必维护这些线程和进程，并且只有在真正有socket读写事件进行时，才会使用IO资源，所以它大大减少了资源占用。(NIO中会有N个线程去轮训造成CPU资源浪费)

### Reactor模式有三种实现方式：

- Reactor单线程

![](https://image.avalon-zheng.xin/8dd7c90c-baa2-42a0-94e6-7248708f1b2d "")

每个客户端发起连接请求都会交给acceptor,acceptor根据事件类型交给线程handler处理，注意acceptor 处理和 handler 处理都在一个线程中处理，所以其中某个 handler 阻塞时, 会导致其他所有的 client 的 handler 都得不到执行, 并且更严重的是, handler 的阻塞也会导致整个服务不能接收新的 client 请求(因为 acceptor 也被阻塞了). 因为有这么多的缺陷, 因此单线程Reactor 模型用的比较少.

- Reactor多线程模式

![](https://image.avalon-zheng.xin/a04b94b7-e69a-4224-9683-e55c99f23e99 "")

有专门一个线程, 即 Acceptor 线程用于监听客户端的TCP连接请求.

客户端连接的 IO 操作都是由一个特定的 NIO 线程池负责. 每个客户端连接都与一个特定的 NIO 线程绑定, 因此在这个客户端连接中的所有 IO 操作都是在同一个线程中完成的.

客户端连接有很多, 但是 NIO 线程数是比较少的, 因此一个 NIO 线程可以同时绑定到多个客户端连接中.

缺点：如果我们的服务器需要同时处理大量的客户端连接请求或我们需要在客户端连接时, 进行一些权限的检查, 那么单线程的 Acceptor 很有可能就处理不过来, 造成了大量的客户端不能连接到服务器.

- Reactor主从模式

![](https://image.avalon-zheng.xin/ec18d05e-c1a3-467a-9004-4ee59ff426ab "")

Reactor 的主从多线程模型和 Reactor 多线程模型很类似, 只不过 Reactor 的主从多线程模型的 acceptor 使用了线程池来处理大量的客户端请求.


## 异步IO

![](https://image.avalon-zheng.xin/e9935c0e-f219-4e75-a096-34ec8daadd4e "")


理论上来说，异步IO是真正的异步输入输出，它的吞吐量高于IO多路复用模型的吞吐量。就目前而言，Windows系统下通过IOCP实现了真正的异步IO。在Linux系统下，异步IO模型在2.6版本才引入，JDK对它的支持目前并不完善，因此异步IO在性能上没有明显的优势。大多数高并发服务端的程序都是基于Linux系统的。因而，目前这类高并发网络应用程序的开发大多采用IO多路复用模型。大名鼎鼎的Netty框架使用的就是IO多路复用模型，而不是异步IO模型。