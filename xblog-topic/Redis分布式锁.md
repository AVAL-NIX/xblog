---
submitToken: 01FC25255EBA95EE4D8107AFF069D869413277E795AC9F335724767EEC08BCB9
title: Redis分布式锁有几种实现方式？
channel: topic
labels: redis
---

## 普通实现(SET实现)

说道Redis分布式锁大部分人都会想到：setnx+lua，或者知道set key value px milliseconds nx。后一种方式的核心实现命令如下：


```
- 获取锁（unique_value可以是UUID等）
SET resource_name unique_value NX PX 30000

- 释放锁（lua脚本中，一定要比较value，防止误解锁）
if redis.call("get",KEYS[1]) == ARGV[1] then
    return redis.call("del",KEYS[1])
else
    return 0
end
```

1. set命令要用set key value px milliseconds nx；

2. value要具有唯一性；

3. 释放锁时要验证value值，不能误解锁；

问题一：「锁过期释放了，业务还没执行完」。假设线程a获取锁成功，一直在执行临界区的代码。但是100s过去后，它还没执行完。但是，这时候锁已经过期了，此时线程b又请求过来。显然线程b就可以获得锁成功，也开始执行临界区的代码。那么问题就来了，临界区的业务代码都不是严格串行执行的啦。

问题二：「锁被别的线程误删」。假设线程a执行完后，去释放锁。但是它不知道当前的锁可能是线程b持有的（线程a去释放锁时，有可能过期时间已经到了，此时线程b进来占有了锁）。那线程a就把线程b的锁释放掉了，但是线程b临界区业务代码可能都还没执行完呢。

## RedissonLock （第3方实现同set实现）

## Redlock

1. 引入jar

```
Config config = new Config();
config.useSentinelServers().addSentinelAddress("127.0.0.1:6369","127.0.0.1:6379", "127.0.0.1:6389")
        .setMasterName("masterName")
        .setPassword("password").setDatabase(0);
RedissonClient redissonClient = Redisson.create(config);
// 还可以getFairLock(), getReadWriteLock()
RLock redLock = redissonClient.getLock("REDLOCK_KEY");
boolean isLock;
try {
    isLock = redLock.tryLock();
    // 500ms拿不到锁, 就认为获取锁失败。10000ms即10s是锁失效时间。
    isLock = redLock.tryLock(500, 10000, TimeUnit.MILLISECONDS);
    if (isLock) {
        //TODO if get lock success, do something;
    }
} catch (Exception e) {
} finally {
    // 无论如何, 最后都要解锁
    redLock.unlock();
}
```

![](https://image.avalon-zheng.xin/5141e36e-78f9-441a-ab5c-dab39da660b7 "")

红锁算法认为，只要 2N+1 个节点加锁成功，那么就认为获取了锁， 解锁时将所有实例解锁。


流程为：

- 顺序向五个节点请求加锁
- 根据一定的超时时间来推断是不是跳过该节点
- 三个节点加锁成功并且花费时间小于锁的有效期
- 认定加锁成功

 缺点，极端情况:

> 主节点没来的及把刚刚 Set 进来这条数据给从节点，就挂了。

