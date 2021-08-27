---
submitToken: 05F73172E515CBD6AF2B355C9B507C447DC5F076E96B981E4C8BEE2A5DE773ED
title: mybatis的优缺点?
channel: topic
labels: mybatis
---

- 什么是mybatis？
MyBatis 是支持定制 化 SQL、存储过程以及高级映射的优秀的持久层框架。MyBatis 避免了几乎所有的 JDBC 代码和手工设置参数以及抽取结果集。 MyBatis 使用简单的 XML 或注解来配置和映射基本体，将接口和 Java 的 POJOs(Plain Old Java Objects, 普通的 Java对象)映射成数据库中的记录。

- mybatis的原理：
MyBatis应用程序根据XML配置文件创建SqlSessionFactory，SqlSessionFactory在根据配置，配置来源于两个地方，一处是配置文件，一处是Java代码的注解，获取一个SqlSession。SqlSession包含了执行sql所需要的所有方法，可以通过SqlSession实例直接运行映射的sql语句，完成对数据的增删改查和事务提交等，用完之后关闭SqlSession。

- mybatis的优点：
```
1、简单易学
mybatis本身就很小且简单。没有任何第三方依赖，最简单安装只要两个jar文件+配置几个sql映射文件易于学习，易于使用，通过文档和源代码，可以比较完全的掌握它的设计思路和实现

2、解除sql与程序代码的耦合
通过提供DAL层，将业务逻辑和数据访问逻辑分离，使系统的设计更清晰，更易维护，更易单元测试。sql和代码的分离，提高了可维护性
3.提供映射标签，支持对象与数据库的orm字段关系映射
4.提供xml标签，支持编写动态sql。
```
- mybatis的缺点：
```
1、编写SQL语句时工作量很大，尤其是字段多、关联表多时，更是如此。
2、SQL语句依赖于数据库，导致数据库移植性差，不能更换数据库。

3、框架还是比较简陋，功能尚有缺失，虽然简化了数据绑定代码，但是整个底层数据库查询实际还是要自己写的，工作量也比较大，而且不太容易适应快速数据库修改。

4、二级缓存机制不佳

```
