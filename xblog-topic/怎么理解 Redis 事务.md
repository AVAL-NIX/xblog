---
submitToken: C95814DC374E69677F28CD9D72C048C71005EB67DBF472449D71987B3F1C629D
title: 怎么理解 Redis 事务？有那些指令？
channel: topic
labels: redis
---

事务是一个单独的隔离操作：事务中的所有命令都会序列化、按顺序地执行，事务在执行的过程中，不会 被其他客户端发送来的命令请求所打断。 事务是一个原子操作：事务中的命令要么全部被执行，要么全部都不执行。


## 事务的主要命令


MULTI(begin): 事务开始
EXEC(commit): 事务提交
DISCARD :取消事务，放弃执行事务块内的所有命令。
WATCH:监视一个(或多个) key ，如果在事务执行之前这个(或这些) key 被其他命令所改动，那么事务将被打断。