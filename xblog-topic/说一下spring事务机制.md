---
submitToken: D7704E2A8974337F155DFCE42753800BD44E5C82C82C83DFD0C374A945F93E4D
title: 说一下spring事务机制
channel: topic
labels: redis
---

- 基于数据库+AOP
- 创建一个代理对象作为bean .
- 检查是否加了transaction 注解
- 如果加了会用事务管理器创建一个数据库session连接
- 禁用自动提交
- 没有报错就提交事务。
- 事务提交机制 根据注解内容， spring利用AOP进行是否要新开数据库session连接

