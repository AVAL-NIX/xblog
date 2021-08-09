---
submitToken: 898CF5EBC38336B7CD2B86B7BD66190E7CE5416F828E92EBF456BE5187A0709A
title: MySQL的两种存储引擎 InnoDB 和 MyISAM 的区别？
channel: topic
labels: 数据库
---

- InnoDB**支持事务**，可以进行Commit和Rollback；

- MyISAM 只支持表级锁，而 InnoDB 还**支持行级锁**，提高了并发操作的性能；

- InnoDB **支持外键**；

- MyISAM **崩溃**后发生损坏的概率比 InnoDB 高很多，而且**恢复的速度**也更慢；

- MyISAM 支持**压缩**表和空间数据索引，InnoDB需要更多的内存和存储；

- InnoDB 支持在线**热备份**

