---
submitToken: 213CA86132A90AE609AEDDED3DD9F53ADDBDF139D3F33164DDDE4EA56062BAAD
title: netty基本知识2
channel: topic
labels: netty
---

## Reactor模式

一般一个reactor 线程，一个handler线程，放在attch中。 然后读的时候取出对应的handler线程去执行

> 在单线程Reactor模式中，Reactor和Handler都在同一条线程中执行。这样，带来了一个问题：当其中某个Handler阻塞时，会导致其他所有的Handler都得不到执行。在这种场景下，被阻塞的Handler不仅仅负责输入和输出处理的传输处理器，还包括负责新连接监听的AcceptorHandler处理器，可能导致服务器无响应。这是一个非常严重的缺陷，导致单线程反应器模型在生产场景中使用得比较少。


> 没有用线程池处理handler 效率低

## 多线程版本的Reactor模式演进


用线程池的方式处理handler 效率高

分别用2个线程来跑reactor


## Netty中的Channel

Netty中不直接使用Java NIO的Channel组件，对Channel组件进行了自己的封装。Netty实现了一系列的Channel组件，为了支持多种通信协议，换句话说，对于每一种通信连接协议，Netty都实现了自己的通道。除了Java的NIO，Netty还提供了Java面向流的OIO处理通道。

```
NioSocketChannel：异步非阻塞TCP Socket传输通道。
NioServerSocketChannel：异步非阻塞TCP Socket服务端监听通道。NioDatagramChannel：异步非阻塞的UDP传输通道。
NioSctpChannel：异步非阻塞Sctp传输通道。
NioSctpServerChannel：异步非阻塞Sctp服务端监听通道。
OioSocketChannel：同步阻塞式TCP Socket传输通道。
OioServerSocketChannel：同步阻塞式TCP Socket服务端监听通道。OioDatagramChannel：同步阻塞式UDP传输通道。
OioSctpChannel：同步阻塞式Sctp传输通道。
OioSctpServerChannel：同步阻塞式Sctp服务端监听通道。
```

## Netty中的Reactor

首先为大家介绍一下Netty中的反应器组件。Netty中的反应器组件有多个实现类，这些实现类与其通道类型相互匹配。对应于NioSocketChannel通道，Netty的反应器类为NioEventLoop（NIO事件轮询）。


在Netty中，EventLoop反应器和Channel的关系是什么呢？理论上来说，一个EventLoop反应器和NettyChannel通道是一对多的关系：一个反应器可以注册成千上万的通道

![](https://image.avalon-zheng.xin/a41aa4a1-d615-4699-8d8f-191feccdea50 "")

## Netty中的Handler

可读：SelectionKey.OP_READ。
可写：SelectionKey.OP_WRITE。
连接：SelectionKey.OP_CONNECT。
接收：SelectionKey.OP_ACCEPT。

![](https://image.avalon-zheng.xin/853bea5e-e7fc-4c94-adf2-0a9df229f81f "")

（2）通道和Handler处理器实例之间是多对多的关系：

一个通道的IO事件可以被多个Handler实例处理；一个Handler处理器实例也能绑定到很多通道，处理多个通道的IO事件。问题是：通道和Handler处理器实例之间的绑定关系，Netty是如何组织的呢？


Netty设计了一个特殊的组件，叫作ChannelPipeline（通道流水线）。它像一条管道，将绑定到一个通道的多个Handler处理器实例串联在一起，形成一条流水线。ChannelPipeline的默认实现实际上被设计成一个双向链表。所有的Handler处理器实例被包装成双向链表的节点，被加入到ChannelPipeline中。


> 一个Netty通道拥有一个ChannelPipeline类型的成员属性，该属性的名称叫作pipeline。


以入站处理为例，每一个来自通道的IO事件都会进入一次ChannelPipeline。在进入第一个Handler处理器后，这个IO事件将按照既定的从前往后次序，在流水线上不断地向后流动，流向下一个Handler处理器。在向后流动的过程中，会出现3种情况：

（1）如果后面还有其他Handler入站处理器，那么IO事件可以交给下一个Handler处理器向后流动。

（2）如果后面没有其他的入站处理器，就意味着这个IO事件在此次流水线中的处理结束了。

（3）如果在中间需要终止流动，可以选择不将IO事件交给下一个Handler处理器，流水线的执行也被终止了。N

etty的通道流水线与普通的流水线不同，Netty的流水线不是单向的，而是双向的，而普通的流水线基本都是单向的。Netty是这样规定的：入站处理器的执行次序是从前到后，出站处理器的执行次序是从后到前。总之，IO事件在流水线上的执行次序与IO事件的类型是有关系的，如图5-6所示。

![](https://image.avalon-zheng.xin/66e98543-f666-4765-9e70-6ec48a00237d "")

## 详解Bootstrap

![](https://image.avalon-zheng.xin/b887ccfd-87fc-43e4-907e-c674d78d21f3 "")


在介绍ServerBootstrap的服务器启动流程之前，首先介绍一下涉及的两个基础概念：

父子通道、EventLoopGroup（事件轮询线程组）。

option的参数后面自己找


## 详解Channel

（1）ChannelFuture connect(SocketAddress address)

此方法的作用为连接远程服务器。方法的参数为远程服务器的地址，调用后会立即返回，其返回值为执行连接操作的异步任务ChannelFuture。此方法在客户端的传输通道使用。

（2）ChannelFuture bind(SocketAddress address)

此方法的作用为绑定监听地址，开始监听新的客户端连接。此方法在服务器的新连接监听和接收通道时调用。

（3）ChannelFuture close()

此方法的作用为关闭通道连接，返回连接关闭的ChannelFuture异步任务。如果需要在连接正式关闭后执行其他操作，则需要为异步任务设置回调方法；或者调用ChannelFuture异步任务的sync()方法来阻塞当前线程，一直等到通道关闭的异步任务执行完毕。

（4）Channel read()

此方法的作用为读取通道数据，并且启动入站处理。具体来说，从内部的Java NIOChannel通道读取数据，然后启动内部的Pipeline流水线，开启数据读取的入站处理。此方法的返回通道自身用于链式调用。

（5）ChannelFuture write（Object o）

此方法的作用为启程出站流水处理，把处理后的最终数据写到底层通道（如JavaNIO通道）。此方法的返回值为出站处理的异步处理任务。

（6）Channel flush()

此方法的作用为将缓冲区中的数据立即写出到对端。调用前面的write()出站处理时，并不能将数据直接写出到对端，write操作的作用在大部分情况下仅仅是写入操作系统的缓冲区，操作系统会根据缓冲区的情况决定什么时候把数据写到对端。执行flush()方法会立即将缓冲区的数据写到对端。


## EmbeddedChannel

![](https://image.avalon-zheng.xin/a8cd000f-8ab0-48cf-aa63-20f29ca9f1ae "")

最为重要的两个方法为writeInbound()和writeOutbound()方法。

（1）writeInbound()

它的使用场景是测试入站处理器。在测试入站处理器时（例如测试一个解码器），需要读取入站（Inbound）数据。可以调用writeInbound()方法，向EmbeddedChannel写入一个入站数据（如二进制ByteBuf数据包），模拟底层的入站包，从而被入站处理器处理到，达到测试的目的。


（2）writeOutbound()

它的使用场景是测试出站处理器。在测试出站处理器时（例如测试一个编码器），需要有出站（Outbound）的数据进入流水线。可以调用writeOutbound()方法，向模拟通道写入一个出站数据（如二进制ByteBuf数据包），该包将进入处理器流水线，被待测试的出站处理器所处理。


> 总之，EmbeddedChannel类既拥有通道的通用接口和方法，又增加了一些单元测试的辅助方法，在开发时是非常有用的。有关它的具体用法，后面还会结合其他Netty组件的实例反复提到。


## 详解Handler


### 　ChannelInboundHandler入站处理器

- 1. channelRegistered()

当通道注册完成后，Netty会调用fireChannelRegistered()方法，触发通道注册事件，而在通道流水线注册过的入站处理器的channelRegistered()回调方法会被调用。

- 2. channelActive()

当通道激活完成后，Netty会调用fireChannelActive()方法，触发通道激活事件，而在通道流水线注册过的入站处理器的channelActive()回调方法会被调用。

- 3. channelRead()

当通道缓冲区可读时，Netty会调用fireChannelRead()方法，触发通道可读事件，而在通道流水线注册过的入站处理器的channelRead()回调方法会被调用，以便完成入站数据的读取和处理。

- 4. channelReadComplete()

当通道缓冲区读完时，Netty会调用fireChannelReadComplete()方法，触发通道缓冲区读完事件，而在通道流水线注册过的入站处理器的channelReadComplete()回调方法会被调用。

- 5. channelInactive()

当连接被断开或者不可用时，Netty会调用fireChannelInactive()方法，触发连接不可用事件，而在通道流水线注册过的入站处理器的channelInactive()回调方法会被调用。

- 6. exceptionCaught()

当通道处理过程发生异常时，Netty会调用fireExceptionCaught()方法，触发异常捕获事件，而在通道流水线注册过的入站处理器的exceptionCaught()方法会被调用。注意，这个方法是在ChannelHandler中定义的方法，入站处理器、出站处理器接口都继承了该方法。

> 上面介绍的并不是ChannelInboundHandler的全部方法，仅仅介绍了其中几种比较重要的方法。在Netty中，入站处理器的默认实现为ChannelInboundHandlerAdapter，在实际开发中只需要继承ChannelInboundHandlerAdapter默认实现，重写自己需要的回调方法即可。

### ChannelOutboundHandler出站处理器

再强调一下，Netty出站处理的方向是通过上层Netty通道去操作底层Java IO通道，主要出站（Outbound）的操作如下：

- （1）bind()监听地址（IP+端口）

绑定：完成底层Java IO通道的IP地址绑定。如果使用TCP传输协议，这个方法用于服务端。
P地址绑定。如果使用TCP传输协议，这个方法用于服务端。

- （2）connect()连接服务端：


完成底层Java IO通道的服务端的连接操作。如果使用TCP传输协议，那么这个方法将用于客户端。

- （3）write()

写数据到底层：完成Netty通道向底层Java IO通道的数据写入操作。此方法仅仅是触发一下操作，并不是完成实际的数据写入操作。

- （4）flush()将底层缓存区的数据腾空，立即写出到对端。

- （5）read ()从底层读数据：

完成Netty通道从Java IO通道的数据读取。

- （6）disConnect()断开服务器连接：断开底层Java IO通道的socket连接。如果使用TCP传输协议，此方法主要用于客户端。

- （7）close()主动关闭通道：关闭底层的通道，例如服务端的新连接监听通道。

> 上面介绍的并不是ChannelOutboundHandler的全部方法，仅仅介绍了其中几个比较重要的方法。在Netty中，它的默认实现为ChannelOutboundHandlerAdapter。在实际开发中，只需要继承ChannelOutboundHandlerAdapter默认实现，重写自己需要的方法即可。


## 详解Pipeline

### Pipeline进站处理流程

前面讲到，一条Netty通道需要很多业务处理器来处理业务。每条通道内部都有一条流水线（Pipeline）将Handler装配起来。Netty的业务处理器流水线ChannelPipeline是基于责任链设计模式（Chain of Responsibility）来设计的，内部是一个双向链表结构，能够支持动态地添加和删除业务处理器。


![](https://image.avalon-zheng.xin/9c74568e-39aa-4036-8650-829b56c52c0e "")


> 如果channelRead 中没有   super.channelRead(ctx, msg); 不会进行链式调用

> 或者不调用     ctx.fireChannelRead(msg); 这个也是所有channelRead的入口

### Pipeline出站处理流程


为了完整地演示Pipeline出站处理流程，将新建三个极为简单的出站处理器：SimpleOutHandlerA、SimpleOutHandlerB、SimpleOutHandlerC。在ChannelInitializer处理器的initChannel()方法中，把它们加入到流水线中，添加的顺序为A→B→C。实战案例的代码如下：


![](https://image.avalon-zheng.xin/505ffcfe-8012-4771-8fd7-0a733a92229e "")

> out handle 是从底下往上的。 in handle 是从上往下的


### 在流水线上热插拔Handler

> ctx.pipeline().remove(this);
