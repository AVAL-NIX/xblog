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
```
```
修饰静态方法
类锁
```
```
修饰代码块
synchronized(this) 对象锁
synchronized(A.class) 类锁
```