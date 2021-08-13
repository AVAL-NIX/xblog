---
submitToken: 98C24F31D17BE07D8D906E832C4958B1C42A6FC3B6047B0DA822E14A22F6EC91
title: redis的几种集群方式
channel: topic
labels: redis
---

- 单节点实例

- 主从模式（master/slaver）

> master节点挂了以后，redis就不能对外提供写服务了，因为剩下的slave不能成为master
这个缺点影响是很大的，尤其是对生产环境来说，是一刻都不能停止服务的，所以一般的生产坏境是不会单单只有主从模式的。所以有了下面的sentinel模式。

- sentinel模式（哨兵模式）

> 当使用sentinel模式的时候，客户端就不要直接连接Redis，而是连接sentinel的ip和port，由sentinel来提供具体的可提供服务的Redis实现，这样当master节点挂掉以后，sentinel就会感知并将新的master节点提供给使用者。 sentinel模式基本可以满足一般生产的需求，具备高可用性。但是当数据量过大到一台服务器存放不下的情况时，主从模式或sentinel模式就不能满足需求了，这个时候需要对存储的数据进行分片，将数据存储到多个Redis实例中，就是下面要讲的。

- cluster模式

> cluster的出现是为了解决单机Redis容量有限的问题，将Redis的数据根据一定的规则分配到多台机器。对cluster的一些理解：cluster可以说是sentinel和主从模式的结合体，通过cluster可以实现主从和master重选功能，所以如果配置两个副本三个分片的话，就需要六个Redis实例。
因为Redis的数据是根据一定规则分配到cluster的不同机器的，当数据量过大时，可以新增机器进行扩容
　　这种模式适合数据量巨大的缓存要求，当数据量不是很大使用sentinel即可。


优点：

哨兵模式基于主从复制模式，所以主从复制模式有的优点，哨兵模式也有
哨兵模式下，master挂掉可以自动进行切换，系统可用性更高

缺点：

同样也继承了主从模式难以在线扩容的缺点，Redis的容量受限于单机配置
需要额外的资源来启动sentinel进程，实现相对复杂一点，同时slave节点作为备份节点不提供服务



3. Cluster模式的优缺点

优点：

无中心架构，数据按照slot分布在多个节点。
集群中的每个节点都是平等的关系，每个节点都保存各自的数据和整个集群的状态。每个节点都和其他所有节点连接，而且这些连接保持活跃，这样就保证了我们只需要连接集群中的任意一个节点，就可以获取到其他节点的数据。
可线性扩展到1000多个节点，节点可动态添加或删除
能够实现自动故障转移，节点之间通过gossip协议交换状态信息，用投票机制完成slave到master的角色转换

缺点：

客户端实现复杂，驱动要求实现Smart Client，缓存slots mapping信息并及时更新，提高了开发难度。目前仅JedisCluster相对成熟，异常处理还不完善，比如常见的“max redirect exception”
节点会因为某些原因发生阻塞（阻塞时间大于 cluster-node-timeout）被判断下线，这种failover是没有必要的
数据通过异步复制，不保证数据的强一致性
slave充当“冷备”，不能缓解读压力
批量操作限制，目前只支持具有相同slot值的key执行批量操作，对mset、mget、sunion等操作支持不友好
key事务操作支持有线，只支持多key在同一节点的事务操作，多key分布不同节点时无法使用事务功能
