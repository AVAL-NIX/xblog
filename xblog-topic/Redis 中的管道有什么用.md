---
submitToken: 646D26DD946E380A2AC923B4288549AF9EC1F993AF63569438480B29BAED9C08
title: Redis 中的管道有什么用？
channel: topic
labels: redis
---

主要应用批处理

> 一次请求/响应服务器能实现处理新的请求即使旧的请求还未被响应，这样就可以将多个命令发送到服务 器，而不用等待回复，最后在一个步骤中读取该答复。 这就是管道（pipelining），是一种几十年来广泛使用的技术。例如许多 POP3 协议已经实现支持这个 功能，大大加快了从服务器下载新邮件的过程。