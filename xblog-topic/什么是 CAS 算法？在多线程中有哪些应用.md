---
submitToken: BE4072EDE9C4D37948FCE806306764201D09383A6BBC61014DC08F51D015342E
title:  什么是 CAS 算法？在多线程中有哪些应用。
channel:  topic
labels: juc
---

> CAS，全称为 Compare and Swap，即比较-替换。假设有三个操作数：内存值 V、 旧的预期值 A、要修改的值 B，当且仅当预期值 A 和内存值 V 相同时，才会将内 存值修改为 B 并返回 true，否则什么都不做并返回 false。当然 CAS 一定要 volatile 变量配合，这样才能保证每次拿到的变量是主内存中最新的那个值，否则旧的预期 值 A 对某条线程来说，永远是一个不会变的值 A，只要某次 CAS 操作失败，永远 都不可能成功。
java.util.concurrent.atomic 包下面的 Atom类都有 CAS 算法的应用。