---
submitToken: 3735D831BDBC0D7CF4A12C0ADF5D47255732BF30DA8576E743C0F31C8986DEDA
title: netty基本知识4
channel: topic
labels: netty
---


## Netty的解码器呢

什么是Netty的解码器呢？首先，它是一个InBound入站处理器，负责处理“入站数据”。其次，它能将上一站Inbound入站处理器传过来的输入（Input）数据进行解码或者格式转换，然后发送到下一站Inbound入站处理器

一个标准的解码器的职责为：将输入类型为ByteBuf的数据进行解码，输出一个一个的Java POJO对象。Netty内置了ByteToMessageDecoder解码器。Netty中的解码器都是Inbound入站处理器类型，都直接或者间接地实现了入站处理的超级接口ChannelInboundHandler。

### ByteToMessageDecoder解码器处理流程

ByteToMessageDecoder是一个非常重要的解码器基类，是一个抽象类，实现了解码处理的基础逻辑和流程。ByteToMessageDecoder继承自ChannelInboundHandlerAdapter适配器，是一个入站处理器，用于完成从ByteBuf到Java POJO对象的解码功能。

![](https://image.avalon-zheng.xin/cc0829c7-e5b0-475a-b8b1-3b058bdbdaf4 "")

> 解析完是个数组，逐个送往下一站 ， 所以下一个inboud接受的是一个对象不是一个list对象


### MessageToMessageDecoder解码器

前面的解码器都是将ByteBuf缓冲区中的二进制数据解码成Java的普通POJO对象，那么是否存在一些解码器可以将一种POJO对象解码成另外一种POJO对象呢？答案是存在。与前面不同的是，解码器需要继承一个新的Netty解码器基类MessageToMessageDecoder。在继承它的时候，需要明确的泛型实参，用于指定入站消息的Java POJO类型。


### 常用的内置Decoder

#### 固定长度数据包解码器——FixedLengthFrameDecoder

适用场景：每个接收到的数据包的长度都是固定的，例如100字节。在这种场景下，把FixedLengthFrameDecoder解码器加到流水线中，它就会把入站ByteBuf数据包拆分成一个个长度为100的数据包，然后发往下一个channelHandler入站处理器。


#### 行分割数据包解码器——LineBasedFrameDecoder

适用场景：每个ByteBuf数据包使用换行符（或者回车换行符）作为边界分隔符。在这种场景下，把LineBasedFrameDecoder解码器加到流水线中，Netty就会使用换行分隔符把ByteBuf数据包分割成一个一个完整的应用层ByteBuf数据包再发送到下一站。


#### 自定义长度数据包解码器——LengthFieldBasedFrameDecoder

这是一种基于灵活长度的解码器，在ByteBuf数据包中加了一个长度字段，保存了原始数据包的长度，解码时会按照原始数据包长度进行提取。此解码器在所有开箱即用解码器中是最为复杂的一种，后面会重点介绍。

### Encoder原理与实战

在Netty中，什么叫编码器？首先，编码器是一个Outbound出站处理器，负责处理“出站”数据；其次，编码器将上一站Outbound出站处理器传过来的输入（Input）数据进行编码或者格式转换，然后传递到下一站ChannelOutboundHandler出站处理器。

#### MessageToByteEncoder编码器

MessageToByteEncoder是一个非常重要的编码器基类，位于Netty的io.netty.handler.codec包中。MessageToByteEncoder的功能是将一个JavaPOJO对象编码成一个ByteBuf数据包。它是一个抽象类，仅仅实现了编码的基础流程，在编码过程中通过调用encode()抽象方法来完成。它的encode()编码方法是一个抽象方法，没有具体的编码逻辑实现，实现encode()抽象方法的工作需要子类去完成。

![](https://image.avalon-zheng.xin/6fd443c3-c379-4666-a3dd-3a757bf58470 "")


## ByteToMessageCodec编解码器

完成POJO到ByteBuf数据包的编解码器基类为ByteToMessageCodec，它是一个抽象类。从功能上说，继承ByteToMessageCodec就等同于继承了ByteToMessageDecoder和MessageToByteEncoder这两个基类。


![](https://image.avalon-zheng.xin/3dc2707d-7cfb-4dc9-9a54-db6f4e40dba1 "")

## 序列化与反序列化：JSON和Protobuf


理论上来说，对于对性能要求不是太高的服务器程序，可以选择JSON文本格式的序列化框架；对于性能要求比较高的服务器程序，应该选择传输效率更高的二进制序列化框架，建议是Protobuf。

### 详解粘包和拆包

什么是粘包和半包？先从数据包的发送和接收开始讲起。大家知道，Netty发送和读取数据的“场所”是ByteBuf缓冲区。对于发送端，每一次发送就是向通道写入一个ByteBuf，发送数据时先填好ByteBuf，然后通过通道发送出去。对于接收端，每一次读取就是通过业务处理器的入站方法从通道读到一个ByteBuf。读取数据的方法如下：

![](https://image.avalon-zheng.xin/758cb62c-033c-48e9-8784-19121950b4c6 "")

### 半包问题

接收到的完整的ByteBuf，这里称为“全包”。对应于第2种情况，多个发送端的输入ByteBuf“粘”在了一起，这里称为“粘包”。对应于第3种情况，一个发送过来的ByteBuf被“拆开”接收，接收端读取到一个破碎的包，这里称为“半包”。

在Netty中分包的方法主要有以下两种：

- （1）可以自定义解码器分包器：基于ByteToMessageDecoder或者ReplayingDecoder，定义自己的用户缓冲区分包器。

-  （2）使用Netty内置的解码器。例如，可以使用Netty内置的LengthFieldBasedFrameDecoder自定义长度数据包解码器对用户缓冲区ByteBuf进行正确的分包。

## 使用JSON协议通信


![](https://image.avalon-zheng.xin/6259fb77-9a72-4040-9f76-be40d5df0cf5 "")

![](https://image.avalon-zheng.xin/c783abc5-7e54-4d9a-8603-9212be2bc83e "")



![](https://image.avalon-zheng.xin/bf35502a-acbb-4370-be68-227b1ca855e4 "")


## 使用Protobuf协议通信

Protobuf（Protocol Buffer）是Google提出的一种数据交换格式，是一套类似JSON或者XML的数据传输格式和规范，用于不同应用或进程之间的通信。Protobuf具有以下特点：

（1）语言无关，平台无关Protobuf支持Java、C++、Python、JavaScript等多种语言，支持跨多个平台。

（2）高效比XML更小（3~10倍）、更快（20~100倍）、更为简单。

（3）扩展性、兼容性好


> 有3种方式， 1是二进制数组-》对象 ， 2。是inputstrem


![](https://image.avalon-zheng.xin/e67bea0a-0c19-40ec-aa67-43b6e55808d9 "")