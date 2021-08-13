---
submitToken: EDB3658A42ABEE161DD6F57ECC7DA2EDAA6DA330D0EA8877CBF6C29451F45DC5
title: Zookeeper选举
channel: topic
labels: juc
---

### Zab协议

Zookeeper是一个很强的分布式数据一致性解决方案，比如dubbo中的注册中心就使用的Zookeeper。当然，这也是集群部署的，但是它没有采用传统的Master/Slave结构，而是引入了Leader、Follwer和Observer。Leader和Follower类似于Master/Slave，新增的Observer作用仅仅只是增加集群的读性能，它不参与Leader的选举。
节点的状态有以下几种：

- **LOOKING:** 节点正处于选主状态，不对外提供服务，直至选主结束；

- **FOLLOWING:** 作为系统的从节点，接受主节点的更新并写入本地日志；

- **LEADING:** 作为系统主节点，接受客户端更新，写入本地日志并复制到从节点


Zookeeper的状态同步是基于Zab协议实现的，Zab协议有两种模式，它们分别是崩溃恢复（选主）和消息广播（同步）。当服务启动或者在Leader崩溃后，Zab就进入了恢复模式，当Leader被选举出来，且超过一半机器完成了和 leader的状态同步以后，恢复模式就结束了。
我们来重点看下选主是怎么完成的
首先明确几个概念： - **Sid**：服务器id；

- **Zxid**：服务器的事务id，数据越新，zxid越大；zxid的高32位是epoch，低32位是zpoch内的自增id，由0开始。每次选出新的Leader，epoch会递增，同时zxid的低32位清0。

**整个选主流程如下**

1. 状态变更。服务器启动的时候每个server的状态时Looking，如果是leader挂掉后进入选举，那么余下的非Observer的Server就会将自己的服务器状态变更为Looking，然后开始进入Leader的选举状态；

1. 发起投票。**每个server会产生一个（sid，zxid）的投票**，系统初始化的时候zxid都是0，如果是运行期间，每个server的zxid可能都不同，这取决于最后一次更新的数据。将投票发送给集群中的所有机器；

1. 接收并检查投票。server收到投票后，会先检查是否是本轮投票，是否来自looking状态的server；

1. 处理投票。对自己的投票和接收到的投票进行PK：

> **先检查zxid，较大的优先为leader；如果zxid一样，sid较大的为leader；根据PK结果更新自己的投票，再次发送自己的投票**> ；

1. 统计投票。每次投票后，服务器统计投票信息，**如果有过半机器接收到相同的投票，那么leader产生**，如果否，那么进行下一轮投票；

1. 改变server状态。一旦确定了Leader，server会更新自己的状态为Following或者是Leading。选举结束。


**我们要保证选主完成后，原来的主节点已经提交的事务继续完成提交；原主节点只是提出而没提交的事务要抛弃。这也是为什么倾向于选zxid最大的从节点为主节点，因为它上边的事务最新，最与原主节点保持一致。**