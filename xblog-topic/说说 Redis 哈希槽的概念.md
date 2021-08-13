---
submitToken: 551D2C12BEB21F8B5BF1CBA015062EDAC611B9CB872A504639E13D799449167A
title: 说说 Redis 哈希槽的概念？
channel: topic
labels: redis
---

Redis 集群没有使用一致性 hash,而是引入了哈希槽的概念，Redis 集群有 16384 个哈希槽，每个 key 通过 CRC16 校验后对 16384 取模来决定放置哪个槽，集群的每个节点负责一部分 hash 槽。


## 新增机器怎么扩展哈希槽的？

手工控制的， 可以手动指定一个redis控制那些hash槽范围