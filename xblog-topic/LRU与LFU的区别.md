---
submitToken: 3401132FEE156AC8F13C33F3DE064D20975B1366056D4001569E8D7D20DE18A3
title: LRU与LFU的区别
channel: topic
labels: redis
---


- LRU(Least Recently Used ):淘汰最后被访问时间最久的元素。(时间维度)

缺点：可能会由于一次冷数据的批量查询而误导大量热点的数据。

- LFU(Least Frequently Used)：淘汰最近访问频率最小的元素。（频次维度）

缺点：1. 最新加入的数据常常会被踢除，因为其起始方法次数少。 2. 如果频率时间度量是1小时，则平均一天每个小时内的访问频率1000的热点数据可能会被2个小时的一段时间内的访问频率是1001的数据剔除掉
