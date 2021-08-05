---
submitToken: 1A7E83CA892107C066C5B90895C48F3BE3F593BAF3A258D834F365CEF55AE4F6
title: JUC 包中的原子类是哪 4 类?
channel: topic
labels: juc
---

- 基本类型

使用原子的方式更新基本类型

```
AtomicInteger：整形原子类
AtomicLong：长整型原子类
AtomicBoolean：布尔型原子类
```

- 数组类型

使用原子的方式更新数组里的某个元素

```
AtomicIntegerArray：整形数组原子类
AtomicLongArray：长整形数组原子类
AtomicReferenceArray：引用类型数组原子类
```

- 引用类型

```
AtomicReference：引用类型原子类
AtomicStampedReference：原子更新带有版本号的引用类型。该类将整数值与引用关联起来，可用于解决原子的更新数据和数据的版本号，可以解决使用 CAS 进行原子更新时可能出现的 ABA 问题。
AtomicMarkableReference ：原子更新带有标记位的引用类型

```
对象的属性修改类型
```
AtomicIntegerFieldUpdater：原子更新整形字段的更新器
AtomicLongFieldUpdater：原子更新长整形字段的更新器
AtomicReferenceFieldUpdater：原子更新引用类型字段的更新器
```