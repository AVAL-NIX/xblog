---
submitToken: C6D31AC06ED92CEBDDCA711BD06FBA6DFAC2A0D2B28F809FB7E07596AB67E0EA
title: 那些可以作为gc root ?
channel: topic
labels: jvm
---

## gc root 特征

就是只会引用其他的对象， 而不会被其他对象引用


## 可以作为的

- 栈中的本地变量
- 方法区的静态变量
- 正在运行的线程

