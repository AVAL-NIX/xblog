---
submitToken: E7AAAC5A90177CE922C5F65F3EDCC7A3311916EEFCFB26CDCD3B1C79ADE73484
title: 说说你对AOP的理解,怎么实现一个AOP
channel: topic
labels: redis
---

> 1. 说出基本概念 2. 说出AOP特点 3. 说出实现原理 （代理说说）


## 基本概念

AOP的目的就是要实现"关注点代码"与"核心代码"分离，然后通过面向切面的方式，将关注点代码织入到核心代码中。

## 特点

- 1.2.2 切入点(Pointcut) （重要）
切入点指的就是在哪些类的哪些方法上织入切面(Where)。

在上面的例子中，我们的切入点就是UserServiceImpl类中的方法。

- 1.2.3 通知(Advice) （重要）
通知指的是在方法执行的什么时候（When：方法前/方法后/方法前后/方法异常/方法返回）做什么增强(What)。

@Before()：前置通知，目标方法执行之前通知。
@After()：后置通知，目标方法执行之后通知。
@Around()：环绕通知，目标方法执行前后通知。
@AfterReturning()：方法返回的时候通知。
@AfterThrowing()：方法异常的时候通知。

-  1.2.4 切面(Aspect)
切面 = 切入点+通知，指的就是在什么地方，什么时候，做了什么增强。

-  1.2.5 织入(Weaving)
织入指的就是把切面加入到目标对象中，并创建出目标对象对应的代理对象(由Spring完成）

## 原理

在Spring中AOP的实现主要是基于JDK动态代理和CGLib代理方式。
