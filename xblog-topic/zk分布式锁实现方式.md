---
submitToken: DD70AFE6B24130D711F9CEF57753D7E1F6BDD02437E1E4ACEA8A5242AD2AAF2D
title: zk分布式锁实现方式
channel: topic
labels: zk
---


我们先简要介绍一些ZooKeeper（以下简称ZK）：

ZooKeeper是一种“分布式协调服务”。所谓分布式协调服务，可以在分布式系统中共享配置，协调锁资源，提供命名服务等。为读多写少的场景所设计，ZK中的节点（以下简称ZNode）非常适合用于存储少量的状态和配置信息。

对ZK常见的操作：

- create：创建节点

- delete：删除节点

- exists：判断一个节点的数据

- setdata：设置一个节点的数据

- getchildren：获取节点下的所有子节点

> 这其中，exists，getData，getChildren属于读操作。Zookeeper客户端在请求读操作的时候，可以选择是否设置Watch（监听机制）。

## 什么是Watch？

Watch机制是zk中非常有用的功能。我们可以理解成是注册在特定Znode上的触发器。当这个Znode发生改变，也就是调用了create，delete，setData方法的时候，将会触发Znode上注册的对应事件，请求Watch的客户端会接收到异步通知。

我们在实现分布式锁的时候，正是通过Watch机制，来通知正在等待的session相关锁释放的信息。

## 什么是ZNode？

ZNode就是ZK中的节点。ZooKeeper节点是有生命周期的，这取决于节点的类型。在 ZooKeeper 中，节点类型可以分为临时节点（EPHEMERAL），时序节点（SEQUENTIAL ），持久节点（PERSISTENT ）。

- 临时节点（EPHEMERAL）：

节点的生命周期跟session绑定，session创建的节点，一旦该session失效，该节点就会被删除。

- 临时顺序节点（EPHEMERAL_SEQUENTIAL）：

在临时节点的基础上增加了顺序。每个父结点会为自己的第一级子节点维护一份时序。在创建子节点的时候，会自动加上数字后缀，越后创建的节点，顺序越大，后缀越大。

- 持久节点（PERSISTENT ）：

节点创建之后就一直存在，不会因为session失效而消失。

- 持久顺序节点（PERSISTENT_SEQUENTIAL）：

与临时顺序节点同理。

## ZNode中的数据结构：

- data（znode存储的数据信息）
- acl（记录znode的访问权限，即哪些人或哪些ip可以访问本节点），
- stat（包含znode的各种元数据，比如事务id，版本号，时间戳，大小等等），
- child（当前节点的子节点引用）。

利用ZK实现分布式锁，主要得益于ZK保证了数据的强一致性。

下面说说通过zk简单实现一个保持独占的锁（利用临时节点的特性）：

> 我们可以将ZK上的ZNode看成一把锁（类似于Redis方案中的key）。多个session都去创建同一个distribute_lock节点，只会有一个创建成功的session。相当于只有该session获取到锁，其他session没有获取到锁。在该成功获锁的session失效前，锁将会一直阻塞住。session失效时，节点会自动被删除，锁被解除。（类似于Redis方案中的expire）。

上述实现方案跟Redis方案3的实现效果一样。

但是，这样的锁有没有改进的地方？当然！

1）我们可能会有可重入的需求，因此希望能有可重入的锁机制。

2）有些场景下，在争抢锁的时候，我们既不想一次争抢不到就pass，也不想一直阻塞住直到获取到锁。一个朴素的需求是，我们希望有超时时间来控制是否去上锁。更进一步，我们不想主动的去查到底是否能够加锁，我们希望能够有事件机制来通知是否能够上锁。（这里，你是不是想到了ZK的Watch机制呢？）

要满足这样的需求就需要控制时序。利用顺序临时节点和Watch机制的特性，来实现：

我们事先创建/distribute_lock节点，多个session在它下面创建临时有序节点。由于zk的特性，/distribute_lock该节点会维护一份sequence，来保证子节点创建的时序性。

具体实现如下：

1）客户端调用create()方法在/distribute_lock节点下创建EPHEMERAL_SEQUENTIAL节点。

2）客户端调用getChildren(“/distribute_lock”)方法来获取所有已经创建的子节点。

3）客户端获取到所有子节点path之后，如果发现自己在步骤1中创建的节点序号最小，那么就认为这个客户端获得了锁。

4）如果在步骤3中发现自己并非所有子节点中最小的，说明自己还没有获取到锁。此时客户端需要找到比自己小的那个节点，然后对其调用exist()方法，同时注册事件监听。需要注意是，只在比自己小一号的节点上注册Watch事件。如果在比自己都小的节点上注册Watch事件，将会出现惊群效应，要避免。

5）之后当这个被关注的节点被移除了，客户端会收到相应的通知。这个时候客户端需要再次调用getChildren(“/distribute_lock”)方法来获取所有已经创建的子节点，确保自己确实是最小的节点了，然后进入步骤3）。

Curator框架封装了对ZK的api操作。以Java为例来进行演示：

引入依赖：
```
1<dependency>
2   <groupId>org.apache.curator</groupId>
3   <artifactId>curator-recipes</artifactId>
4   <version>2.11.1</version>
5</dependency>
```
使用的时候需要注意Curator框架和ZK的版本兼容问题。

以排他锁为例，看看怎么使用：
```
 1public class TestLock {
 2
 3    public static void main(String[] args) throws Exception {
 4        //创建zookeeper的客户端
 5        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
 6        CuratorFramework client = CuratorFrameworkFactory.newClient(“ip:port", retryPolicy);
 7        client.start();
 8
 9        //创建分布式锁, 锁空间的根节点路径为/sunnyzengqi/curator/lock
10        InterProcessMutex mutex = new InterProcessMutex(client, "/sunnyzengqi/curator/lock");
11        mutex.acquire();
12        //获得了锁, 进行业务流程
13        System.out.println("Enter mutex");
14        Thread.sleep(10000);
15        //完成业务流程, 释放锁
16        mutex.release();
17        //关闭客户端
18        client.close();
19    }
20}
21
```

上面代码在业务执行的过程中，在ZK的/sunnyzengqi/curator/lock路径下，会创建一个临时节点来占位。相同的代码，在两个机器节点上运行，可以看到该路径下创建了两个临时节点：



- 运行命令echo wchc | nc localhost 2181查看watch信息：


可以看到lock1节点的session在监听节点lock0的变动。此时是lock0获取到锁。等到lock0执行完，session会失效，触发Watch机制，通知lock1的session说锁已经被释放了。这时，lock1可以来抢占锁，进而执行自己的操作。

除了简单的排它锁的实现，还可以利用ZK的特性来实现更高级的锁（比如信号量，读写锁，联锁）等，这里面有很多的玩法。

## ZooKeeper方案小结

> 能够实现很多具有更高条件的锁机制，并且由于ZK优越的session和watch机制，适用于复杂的场景。因为有久经检验的Curator框架，集成了很多基于ZK的分布式锁的api，对于Java语言非常友好。对于其他语言，虽然也有一些开源项目封装了它的api，但是稳定性和效率需要自己去实际检验。