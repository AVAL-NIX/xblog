---
submitToken: 732E8F28D2629E96957EAE3F2CF4B98B0D11E941C1D7B9A223511C5FA8DBE2C9
title: MQ如何保证消息顺序
channel: topic
labels: mq
---

分布式环境下. 如果到了 M1 M2 消息到了不同的服务上. 并不能保证M1先行于M2消费.

## 1.为什么要保证顺序
消息队列中的若干消息如果是对同一个数据进行操作，这些操作具有前后的关系，必须要按前后的顺序执行，否则就会造成数据异常。举例：
比如通过mysql binlog进行两个数据库的数据同步，由于对数据库的数据操作是具有顺序性的，如果操作顺序搞反，就会造成不可估量的错误。比如数据库对一条数据依次进行了 插入->更新->删除操作，这个顺序必须是这样，如果在同步过程中，消息的顺序变成了 删除->插入->更新，那么原本应该被删除的数据，就没有被删除，造成数据的不一致问题。

## 2.出现顺序错乱的场景
### （1）rabbitmq
> ①一个queue，有多个consumer去消费，这样就会造成顺序的错误，consumer从MQ里面读取数据是有序的，但是每个consumer的执行时间是不固定的，无法保证先读到消息的consumer一定先完成操作，这样就会出现消息并没有按照顺序执行，造成数据顺序错误。


> ②一个queue对应一个consumer，但是consumer里面进行了多线程消费，这样也会造成消息消费顺序错误。


### （2）kafka
> ①kafka一个topic，一个partition，一个consumer，但是consumer内部进行多线程消费，这样数据也会出现顺序错乱问题。


> ②具有顺序的数据写入到了不同的partition里面，不同的消费者去消费，但是每个consumer的执行时间是不固定的，无法保证先读到消息的consumer一定先完成操作，这样就会出现消息并没有按照顺序执行，造成数据顺序错误。


## 3.保证消息的消费顺序

###（1）rabbitmq

> ①拆分多个queue，每个queue一个consumer，就是多一些queue而已，确实是麻烦点；这样也会造成吞吐量下降，可以在消费者内部采用多线程的方式取消费。

![](https://image.avalon-zheng.xin/55657f2e-6b8e-4c2a-a8b7-5f1f56f8990a "")

> 或者就一个queue但是对应一个consumer，然后这个consumer内部用内存队列做排队，然后分发给底层不同的worker来处理

![](https://image.avalon-zheng.xin/bf8b14bc-9ab0-4a34-955d-427b5af79b4e "")

### （2）kafka

> ①确保同一个消息发送到同一个partition，一个topic，一个partition，一个consumer，内部单线程消费。
![](https://image.avalon-zheng.xin/bafe45da-5fe0-4083-a7dd-e9b2aeaa4baa "")

单线程保证顺序.png

> ②写N个内存queue，然后N个线程分别消费一个内存queue即可
![](https://image.avalon-zheng.xin/ae699da3-3807-47c3-be04-ec76e95d7ab9 "")