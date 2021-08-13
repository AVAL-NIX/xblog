---
submitToken: 29327BAC6070031736D3A8748129ED52F76D07024003CDCD37A051EF0BDA9E40
title: mysql主从复制原理？
channel: topic
labels: mysql
---


Master开启bin-log功能，服务器配置二进制日志（binlog日志），只保留update等的数据。

需要开启三个线程，Master：I/O线程；Slave：I/O线程，SQL线程。

从数据库会请求主数据库的binlog日志，将bin-log日志内容写入到relay-log中继日志，创建一个master.info文件，然后按日志执行

Slave已经开启了sql线程，由sql线程实时监测relay-log日志内容是否有更新，如果有更新，则解析文件中的sql语句，并在Slave数据库中执行相同的操作语句