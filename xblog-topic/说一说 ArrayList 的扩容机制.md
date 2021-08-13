---
submitToken: F7B62EE5980345A5A59C303C6DE0D9568401F0B3046AA0AA9B2B58D8A89282BA
title: 说一说 ArrayList 的扩容机制吧
channel: topic
labels: javaGuide
---

## ArrayList: 
> 底层是数组队列，相当于动态数组。与 Java 中的数组相比，它的容量能动态增长。在添加大量元素前，应用程序可以使用ensureCapacity操作来增加 ArrayList 实例的容量。这可以减少递增式再分配的数量。

ArrayList继承于 AbstractList ，实现了 List, RandomAccess, Cloneable, java.io.Serializable 这些接口。

```
public class ArrayList<E> extends AbstractList<E>
        implements List<E>, RandomAccess, Cloneable, java.io.Serializable{

  }
```

- **RandomAccess** 是一个标志接口，表明实现这个这个接口的 List 集合是支持快速随机访问的。在 ArrayList 中，我们即可以通过元素的序号快速获取元素对象，这就是快速随机访问。

- **ArrayList** 实现了 Cloneable 接口 ，即覆盖了函数clone()，能被克隆。

- **ArrayList** 实现了 java.io.Serializable接口，这意味着ArrayList支持序列化，能通过序列化去传输。

具体详情参考: [点我](https://snailclimb.gitee.io/javaguide/#/docs/java/collection/ArrayList%E6%BA%90%E7%A0%81+%E6%89%A9%E5%AE%B9%E6%9C%BA%E5%88%B6%E5%88%86%E6%9E%90)