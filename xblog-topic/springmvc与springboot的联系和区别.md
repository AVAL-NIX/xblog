---
submitToken: 26BD15ADD8BD0052A66CC84ECDB495C92DC7965D391B093486A4D52D78A83E14
title: springmvc与springboot的联系和区别
channel: topic
labels: juc
---

联系：

Spring最初利用工厂模式（DI)和代理模式解耦应用组件，为了解耦开发了springmvc；而实际开发过程中，经常会使用到注解，程序的样板很多，于是开发了starter，这套就是springboot。

区别：

1.springboot是约定大于配置，可以简化spring的配置流程；springmvc是基于servlet的mvc框架，个人感觉少了model中的映射。

2.以前web应用要使用到tomat服务器启动，而springboot内置服务器容器，通过@SpringBootApplication中注解类中main函数启动即可。例如：