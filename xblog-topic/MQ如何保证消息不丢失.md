---
submitToken: 4F5C0FDCACFB46CBBF5FF887E46887113E7F08BB6FDDC9EDCB5FB812FDE587DD
title: MQ如何保证消息不丢失
channel: topic
labels: message
---

丢数据一般分为两种，

- 一种是mq把消息丢了，

- 一种就是消费时将消息丢了。


下面从rabbitmq和kafka分别说一下，丢失数据的场景，

## A:生产者弄丢了数据
生产者将数据发送到rabbitmq的时候，可能在传输过程中因为网络等问题而将数据弄丢了。

## B:rabbitmq自己丢了数据
如果没有开启rabbitmq的持久化，那么rabbitmq一旦重启，那么数据就丢了。所依必须开启持久化将消息持久化到磁盘，这样就算rabbitmq挂了，恢复之后会自动读取之前存储的数据，一般数据不会丢失。除非极其罕见的情况，rabbitmq还没来得及持久化自己就挂了，这样可能导致一部分数据丢失。

## C：消费端弄丢了数据
主要是因为消费者消费时，刚消费到，还没有处理，结果消费者就挂了，这样你重启之后，rabbitmq就认为你已经消费过了，然后就丢了数据。


## 如何防止消息丢失
# （1）rabbitmq
### A:生产者丢失消息

- 开启事务
- 启用confirm模式

①：可以选择使用rabbitmq提供是事物功能，就是生产者在发送数据之前开启事物，然后发送消息，如果消息没有成功被rabbitmq接收到，那么生产者会受到异常报错，这时就可以回滚事物，然后尝试重新发送；如果收到了消息，那么就可以提交事物。
```
  channel.txSelect();//开启事物
  try{
      //发送消息
  }catch(Exection e){
      channel.txRollback()；//回滚事物
      //重新提交
  }
```
缺点：rabbitmq事物已开启，就会变为同步阻塞操作，生产者会阻塞等待是否发送成功，太耗性能会造成吞吐量的下降。

②：可以开启confirm模式。在生产者哪里设置开启了confirm模式之后，每次写的消息都会分配一个唯一的id，然后如何写入了rabbitmq之中，rabbitmq会给你回传一个ack消息，告诉你这个消息发送OK了；如果rabbitmq没能处理这个消息，会回调你一个nack接口，告诉你这个消息失败了，你可以进行重试。而且你可以结合这个机制知道自己在内存里维护每个消息的id，如果超过一定时间还没接收到这个消息的回调，那么你可以进行重发。
```
    //开启confirm
    channel.confirm();
    //发送成功回调
    public void ack(String messageId){

    }

    // 发送失败回调
    public void nack(String messageId){
        //重发该消息
    }
```

二者不同
事务机制是同步的，你提交了一个事物之后会阻塞住，但是confirm机制是异步的，发送消息之后可以接着发送下一个消息，然后rabbitmq会回调告知成功与否。
一般在生产者这块避免丢失，都是用confirm机制。
### B:rabbitmq自己弄丢了数据

> 设置消息持久化到磁盘

设置消息持久化到磁盘。设置持久化有两个步骤：
①创建queue的时候将其设置为持久化的，这样就可以保证rabbitmq持久化queue的元数据，但是不会持久化queue里面的数据。
②发送消息的时候讲消息的deliveryMode设置为2，这样消息就会被设为持久化方式，此时rabbitmq就会将消息持久化到磁盘上。
必须要同时开启这两个才可以。

而且持久化可以跟生产的confirm机制配合起来，只有消息持久化到了磁盘之后，才会通知生产者ack，这样就算是在持久化之前rabbitmq挂了，数据丢了，生产者收不到ack回调也会进行消息重发。
### C:消费者弄丢了数据
> 关闭自动ack手动ACK

使用rabbitmq提供的ack机制，首先关闭rabbitmq的自动ack，然后每次在确保处理完这个消息之后，在代码里手动调用ack。这样就可以避免消息还没有处理完就ack。



# （2）kafka
## A:消费端弄丢了数据

> 关闭自动提交offset 手动提交offset

关闭自动提交offset，在自己处理完毕之后手动提交offset，这样就不会丢失数据。

## B:kafka弄丢了数据

> ack=all

一般要求设置4个参数来保证消息不丢失：
①给topic设置 replication.factor参数：这个值必须大于1，表示要求每个partition必须至少有2个副本。

②在kafka服务端设置min.isync.replicas参数：这个值必须大于1，表示 要求一个leader至少感知到有至少一个follower在跟自己保持联系正常同步数据，这样才能保证leader挂了之后还有一个follower。

③在生产者端设置acks=all：表示 要求每条每条数据，必须是写入所有replica副本之后，才能认为是写入成功了

④在生产者端设置retries=MAX(很大的一个值，表示无限重试)：表示 这个是要求一旦写入事变，就无限重试

## C：生产者弄丢了数据

> ack=all

如果按照上面设置了ack=all，则一定不会丢失数据，要求是，你的leader接收到消息，所有的follower都同步到了消息之后，才认为本次写成功了。如果没满足这个条件，生产者会自动不断的重试，重试无限次。
