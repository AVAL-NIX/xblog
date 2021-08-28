---
submitToken: A426810F965CB99268DE3C4CAC36FDEC81F6DB3EA96AC06BEE373BA34FC89CBE
title: mysql执行计划怎么看
channel: topic
labels: mysql
---


explain + SQL

主要看2个字段

- type

system，const，eq_ref，ref，fulltext，ref_or_null，unique_subquery，index_subquery，range，index_merge，index，ALL，除了all之外，其他的type都可以使用到索引，除了index_merge之外，其他的type只可以用到一个索引

> type不要是all就好了


- rows

扫描的行。