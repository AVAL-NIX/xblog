---
submitToken: 00C7BE3DE50D8D32A3E6561DE483948BF77421B5EDF3C7DCD282A74892A33EB7
title: netty运用的设计模式
channel: topic
labels: redis
---

## 单例模式

netty运用的设计模式

## 策略模式

策略模式要点回顾如下。

（1）封装一系列可相互替换的算法家族。


（2）动态选择某一个策略。


Netty在根据CPU核数分配线程数量的一个优化时，如果是2的平方则采用PowerOfTwoEventExecutorChooser来创建EventExecutorChooser，如果不是2的平方则采用GenericEventExecutorChooser来创建EventExecutorChooser，这里用的是三元运算选择策略，具体代码如下。


## 装饰者模式

装饰者模式要点回顾如下。

（1）装饰者和被装饰者实现同一个接口。

（2）装饰者通常继承被装饰者，同宗同源。

（3）动态修改、重载被装饰者的方法。


从Netty的ByteBuf类结构图可以看到，ByteBuf的直接实现类有五个，忽略WrappedByteBuf这个类，其实直接实现类有四个。为什么要忽略掉WrappedByteBuf呢？因为它是ByteBuf装饰者的基类，本身没有任何实现功能。来看WrappedByteBuf的代码，主要功能就是保存被装饰者的引用。

##  观察者模式

观察者模式要点回顾如下。

（1）两个角色：观察者和被观察者。

（2）观察者订阅消息，被观察者发布消息。

（3）订阅则能收到消息，取消订阅则收不到消息。


Netty里面的观察者和被观察者模式一般用Promise和Future来实现。


项目中用得比较多的一个方法就是channel.writeAndFlush()方法。当调用channel.writeAndFlush()方法的时候，实际上就是创建了一个被观察者ChannelFuture，来看源码。

## 责任链
责任链是指多个对象都有机会处理同一个请求，从而避免请求的发送者和接收者之间的耦合关系。然后，将这些对象连成一条链，并且沿着这条链往下传递请求，直到有一个对象可以处理它为止。在处理过程中，每个对象只处理它自己关心的那一部分，不相关的部分可以继续往下传递，直到链中的某个对象不想处理，可以将请求终止或丢弃。责任链模式要点回顾如下。


（1）需要有一个顶层责任处理接口。


（2）需要有动态创建链、添加和删除责任处理器的接口。

（3）需要有上下文机制。

（4）需要有责任终止机制。


Netty的Pipeline就是采用了责任链设计模式，底层采用双向链表的数据结构，将链上的各个处理器串联起来。

客户端每一个请求到来，Netty都认为是Pipeline中所有的处理器都有机会处理它。因此，对于入栈的请求，全部从头节点开始往后传播，一直传播到尾节点（来到尾节点的msg才会被释放）。

Netty的责任链模式中的组件如下。

（1）责任处理器接口ChannelHandler。

（2）添加删除责任处理器的接口ChannelPipeline。

（3）上下文组件ChannelHandlerContext，可获得用户记需要的数据。

（4）终止责任链的

## 工厂模式

工厂模式的要点就是将创建对象的逻辑封装起来。我们最先接触

ReflectiveChannelFactory就是专门用来创建Channel的工厂，它接收一个Class对象，然后调用Class的newInstance()方法，将创建好的对象返回去。