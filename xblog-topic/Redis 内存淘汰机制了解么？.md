---
submitToken: CB1240F5DD93A5953B6D52844E54687794D830E60A799DA67E5A15033142227A
title: Redis 内存淘汰机制了解么？？
channel: topic
labels: redis
---



Redis 提供 6 种数据淘汰策略：
- volatile-lru（least recently used）：从已设置过期时间的数据集  ,挑选最近最少使用的数据淘汰

- allkeys-lru（least recently used）：当内存不足以容纳新写入数据时，在键空间中，移除最近最少使用的 key（这个是最常用的）


- volatile-random：从已设置过期时间的数据集（server.db[i].expires）中任意选择数据淘汰

- allkeys-random：从数据集（server.db[i].dict）中任意选择数据淘汰

- no-eviction：禁止驱逐数据，也就是说当内存不足以容纳新写入数据时，新写入操作会报错。这个应该没人使用吧！

- volatile-ttl：从已设置过期时间的数据集（server.db[i].expires）中挑选将要过期的数据淘汰

4.0 版本后增加以下两种：

- volatile-lfu（least frequently used）：LFU是淘汰一段时间内，使用次数最少的页面

- allkeys-lfu（least frequently used）：当内存不足以容纳新写入数据时，在键空间中，移除最不经常使用的 key

> 相关问题：MySQL 里有 2000w 数据，Redis 中只存 20w 的数据，如何保证 Redis 中的数据都是热点数据?