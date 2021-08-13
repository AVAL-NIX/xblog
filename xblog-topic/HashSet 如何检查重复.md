---
submitToken: 7A1E8E803C34ECB8907C5981C42560DDD77E6B36F278F231FF510C336E9393CD
title: HashSet 如何检查重复
channel: topic
labels: javaGuide
---

> 当你把对象加入HashSet时，HashSet 会先计算对象的hashcode值来判断对象加入的位置，同时也会与其他加入的对象的 hashcode 值作比较，如果没有相符的 hashcode，HashSet 会假设对象没有重复出现。但是如果发现有相同 hashcode 值的对象，这时会调用equals()方法来检查 hashcode 相等的对象是否真的相同。如果两者相同，HashSet 就不会让加入操作成功。

在openjdk8中，HashSet的add()方法只是简单的调用了HashMap的put()方法，并且判断了一下返回值以确保是否有重复元素。

直接看一下HashSet中的源码：
```
// Returns: true if this set did not already contain the specified element
// 返回值：当set中没有包含add的元素时返回真
public boolean add(E e) {
        return map.put(e, PRESENT)==null;
}
```
而在HashMap的putVal()方法中也能看到如下说明：
```
// Returns : previous value, or null if none
// 返回值：如果插入位置没有元素返回null，否则返回上一个元素
final V putVal(int hash, K key, V value, boolean onlyIfAbsent,
                   boolean evict) {
...
}
```
也就是说，在openjdk8中，实际上无论HashSet中是否已经存在了某元素，HashSet都会直接插入，只是会在add()方法的返回值处告诉我们插入前是否存在相同元素。

## **hashCode()与 equals() 的相关规定：**

1. 如果两个对象相等，则 hashcode 一定也是相同的

2. 两个对象相等,对两个 equals() 方法返回 true

3. 两个对象有相同的 hashcode 值，它们也不一定是相等的

4. 综上，equals() 方法被覆盖过，则 hashCode() 方法也必须被覆盖

5. hashCode()的默认行为是对堆上的对象产生独特值。如果没有重写 hashCode()，则该 class 的两个对象无论如何都不会相等（即使这两个对象指向相同的数据）。

## **==与 equals 的区别**

- 对于基本类型来说，== 比较的是值是否相等；

- 对于引用类型来说，== 比较的是两个引用是否指向同一个对象地址（两者在内存中存放的地址（堆内存地址）是否指向同一个地方）；

- 对于引用类型（包括包装类型）来说，equals 如果没有被重写，对比它们的地址是否相等；如果 equals()方法被重写（例如 String），则比较的是地址里的内容。