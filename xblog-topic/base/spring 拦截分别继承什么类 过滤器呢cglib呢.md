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