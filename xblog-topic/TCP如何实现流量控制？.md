---
submitToken: 4E797CF51935F5B9BC3F0353CD3B824E47B9C0EF87821C2F89957DC916EA7820
title: TCP如何实现流量控制？
channel: topic
labels: http
---

![](https://image.avalon-zheng.xin/78e6dc06-8bac-4bcf-904e-29b697bfe2e7 "")

使用滑动窗口协议实现流量控制。防止发送方发送速率太快，接收方缓存区不够导致溢出。接收方会维护一个接收窗口 receiver window（窗口大小单位是字节），接受窗口的大小是根据自己的资源情况动态调整的，在返回ACK时将接受窗口大小放在TCP报文中的窗口字段告知发送方。发送窗口的大小不能超过接受窗口的大小，只有当发送方发送并收到确认之后，才能将发送窗口右移。

发送窗口的上限为接受窗口和拥塞窗口中的较小值。接受窗口表明了接收方的接收能力，拥塞窗口表明了网络的传送能力。

![](https://image.avalon-zheng.xin/52023556-d897-4f5b-8d14-dff5477b6655 "")