---
submitToken: 0BD12CC2036393E85159438C0662C1342FF39DD825D1F604F1814FC528D1FAFF
title: mybatis如何开发一个插件?
channel: topic
labels: mybatis
---

> 简单的说，mybatis插件就是对ParameterHandler、ResultSetHandler、StatementHandler、Executor这四个接口上的方法进行拦截，利用JDK动态代理机制，为这些接口的实现类创建代理对象，在执行方法时，先去执行代理对象的方法，从而执行自己编写的拦截逻辑，所以真正要用好mybatis插件，主要还是要熟悉这四个接口的方法以及这些方法上的参数的含义；

> 另外，如果配置了多个拦截器的话，会出现层层代理的情况，即代理对象代理了另外一个代理对象，形成一个代理链条，执行的时候，也是层层执行；

> 关于mybatis插件涉及到的设计模式和软件思想如下：

设计模式：代理模式、责任链模式；

软件思想：AOP编程思想，降低模块间的耦合度，使业务模块更加独立；

一些注意事项：

> 不要定义过多的插件，代理嵌套过多，执行方法的时候，比较耗性能；

> 拦截器实现类的intercept方法里最后不要忘了执行invocation.proceed()方法，否则多个拦截器情况下，执行链条会断掉；