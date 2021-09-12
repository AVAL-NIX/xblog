---
submitToken: C83A3422CBF5CD2548A947F906A17F01E47222017388C2D4CB9CE4D6C1959E96
title: netty基本知识3
channel: topic
labels: netty
---

## 详解ByteBuf

ByteBuf的优势

- Pooling（池化），减少了内存复制和GC，提升了效率。

- 复合缓冲区类型，支持零复制。

- 不需要调用flip()方法去切换读/写模式。

- 可扩展性好。

- 可以自定义缓冲区类型。

- 读取和写入索引分开。方法的链式调用。

- 可以进行引用计数，方便重复使用。

## ByteBuf的重要属性

- readerIndex（读指针）：指示读取的起始位置。每读取一个字节，readerIndex自动增加1。一旦readerIndex与writerIndex相等，则表示ByteBuf不可读了。

- writerIndex（写指针）：指示写入的起始位置。每写一个字节，writerIndex自动增加1。一旦增加到writerIndex与capacity()容量相等，则表示ByteBuf不可写了。注意，capacity()是一个成员方法，不是一个成员属性，表示ByteBuf中可以写入的容量，而且它的值不一定是最大容量值。

- maxCapacity（最大容量）：表示ByteBuf可以扩容的最大容量。当向ByteBuf写数据的时候，如果容量不足，可以进行扩容。扩容的最大限度由maxCapacity来设定，超过maxCapacity就会报错。


![](https://image.avalon-zheng.xin/850ffa4b-670b-4cd8-9079-84ac8b501379 "")

### ByteBuf池化

ByteBuf是池化的， 所以需要调用retain增加一次，或者减少一次，都不用的话，netty会自己帮我们调用

（1）ReferenceCountUtil.retain(Object)：增加一次缓冲区引用计数的静态方法，从而防止该缓冲区被释放。

（2）ReferenceCountUtil.release(Object)：减少一次缓冲区引用计数的静态方法，如果引用计数为0，缓冲区将被释放。

### ByteBuf的分配器


Netty通过ByteBufAllocator分配器来创建缓冲区和分配内存空间。

Netty提供了两种分配器实现：PoolByteBufAllocator和UnpooledByteBufAllocator。


- PoolByteBufAllocator（池化的ByteBuf分配器）将ByteBuf实例放入池中，提高了性能，将内存碎片减少到最小；池化分配器采用了jemalloc高效内存分配的策略，该策略被好几种现代操作系统所采用。

- UnpooledByteBufAllocator是普通的未池化ByteBuf分配器，没有把ByteBuf放入池中，每次被调用时，返回一个新的ByteBuf实例；使用完之后，通过Java的垃圾回收机制回收或者直接释放（对于直接内存而言）。

> 可以在Netty程序中设置引导类Bootstrap装配的时候将PooledByteBufAllocator设置为默认的分配器。


![](https://image.avalon-zheng.xin/b6f3fb2b-931d-4024-9f7a-272851071af0 "")


![](https://image.avalon-zheng.xin/452cd39b-ddae-4f97-a2ff-010ddd310d16 "")

### ByteBuf缓冲区的类型(内存分配方式)

- 直接内存
  - 创建慢，效率高
- JVM堆内存
  - 创建快，效率低

为了快速创建ByteBuffer，Netty提供了一个非常方便的获取缓冲区的类——Unpooled，用它可以创建和使用非池化的缓冲区。Unpooled的使用也很容易，下面给出三个例子：

![](https://image.avalon-zheng.xin/9f1d33f0-3caf-4356-8de4-0218e0caf3d2 "")

> 还可以 调用ctx.alloc().buffer()方法获取ByteBuf

## ByteBuf的自动创建与自动释放

### ByteBuf的自动创建

查看Netty源代码，我们可以看到，Netty的Reactor线程会通过底层的Java NIO通道读数据。发生NIO读取的方法为AbstractNioByteChannel.NioByteUnsafe.read()


### ByteBuf的自动销毁

只要最初的ByteBuf数据包一路向后传递，进入流水线的末端，TailContext（末尾处理器）就会自动释放掉入站的ByteBuf实例。


> ByteBuf splice()切片 浅复制
