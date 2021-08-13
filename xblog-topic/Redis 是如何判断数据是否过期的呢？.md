---
submitToken: A36817ED55690A7B69DF9574AB7F8EBCF59EF95FCC006BE59743D41B1ADAC467
title: Redis 是如何判断数据是否过期的呢？
channel: topic
labels: redis
---

Redis 通过一个叫做过期字典（可以看作是 hash 表）来保存数据过期的时间。过期字典的键指向 Redis 数据库中的某个 key(键)，过期字典的值是一个 long long 类型的整数，这个整数保存了 key 所指向的数据库键的过期时间（毫秒精度的 UNIX 时间戳）。


![](https://image.avalon-zheng.xin/84e84b70-aa7e-4b14-a50e-29c08a79e78f "")