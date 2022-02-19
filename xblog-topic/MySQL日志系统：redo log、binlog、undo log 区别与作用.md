---
submitToken: 64A0DDF3AF44645848EBFD73C520D75FA873BEDA21A41DB769F001F61E50E092
title: MySQL日志系统：redo log、binlog、undo log 区别与作用
channel: topic
labels: mysql
---



## redo log和binlog区别

- redo log是属于innoDB层面，binlog属于MySQL Server层面的，这样在数据库用别的存储引擎时可以达到一致性的要求。

- redo log是物理日志，记录该数据页更新的内容；binlog是逻辑日志，记录的是这个更新语句的原始逻辑

- redo log是循环写，日志空间大小固定；binlog是追加写，是指一份写到一定大小的时候会更换下一个文件，不会覆盖。

- binlog可以作为恢复数据使用，主从复制搭建，redo log作为异常宕机或者介质故障后的数据恢复使用。

## 一、重做日志（redo log）

redo log的写入分为两个阶段（prepare和commit），这个称作两阶段提交，保证了数据的正确性。下面我们从上图4个可能发生异常关闭的时间点来分析InnoDB如何在MySQL启动时做崩溃恢复。

Point A
如果服务器异常关闭发生在Point A以及之前的时间点，这个时候redolog 和 binlog都没有任何记录，事务还未提交，不会造成任何影响。

Point B
当服务器启动的时候发现redo log里处于prepare状态的记录，这个时候需要检查binlog是否完整包含此条redo log的更新内容（通过全局事务ID对应），发现binlog中还未包含此事务变更，则丢弃此次变更。

Point C
和Point B基本相同，只不过此时发现binlog中包含redo log的更新内容，此时事务会进行提交。

Point D
binlog中和数据库中均含有此事务的变更，没有任何影响。


作用：

确保事务的持久性。防止在发生故障的时间点，尚有脏页未写入磁盘，在重启mysql服务的时候，根据redo log进行重做，从而达到事务的持久性这一特性。

## 二、回滚日志（undo log）

数据库事务四大特性中有一个是原子性，具体来说就是 原子性是指对数据库的一系列操作，要么全部成功，要么全部失败，不可能出现部分成功的情况。

实际上，原子性底层就是通过undo log实现的。undo log主要记录了数据的逻辑变化，比如一条INSERT语句，对应一条DELETE的undo log，对于每个UPDATE语句，对应一条相反的UPDATE的undo log，这样在发生错误时，就能回滚到事务之前的数据状态

作用：

保存了事务发生之前的数据的一个版本，可以用于回滚，同时可以提供多版本并发控制下的读（MVCC），也即非锁定读



## 三、二进制日志（binlog）：

binlog用于记录数据库执行的写入性操作(不包括查询)信息，以二进制的形式保存在磁盘中。binlog是mysql的逻辑日志，并且由Server层进行记录，使用任何存储引擎的mysql数据库都会记录binlog日志。

- 逻辑日志：可以简单理解为记录的就是sql语句。
- 物理日志：因为mysql数据最终是保存在数据页中的，物理日志记录的就是数据页变更。
binlog是通过追加的方式进行写入的，可以通过max_binlog_size参数设置每个binlog文件的大小，当文件大小达到给定值之后，会生成新的文件来保存日志。

binlog使用场景
在实际应用中，binlog的主要使用场景有两个，分别是主从复制和数据恢复。

- 主从复制：在Master端开启binlog，然后将binlog发送到各个Slave端，Slave端重放binlog从而达到主从数据一致。
- 数据恢复：通过使用mysqlbinlog工具来恢复数据。

### binlog刷盘时机
对于InnoDB存储引擎而言，只有在事务提交时才会记录biglog，此时记录还在内存中，那么biglog是什么时候刷到磁盘中的呢？mysql通过sync_binlog参数控制biglog的刷盘时机，取值范围是0-N：

0：不去强制要求，由系统自行判断何时写入磁盘；
1：每次commit的时候都要将binlog写入磁盘；
N：每N个事务，才会将binlog写入磁盘。

> 从上面可以看出，sync_binlog最安全的是设置是1，这也是MySQL 5.7.7之后版本的默认值。但是设置一个大一些的值可以提升数据库性能，因此实际情况下也可以将值适当调大，牺牲一定的一致性来获取更好的性能。

### binlog日志格式
binlog日志有三种格式，分别为STATMENT、ROW和MIXED。

在 MySQL 5.7.7之前，默认的格式是STATEMENT，MySQL 5.7.7之后，默认值是ROW。日志格式通过binlog-format指定。
- STATMENT

基于SQL语句的复制(statement-based replication, SBR)，每一条会修改数据的sql语句会记录到binlog中。

优点：不需要记录每一行的变化，减少了binlog日志量，节约了IO, 从而提高了性能；
缺点：在某些情况下会导致主从数据不一致，比如执行sysdate()、slepp()等。
- ROW

基于行的复制(row-based replication, RBR)，不记录每条sql语句的上下文信息，仅需记录哪条数据被修改了。

优点：不会出现某些特定情况下的存储过程、或function、或trigger的调用和触发无法被正确复制的问题；
缺点：会产生大量的日志，尤其是alter table的时候会让日志暴涨
- MIXED

基于STATMENT和ROW两种模式的混合复制(mixed-based replication, MBR)，一般的复制使用STATEMENT模式保存binlog，对于STATEMENT模式无法复制的操作使用ROW模式保存binlog


