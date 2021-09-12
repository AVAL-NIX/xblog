---
submitToken: D42E23BBBB046C52F49062E995BB1C25831739B145A819CF1E008DD1915B7D15
title: 说一说自己对于 synchronized 关键字的了解
channel: topic
labels: juc
---


synchronized 关键字解决的是多个线程之间访问资源的同步性，synchronized关键字可以保证被它修饰的方法或者代码块在任意时刻只能有一个线程执行。

## 使用地方

```
修饰实例方法
对象锁

获取一个对象的markword标记头，里面有偏向锁标记位，然后进行锁升级->轻量级锁->重量级锁moniter ,JVM 可以编译出看的到monitorenter monitorexit
```
```
修饰静态方法
类锁

通过方法flags 标记位来确定是不是同步方法，如果是同步方法要先获取monitor对象，没有锁升级
```
```
修饰代码块
synchronized(this) 对象锁
synchronized(A.class) 类锁
```
