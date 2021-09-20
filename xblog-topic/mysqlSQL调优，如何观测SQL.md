---
submitToken: 654E3E81A56EDB461126FFD7DF93D81CCB694A4DCC94143EA3FD7E449363C876
title: mysql调优，如何进行SQL与监控?
channel: topic
labels: mysql
---

- 打开慢查询日记 。 一般超过1S的SQL需要优化

- 针对慢查询的SQL 。 使用explan进行分析

type : all 就很差
key : 看是否命中索引
row : 看扫描的表行。

