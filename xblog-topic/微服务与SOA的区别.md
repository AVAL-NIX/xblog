---
submitToken: 05D120E182D112F47019EA2DD88B310C72754176BFB47C1B426AF059FBDE4F1C
title: 微服务与SOA的区别
channel: topic
labels: redis
---


## 什么是单体应用
定义微服务前首要要定义单体应用。所谓单体应用是指所有的模块、业务都包含在一个应用中，比如用户管理模块、内容管理模块、推送模块全部放在一个应用中，发布时打成一个独立的包，放到Web容器中。

单体应用的优势是它部署起来非常方便，如果规模不大的话，开发测试也比较方便。

### 它的劣势也很明显：

- 复杂度高导致可维护性低： 随着需求的增加，项目规模开始膨胀，对象间的依赖关系变得复杂，编译以及测试度秒如年，调试起来也不再轻松，你喜欢的IDE也会变得越来越卡，随着时间的推移和人员的更迭，不同的代码风格交织在一起，可维护性变得越来越低。
- 发布频率低导致发布风险高： 虽然单体应用易于部署，但因为每次发布都需要重新部署整个应用，中断所有服务，为了减少中断的时间不得不降低发布频率，因此两次发布之间可能有大量的变更，导致发布风险比较高。
- 可靠性差： 所有模块共享一个进程，某个模块的bug可能导致整个应用瘫痪。
- 扩展灵活度低： 无法根据不同的业务需求选择不同的硬件规格进行部署，比如IO密集型的模块需要更大的内存，计算密集型的模块需要更快的CPU。
- 技术栈受限： 单体应用的语言、框架通常是统一的，如果希望不同模块用不同的语言、框架做是比较困难的，比如用户模块用Java，推送模块用Go。
## 什么是微服务
微服务和单体应用恰恰相反，把各个模块拆分成不同的项目，每个模块都只关注一个特定的业务功能，发布时每一个项目都是一个独立的包，运行在独立的进程上。微服务应该足够小，小到即使全部重写也不需要过多的时间。微服务化是SOA（Service-Oriented Architecture，面向服务的架构）的一种方法。

### 它解决了单体应用造成的一些问题：

- 易于开发，可维护性高： 一个服务只会关注一个特定的业务模块，代码比较少，可维护性就高。
- 发布风险低： 发布单个服务不需要重新发布整个应用。
- 技术栈不受限（异构）： 微服务之间通过轻量级的通信机制进行通信，比如RESTful API，因此不同项目可以随意选择合适的技术来实现。
- 按需伸缩： 可以针对特定的服务，结合业务特点分配不同的硬件规格。
### 当然，微服务不是完美的，在解决一旧问题的同时带来了新的问题：

- 运维要求高： 对于单体应用只要部署一个服务，微服务化后可能需要部署几十几百个服务.
- 分布式固有的复杂性： 开发人员需要考虑分布式事务、系统的容错性等，比如服务A的某个接口依赖服务B，通过RESTful API调用服务B的接口但发生了错误，需要提供重试等机制。
- 修改接口成本增加： 修改接口本就是一件繁琐的事，微服务化后成本更高，因为各个服务之间只通过轻量级通信机制访问，耦合度比较低，需要排查哪些服务的接口受到了影响，而在单体应用中通常只是方法间的依赖，如果修改了某个方法的签名，那么在编译时就会报错。
- 重复劳动： 当一个单体应用微服务化后，不同的服务之间会有重复代码产生，比如Gradle脚本、Maven依赖都非常类似，使用到的某些函数每个服务都造了一遍等，如果都是用同一种语言实现的还能用共享库解决，如果有多种语言那就难以避免重复劳动了。
## 微服务设计原则

- 单一职责原则： 每个服务应该只关注特定的某一类业务。
- 服务自治原则： 每个服务具备独立的业务能力、依赖和运行环境，与其他服务高度解耦，每个服务都应当可以独立运行，不应该依赖其他服务。
- 轻量级通信机制： 应该使用轻量级的、语言无关的、跨平台的协议，如REST、MQTT等。
## 什么样的服务是好服务
- 松耦合
如果修改一个服务的同时不需要修改另一个服务，那么就做到了松耦合。一个紧耦合的例子是，两个服务依赖了同一个持久化对象，如果其中一个服务要修改该持久化对象，那么另一个服务也很有可能需要修改，从而导致紧耦合。另外对于某些RPC（如Java RMI）而言，返回值对象的修改也可能引起该服务提供者的调用方服务的修改，否则可能导致某些序列化错误的问题。
- 高内聚
相关的行为应该聚集在一起，不相关的行为放在别处。一个低内聚的例子是，每种不同的服务都有自己关于推送功能的代码，那么一旦推送代码出现了问题，所有涉及到的服务都要修改。对于一种行为，最好只在一个地方修改。

## 什么是SOA

实际上SOA只是一种架构设计模式，而SOAP、REST、RPC就是根据这种设计模式构建出来的规范，其中SOAP通俗理解就是http+xml的形式，REST就是http+json的形式，RPC是基于socket的形式。上文提到的CXF就是典型的SOAP/REST框架，dubbo就是典型的RPC框架，而SpringCloud就是遵守REST规范的生态系统。