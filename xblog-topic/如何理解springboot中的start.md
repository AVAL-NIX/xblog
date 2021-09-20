---
submitToken: B85DC21E8BCEED84BC601D4175D26C21E525E9F14E5ADEBA2378F9C3AC4BAAFB
title: 如何理解springboot中的start?
channel: topic
labels: spring
---

Starters可以理解为启动器，它包含了一系列可以集成到应用里面的依赖包，你可以一站式集成Spring及其他技术，而不需要到处找示例代码和依赖包。如你想使用Spring JPA访问数据库，只要加入springboot-starter-data-jpa启动器依赖就能使用了。Starters包含了许多项目中需要用到的依赖，它们能快速持续的运行，都是一系列得到支持的管理传递性依赖。

> 简单就是说pom文件帮我们集成好了springboot开发所需要的基本jar包。不用我们自己去一个个配置jar包了