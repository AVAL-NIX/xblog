---
submitToken: DEB0588A98F219C17317DCA6D2C506F58D6CC06568F98F4905C486EC745C0721
title: Redis 给缓存数据设置过期时间有啥用？
channel: topic
labels: redis
---

- 过期时间除了有助于缓解内存的消耗

- 我们的业务场景就是需要某个数据只在某一时间段内存在，比如我们的短信验证码可能只在 1 分钟内有效，用户登录的 token 可能只在 1 天内有效。

