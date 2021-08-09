---
submitToken: 60B478B938C5A4D220F612E97FBDC103ADF0A35618859C0D099391D7493BB2EF
title: 第三次握手中，如果客户端的ACK未送达服务器，会怎样？
channel: topic
labels: http
---

**Server端：**

由于Server没有收到ACK确认，因此会重发之前的SYN+ACK（默认重发五次，之后自动关闭连接进入CLOSED状态），Client收到后会重新传ACK给Server。

**Client端，两种情况：**

1. 在Server进行超时重发的过程中，如果Client向服务器发送数据，数据头部的ACK是为1的，所以服务器收到数据之后会读取 ACK number，进入 establish 状态

2. 在Server进入CLOSED状态之后，如果Client向服务器发送数据，服务器会以RST包应答。

## 如果已经建立了连接，但客户端出现了故障怎么办？

    服务器每收到一次客户端的请求后都会重新复位一个计时器，时间通常是设置为2小时，若两小时还没有收到客户端的任何数据，服务器就会发送一个探测报文段，以后每隔75秒钟发送一次。若一连发送10个探测报文仍然没反应，服务器就认为客户端出了故障，接着就关闭连接。