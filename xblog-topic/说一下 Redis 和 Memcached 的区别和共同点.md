---
submitToken: E35D8431F7B2BD684AF9092DA75327E2E73AF5D69CE138085D9E76F73B30F1B0
title: 说一下 Redis 和 Memcached 的区别和共同点
channel: topic
labels: redis
---


共同点 ：
```
1. 都是基于内存的数据库，一般都用来当做缓存使用。
1. 都有过期策略。
1. 两者的性能都非常高。
```
区别 ：
```
Redis 支持更丰富的数据类型（支持更复杂的应用场景）。Redis 不仅仅支持简单的 k/v 类型的数据，同时还提供 list，set，zset，hash 等数据结构的存储。Memcached 只支持最简单的 k/v 数据类型。
Redis 支持数据的持久化，可以将内存中的数据保持在磁盘中，重启的时候可以再次加载进行使用,而 Memecache 把数据全部存在内存之中。
Redis 有灾难恢复机制。 因为可以把缓存中的数据持久化到磁盘上。
Redis 在服务器内存使用完之后，可以将不用的数据放到磁盘上。但是，Memcached 在服务器内存使用完之后，就会直接报异常。
Memcached 没有原生的集群模式，需要依靠客户端来实现往集群中分片写入数据；但是 Redis 目前是原生支持 cluster 模式的.
Memcached 是多线程，非阻塞 IO 复用的网络模型；Redis 使用单线程的多路 IO 复用模型。 （Redis 6.0 引入了多线程 IO ）
Redis 支持发布订阅模型、Lua 脚本、事务等功能，而 Memcached 不支持。并且，Redis 支持更多的编程语言。
Memcached 过期数据的删除策略只用了惰性删除，而 Redis 同时使用了惰性删除与定期删除。
```
