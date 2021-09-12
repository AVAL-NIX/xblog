---
submitToken: 1EB55A67A8CF627C973247A735BAF5DFB441AD13CD95AEEA8F48DA0B4C55F6FB
title: netty基本知识
channel: topic
labels: netty
---

## 基本知识

NIO 3个重要的知识点

```
Channel（通道）
Buffer（缓冲区）
Selector（选择器）
```

## Channel（通道）

在OIO中，同一个网络连接会关联到两个流：
一个是输入流（Input Stream），另一个是输出流（Output Stream）。Java应用程序通过这两个流不断地进行输入和输出的操作。

在NIO中，一个网络连接使用一个通道表示，所有NIO的IO操作都是通过连接通道完成的。一个通道类似于OIO中两个流的结合体，既可以从通道读取数据，也可以向通道写入数据。

> 4种Channel实现：FileChannel、SocketChannel、ServerSocketChannel、DatagramChannel。

对于以上四种通道，说明如下：

（1）FileChannel：文件通道，用于文件的数据读写

（2）SocketChannel：套接字通道，用于套接字TCP连接的数据读写。

（3）ServerSocketChannel：服务器套接字通道（或服务器监听通道），允许我们监听TCP连接请求，为每个监听到的请求创建一个SocketChannel通道。

（4）DatagramChannel：数据报通道，用于UDP的数据读写。


## Buffer类 (通过channel读取写入数据)

> Buffer类是一个非线程安全类。

NIO的Buffer本质上是一个内存块，既可以写入数据，也可以从中读取数据。JavaNIO中代表缓冲区的Buffer类是一个抽象类，位于java.nio包中。

Buffer类是一个抽象类，对应于Java的主要数据类型。在NIO中，有8种缓冲区类，分别是ByteBuffer、CharBuffer、DoubleBuffer、FloatBuffer、IntBuffer、LongBuffer、ShortBuffer、MappedByteBuffer。前7种Buffer类型覆盖了能在IO中传输的所有Java基本数据类型，第8种类型是一种专门用于内存映射的ByteBuffer类型。不同的Buffer子类可以操作的数据类型能够通过名称进行判断，比如IntBuffer只能操作Integer类型的对象。实际上，使用最多的是ByteBuffer（二进制字节缓冲区）类型，后面的章节会看到它的具体使用。


Buffer类额外提供了一些重要的属性，其中有三个重要的成员属性：capacity（容量）、position（读写位置）和limit（读写的限制）

- capacity属性

Buffer类的capacity属性表示内部容量的大小。一旦写入的对象数量超过了capacity，缓冲区就满了，不能再写入了。

- position（读写位置）

Buffer类的position属性表示当前的位置。

在写模式下，position值的变化规则如下：

（1）在刚进入写模式时，position值为0，表示当前的写入位置为从头开始。

（2）每当一个数据写到缓冲区之后，position会向后移动到下一个可写的位置。

（3）初始的position值为0，最大可写值为limit-1。当position值达到limit时，缓冲区就已经无空间可写了。

在读模式下，position值的变化规则如下：

（1）当缓冲区刚开始进入读模式时，position会被重置为0。

（2）当从缓冲区读取时，也是从position位置开始读。读取数据后，position向前移动到下一个可读的位置。

（3）在读模式下，limit表示可读数据的上限。position的最大值为最大可读上限limit，当position达到limit时表明缓冲区已经无数据可读。

可以调用flip()方法将缓冲区变成读模式，flip为翻转的意思。

- limit（读写的限制）

在不同的模式下，limit值的含义是不同的，具体分为以下两种情况：

（1）在写模式下，limit属性值的含义为可以写入的数据最大上限。在刚进入写模式时，limit的值会被设置成缓冲区的capacity值，表示可以一直将缓冲区的容量写满。

（2）在读模式下，limit值的含义为最多能从缓冲区读取多少数据。

```
从案例2中可以总结出使用Buffer一般遵循下面几个步骤：

分配空间

ByteBuffer buf = ByteBuffer.allocate(20);

写入数据到Buffer

int bytesRead = fileChannel.read(buf);

调用filp()方法

buf.flip();

如说我们在Buffer中有一个`helloword`字符串。并且我们Buffer的 capacity设置的为20，则：
当前buffer的各项值：

position = 9
limit = 20
capacity = 20

执行flip后：
position = 0
limit = 9
capacity = 20

执行clear 后：
position = 0
limit = 20
capacity = 20
```

- allocate()

分配一个内存空间,大小为1024的

```
ByteBuffer buf = ByteBuffer.allocate(1024);
```

- put()

写值方法

```
buf.put(1111)
```

- flip()

向缓冲区写入数据之后，是否可以直接从缓冲区读取数据呢？不能！这时缓冲区还处于写模式，如果需要读取数据，要将缓冲区转换成读模式。flip()翻转方法是Buffer类提供的一个模式转变的重要方法，作用是将写模式翻转成读模式


- get()

调用flip()方法将缓冲区切换成读模式之后，就可以开始从缓冲区读取数据了。读取数据的方法很简单，可以调用get()方法每次从position的位置读取一个数据，并且进行相应的缓冲区属性的调整。

```
读完后，postion会一直增大，直到跟limit相等
```

> 缓冲区是不是可以重复读呢？答案是可以的，既可以通过倒带方法rewind()去完成，也可以通过mark()和reset()两个方法组合实现。

- rewind()

重读


- mark()和reset()

mark()和reset()两个方法是配套使用的：Buffer.mark()方法将当前position的值保存起来放在mark属性中，让mark属性记住这个临时位置；然后可以调用Buffer.reset()方法将mark的值恢复到position中。


- clear()

在读模式下，调用clear()方法将缓冲区切换为写模式。此方法的作用是：

（1）将position清零。

（2）limit设置为capacity最大容量值，可以一直写入，直到缓冲区写满。



## Selector

选择器是什么？选择器和通道的关系又是什么？

简单地说，选择器的使命是完成IO的多路复用，其主要工作是通道的注册、监听、事件查询。一个通道代表一条连接通路，通过选择器可以同时监控多个通道的IO（输入输出）状况。选择器和通道的关系是监控和被监控的关系。

选择器提供了独特的API方法，能够选出（select）所监控的通道已经发生了哪些IO事件，包括读写就绪的IO操作事件。

在NIO编程中，一般是一个单线程处理一个选择器，一个选择器可以监控很多通道。所以，通过选择器，一个单线程可以处理数百、数千、数万甚至更多的通道。在极端情况下（数万个连接），只用一个线程就可以处理所有的通道，这样会大量地减少线程之间上下文切换的开销。

```
可供选择器监控的通道IO事件类型包括以下四种：
（1）可读：SelectionKey.OP_READ。
（2）可写：SelectionKey.OP_WRITE。
（3）连接：SelectionKey.OP_CONNECT。
（4）接收：SelectionKey.OP_ACCEPT。
```

> socket连接事件的核心原理和TCP连接的建立过程有关。关于TCP的核心原理和连接建立时的三次握手、四次挥手知识，请参阅本书后面有关TCP原理的内容。


## SelectableChannel


并不是所有的通道都是可以被选择器监控或选择的。例如，FileChannel就不能被选择器复用。判断一个通道能否被选择器监控或选择有一个前提：判断它是否继承了抽象类SelectableChannel（可选择通道），如果是，就可以被选择，否则不能被选择。

简单地说，一个通道若能被选择，则必须继承SelectableChannel类。SelectableChannel类是何方神圣呢？

它提供了实现通道可选择性所需要的公共方法。Java NIO中所有网络连接socket通道都继承了SelectableChannel类，都是可选择的。FileChannel并没有继承SelectableChannel，因此不是可选择通道。

## SelectionKey

通道和选择器的监控关系注册成功后就可以选择就绪事件，具体的选择工作可调用Selector的select()方法来完成。通过select()方法，选择器可以不断地选择通道中所发生操作的就绪状态，返回注册过的那些感兴趣的IO事件。换句话说，一旦在通道中发生了某些IO事件（就绪状态达成），并且是在选择器中注册过的IO事件，就会被选择器选中，并放入SelectionKey（选择键）的集合中。


### 选择器使用流程

![](https://image.avalon-zheng.xin/5d3f699a-fa65-4702-858e-03bf0b7db39f "")


用于选择就绪的IO事件的select()方法有多个重载的实现版本，具体如下：

（1）select()：阻塞调用，直到至少有一个通道发生了注册的IO事件。

（2）select(long timeout)：和select()一样，但最长阻塞时间为timeout指定的毫秒数。

（3）selectNow()：非阻塞，不管有没有IO事件都会立刻返回。select()方法的返回值是整数类型（int），表示发生了IO事件的数量，即从上一次select到这一次select之间有多少通道发生了IO事件，更加准确地说是发生了选择器感兴趣（注册过）的IO事件数。


> Selector的wakeup()用来唤醒select()

