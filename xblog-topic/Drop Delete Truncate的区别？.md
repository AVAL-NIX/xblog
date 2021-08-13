---
submitToken: BA5E3D2438FFD1DC20033CE9F7F69D0402098E7B214AFA5C57BD257F56CA9DD4
title: Drop/Delete/Truncate的区别？
channel: topic
labels: 数据库
---

- Delete用来删除表的全部或者部分数据，执行delete之后，用户需要提交之后才会执行，会触发表上的DELETE触发器（包含一个OLD的虚拟表，可以只读访问被删除的数据），DELETE之后表结构还在，删除很慢，一行一行地删，因为会记录日志，可以利用日志还原数据；

- Truncate删除表中的所有数据，这个操作不能回滚，也不会触发这个表上的触发器。操作比DELETE快很多（直接把表drop掉，再创建一个新表，删除的数据不能找回）。如果表中有自增（AUTO_INCREMENT）列，则重置为1；

- Drop命令从数据库中删除表，所有的数据行，索引和约束都会被删除；不能回滚，不会触发触发器；
