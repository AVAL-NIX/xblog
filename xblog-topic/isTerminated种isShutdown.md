---
submitToken: 5133E90633A390A70F25B3459E1A6D2FB4876E8BCCD33FE16B04923024CC0458
title: isShutDown和isTerminated的区别
channel: topic
labels: juc
---

- isShutDown 当调用 shutdown() 方法后返回为 true。

- isTerminated 当调用 shutdown() 方法后，并且所有提交的任务完成后返回为 true