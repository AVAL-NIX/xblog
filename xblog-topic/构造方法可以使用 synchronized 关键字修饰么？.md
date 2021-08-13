---
submitToken: 1097C3B954928162409AD55897901C45889F867B824101B49D467EA90A0518D1
title: 构造方法可以使用 synchronized 关键字修饰么？
channel: topic
labels: juc
---


先说结论：构造方法不能使用 synchronized 关键字修饰。
构造方法本身就属于线程安全的，不存在同步的构造方法一说。

