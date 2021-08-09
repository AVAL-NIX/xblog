---
submitToken: FD45ACC0009EE1B82EF5CADFE3641EFC3E7DAA16B7B37E8CF764A93721A8DC24
title: TCP如何最大利用带宽？
channel: topic
labels: http
---

## TCP速率受到三个因素影响

- **窗口**：即滑动窗口大小，见[TCP如何实现流量控制？](https://github.com/AVAL-NIX/Waking-Up/blob/master/Computer%20Network.md#TCP%E5%A6%82%E4%BD%95%E5%AE%9E%E7%8E%B0%E6%B5%81%E9%87%8F%E6%8E%A7%E5%88%B6)

- **带宽**：这里带宽是指单位时间内从发送端到接收端所能通过的“最高数据率”，是一种硬件限制。TCP发送端和接收端的数据传输数不可能超过两点间的带宽限制。发送端和接收端之间带宽取所通过线路的带宽最小值（如通过互联网连接）。

- **RTT**：即Round Trip Time，表示从发送端到接收端的一去一回需要的时间，TCP在数据传输过程中会对RTT进行采样（即对发送的数据包及其ACK的时间差进行测量，并根据测量值更新RTT值），TCP根据得到的RTT值更新RTO值，即Retransmission TimeOut，就是重传间隔，发送端对每个发出的数据包进行计时，如果在RTO时间内没有收到所发出的数据包的对应ACK，则任务数据包丢失，将重传数据。一般RTO值都比采样得到的RTT值要大。

## 带宽时延乘积

![](https://image.avalon-zheng.xin/e1d61f55-e6ed-47d5-8979-58c72649b071 "")
