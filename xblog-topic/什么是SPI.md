---
submitToken: 1063154A4D0F2E4D30C406CA367F336C053A8BB9C4570E38C162D6FB21BF7825
title: 什么是SPI
channel: topic
labels: base
---

SPI（Service Provider Interface），是 JDK 内置的⼀个服务发现机制，它使得接⼝和
具体实现完全解耦。我们只声明接⼝，具体的实现类在配置中选择。
具体的就是你定义了⼀个接⼝，然后在 META-INF/services ⽬录下放置⼀个与接⼝同名的⽂本⽂件，
⽂件的内容为接⼝的实现类，多个实现类⽤换⾏符分隔。
这样就通过配置来决定具体⽤哪个实现！
⽽ Dubbo SPI 还做了⼀些改进，篇幅有限留在之后再谈。