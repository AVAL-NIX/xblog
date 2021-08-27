---
submitToken: CEFEA4C61B098A4479A950032591EBDBA71933EBC867B514400246922DD3AE19
title: spring事务什么时候会失效?
channel: topic
labels: spring
---

## 总结了8种情况

-  数据库引擎不支持事务

- 没有被 Spring 管理

-  方法不是 public 的

-  自身调用问题

> this.xxxx()方法这样没有走代理是不行的

- update方法上面没有加 @Transactional 注解

- 数据源没有配置事务管理器

-  异常被主动捕捉

- 默认捕捉的是RuntimeException

> @Transactional(rollbackFor = Exception.class) 就是要捕捉Exception异常