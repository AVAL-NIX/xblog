---
submitToken: 6D2E040A4ED5668C6E85AD9218F9C506DAC5D4740C13DD4062CBC247B2FD5F08
title: spring 拦截分别继承什么类? 过滤器呢?? JDK动态代理又继承了什么类. ? cglib呢?
channel: topic
labels: juc
---

## 拦截器
HandlerInterceptor ===== 然后在自定义类继承WebMvcConfigurerAdapter 重写 addInterceptors实现

## 过滤器

Filter

## JDK动态代理
InvocationHandler (重写invoke方法)
要有接口, 因为底层通过重写一个代理类来继承接口，来实现反射调用代理类

## cglib
MethodInterceptor (重写intercept)
没有接口用这个, 重写一个子类来实现反射调用代理类


## 主要体现在如下的两个指标中：

- CGLib所创建的动态代理对象在实际运⾏时候的性能要⽐JDK动态代理⾼不少，有研究表明，⼤概要⾼10倍；
- 但是CGLib在创建对象的时候所花费的时间却⽐JDK动态代理要多很多，有研究表明，⼤概有8倍的差距；
- 因此，对于singleton的代理对象或者具有实例池的代理，因为⽆需频繁的创建代理对象，所以⽐较适合采⽤CGLib动态代理，反正，则⽐较适⽤JDK动态代理
