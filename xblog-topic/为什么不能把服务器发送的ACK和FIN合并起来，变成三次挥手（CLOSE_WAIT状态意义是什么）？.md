---
submitToken: 517ED2B8F85B094815984C7BFA0AEDEBAB7A4BB196184C82D93B909237000635
title: 为什么不能把服务器发送的ACK和FIN合并起来，变成三次挥手（CLOSE_WAIT状态意义是什么）？
channel: topic
labels: http
---

> 因为服务器收到客户端断开连接的请求时，可能还有一些数据没有发完，这时先回复ACK，表示接收到了断开连接的请求。等到数据发完之后再发FIN，断开服务器到客户端的数据传送。
