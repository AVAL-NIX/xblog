---
submitToken: A5FEB15D0EAF045CAF37FB30CDB35203E21EFADE1F5D315151647719EC788999
title: Netty的零拷贝
channel: topic
labels: netty
---

- （1）Netty提供CompositeByteBuf组合缓冲区类，可以将多个ByteBuf合并为一个逻辑上的ByteBuf，避免了各个ByteBuf之间的拷贝。

- （2）Netty提供了ByteBuf的浅层复制操作（slice、duplicate），可以将ByteBuf分解为多个共享同一个存储区域的ByteBuf，避免内存的拷贝。

- （3）在使用Netty进行文件传输时，可以调用FileRegion包装的transferTo()方法直接将文件缓冲区的数据发送到目标通道，避免普通的循环读取文件数据和写入通道所导致的内存拷贝问题。

- （4）在将一个byte数组转换为一个ByteBuf对象的场景下，Netty提供了一系列的包装类，避免了转换过程中的内存拷贝。

- （5）如果通道接收和发送ByteBuf都使用直接内存进行Socket读写，就不需要进行缓冲区的二次拷贝。如果使用JVM的堆内存进行Socket读写，那么JVM会先将堆内存Buffer拷贝一份到直接内存再写入Socket中，相比于使用直接内存，这种情况在发送过程中会多出一次缓冲区的内存拷贝。所以，在发送ByteBuffer到Socket时，尽量使用直接内存而不是JVM堆内存。


> Netty中的零拷贝和操作系统层面上的零拷贝是有区别的，不能混淆，我们所说的Netty零拷贝完全是基于Java层面或者说用户空间的，它更多的是偏向于应用中的数据操作优化，而不是系统层面的操作优化。

## 操作系统层面上的零拷贝


### 普通sendfile实现；（2次用户态到内核态切换+3次拷贝）
应用程序调用sendfile系统调用，进行第一次用户态到内核态上下文切换；
通过DMA copy将磁盘文件copy到kernel buffer，进行第一次拷贝；
将kernel buffer的数据拷贝到socket buffer，进行第二次拷贝；
sendfile系统调用返回，进行第二次用户态到内核态上下文切换；
将kernel buffer刷新到protocal engine，进行第三次拷贝；


### 带有DMA gather的sendfile实现（2次用户态到内核态切换+2次拷贝）

应用程序调用sendfile系统调用，进行第一次用户态到内核态上下文切换；
通过DMA copy将磁盘文件copy到kernel buffer，进行第一次拷贝；
将文件描述符信息拷贝到socket buffer；（kernel buffer的内存地址和偏移量）
sendfile系统调用返回，进行第二次用户态到内核态上下文切换；
DMA gather copy将根据socket buffer给出的文件描述符信息，将kernel buffer的数据拷贝到protocol engine，进行第二次拷贝；

### mmap实现（4次用户态到内核态切换+3次拷贝）

应用程序调用mmap系统调用，进行第一次用户态到内核态上下文切换；
通过DMA copy将磁盘文件拷贝到kernel buffer；
mmap系统调用返回，进行第二次用户态到内核态上下文切换；
应用程序通过write系统调用直接写共享内存，进行第三次内核态到用户态上下文切换；
将kernel buffer中的数据copy到socket buffer，进行第二次拷贝；
write系统调用返回，进行第四次用户态到内核态上下文切换；
将socket buffer的数据刷新到协议引擎，进行第三次拷贝；

> 用户空间的进程直接将虚拟地址空间映射到内核缓冲区，无需将处于内核的数据拷贝到用户进程

![](https://image.avalon-zheng.xin/b83f1984-a454-481e-b497-7e26cc679400 "")

> 简单来说就是  用户空间跟内核空间直接用共享内存少拷贝一次。 就是说0拷贝了