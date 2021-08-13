---
submitToken: D110B71E49356FFE9B53ABBA6147A5DC8FD1E412196A987B8BDA3617E23A2C46
title: 使用过 Redis 做异步队列么，你是怎么用的？有什么缺点？
channel: topic
labels: redis
---

一般使用 list 结构作为队列，rpush 生产消息，lpop 消费消息。当 lpop 没有消息的时候，要适当 sleep 一会再重试。 缺点： 在消费者下线的情况下，生产的消息会丢失，得使用专业的消息队列如 rabbitmq 等。 能不能生产一次消费多次呢？ 使用 pub/sub 主题订阅者模式，可以实现 1:N 的消息队列。