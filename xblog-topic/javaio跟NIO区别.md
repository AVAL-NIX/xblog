---
submitToken: 39707604CE1E2867E0BCEA59415D3C42A317E86E201866FBA6D0E4B9FEE7DC7B
title: javaio跟NIO区别
channel: topic
labels: base
---

## OIO是面向流（Stream Oriented）的，NIO是面向缓冲区（BufferOriented）的。

在一般的OIO操作中，面向字节流或字符流的IO操作总是以流式的方式顺序地从一个流（Stream）中读取一个或多个字节，因此，我们不能随意改变读取指针的位置。在NIO操作中则不同，NIO中引入了Channel和Buffer的概念。面向缓冲区的读取和写入只需要从通道读取数据到缓冲区中，或将数据从缓冲区写入通道中。NIO不像OIO那样是顺序操作，它可以随意读取Buffer中任意位置的数据。


## OIO的操作是阻塞的，而NIO的操作是非阻塞的。

OIO操作都是阻塞的。例如，我们调用一个read方法读取一个文件的内容，调用read的线程就会被阻塞，直到read操作完成。

在NIO模式中，当我们调用read方法时，如果此时有数据，则read读取数据并返回；如果此时没有数据，则read也会直接返回，而不会阻塞当前线程。

NIO的非阻塞是如何做到的呢？其实在上一章已经揭晓答案，即NIO使用了通道和通道的多路复用技术。

## OIO没有选择器（Selector）的概念，而NIO有选择器的概念。

NIO的实现是基于底层选择器的系统调用的，所以NIO需要底层操作系统提供支持；而OIO不需要用到选择器。