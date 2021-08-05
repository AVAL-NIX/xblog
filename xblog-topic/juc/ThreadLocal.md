---
submitToken: CD2FD8C10040ADA540BE548786E2CD32AD7B4DCF59C7FC9F08ECFD9713106FB4
title: 简单介绍一下TreadLoacal
channel: topic
labels: juc
---

就是让每个线程拥有自己的变量副本, 避免多线程问题

### hash冲突怎么办？

底层是数组结构。 不是数组+链表， 发生冲突会从冲突后面找null的位置。 如果前面有key相等的话， 就更新 。

