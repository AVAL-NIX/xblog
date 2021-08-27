---
submitToken: 95A97E8C840B127990ABC7FC0D335AD726874EDAF1001BACBF666DED72AC31C1
title: spring事务的原理以及传播方式?
channel: topic
labels: spring
---

## 原理

1.配置文件开启注解驱动，在相关的类和方法上通过注解@Transactional标识。
2.spring 在启动的时候会去解析生成相关的bean，这时候会查看拥有相关注解的类和方法，并且为这些类和方法生成代理，并根据@Transaction的相关参数进行相关配置注入，这样就在代理中为我们把相关的事务处理掉了（开启正常提交事务，异常回滚事务）。
3.真正的数据库层的事务提交和回滚是通过bin log或者redo log实现的。

> session不共享是通过threadlocal实现的

## 传播方式

- PROPAGATION_REQUIRED：如果当前没有事务，就新建⼀个事务，如果已经存在⼀个事务中，加⼊到这个事务中。这是最常⻅的选择。
- PROPAGATION_SUPPORTS：⽀持当前事务，如果当前没有事务，就以⾮事务⽅式执⾏。
- PROPAGATION_MANDATORY：使⽤当前的事务，如果当前没有事务，就抛出异常。
- PROPAGATION_REQUIRES_NEW：新建事务，如果当前存在事务，把当前事务挂起。
- PROPAGATION_NOT_SUPPORTED：以⾮事务⽅式执⾏操作，如果当前存在事务，就把当前事务
挂起。
- PROPAGATION_NEVER：以⾮事务⽅式执⾏，如果当前存在事务，则抛出异常。
- PROPAGATION_NESTED：如果当前存在事务，则在嵌套事务内执⾏。如果当前没有事务，则执⾏与PROPAGATION_REQUIRED类似的操作。

