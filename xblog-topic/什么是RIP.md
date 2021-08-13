---
submitToken: CF751C27B4DFA76105C4C196D25E6A5E76D8F000658A271678E8D92CBAD75AA0
title: 什么是RIP (Routing Information Protocol, 距离矢量路由协议)? 算法是什么？
channel: topic
labels: 网络协议
---

> 每个路由器维护一张表，记录该路由器到其它网络的”跳数“，路由器到与其直接连接的网络的跳1，
> 每多经过一个路由器跳数就加1；更新该表时和相邻路由器交换路由信息；路由器允许一个路径最多> 包含15个路由器，如果跳数为16，则不可达。交付数据报时优先选取距离最短的路径。
> （PS：RIP是应用层协议：https://www.zhihu.com/question/19645407）