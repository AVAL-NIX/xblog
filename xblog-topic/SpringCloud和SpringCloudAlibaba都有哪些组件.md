---
submitToken: 9C06C86BD72E1D8195D752AF0A7883BD1D8C7318001A374E80FAEB2BD62C80DD
title: SpringCloud和SpringCloudAlibaba都有哪些组件
channel: topic
labels: redis
---
# SpringCloudAlibaba

## Nacos (配置中心与服务注册与发现)

Nacos实现了服务的配置中心与服务注册发现的功能，Nacos可以通过可视化的配置降低相关的学习与维护成本，实现动态的配置管理与分环境的配置中心控制。 同时Nacos提供了基于http/RCP的服务注册与发现功能。

## Sentinel (分布式流控)

Sentinel是面向分布式微服务架构的轻量级高可用的流控组件，以流量作为切入点，从流量控制，熔断降级，系统负载保护等维度帮助用户保证服务的稳定性。常用与实现限流、熔断降级等策略。

## RocketMQ (消息队列)

RocketMQ基于Java的高性能、高吞吐量的消息队列，在SpringCloud Alibaba生态用于实现消息驱动的业务开发，常见的消息队列有Kafka、RocketMQ、RabbitMQ等，相关的比较文档可以自行去翻阅。

## Seata (分布式事物)

既然是微服务的产品，那么肯定会用到分布式事物。Seata就是阿里巴巴开源的一个高性能分布式事物的解决方案。

# SpringCloud

![](https://image.avalon-zheng.xin/bccf259f-c892-4592-ab10-e4aa40983356 "")
![](https://image.avalon-zheng.xin/79c82dae-82cb-4fc2-8a28-b2bb29367f12 "")