---
submitToken: 46D033595A12DB4AD6EE14C6CA00587F98907F8AACC9BB4BED7CA6246BA7A6ED
title: Java 泛型了解么？什么是类型擦除？介绍一下常用的通配符？
channel: topic
labels: juc
---

Java 泛型（generics）是 JDK 5 中引入的一个新特性, 泛型提供了编译时类型安全检测机制，该机制允许程序员在编译时检测到非法的类型。泛型的本质是参数化类型，也就是说所操作的数据类型被指定为一个参数。
Java 的泛型是伪泛型，这是因为 Java 在编译期间，所有的泛型信息都会被擦掉，这也就是通常所说类型擦除 。
```
？ 表示不确定的 java 类型
T (type) 表示具体的一个 java 类型
K V (key value) 分别代表 java 键值中的 Key Value
E (element) 代表 Element
```