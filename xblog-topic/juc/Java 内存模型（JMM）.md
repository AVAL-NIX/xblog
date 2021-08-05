---
submitToken: 0187AAFAF495B2F128789C9A6CD8BFE9A32377E288BB6FFDD55EDF01DCC184BB
title: Java 内存模型（JMM）?
channel: topic
labels: juc
---

JMM 内存模型是线程之间有个变量共享副本的的

![](https://image.avalon-zheng.xin/81cd7ac0-8ba9-4df7-a2b5-7329809057be "")

要解决这个问题，就需要把变量声明为**volatile**，这就指示 JVM，这个变量是共享且不稳定的，每次使用它都到主存中进行读取。
所以，**volatile 关键字 除了防止 JVM 的指令重排 ，还有一个重要的作用就是保证变量的可见性。**

![](https://image.avalon-zheng.xin/648ecf8c-3846-422c-b971-78270d114e99 "")