---
submitToken: 0E5D9A9A8794FCF53F425FECF57D22DC81A669C32C242D9A02A4E6864446D159
title: netty百万连接调优
channel: topic
labels: redis
---

## 调整linux最大文件句柄（硬件方面）

查看文件句柄最大数
```
ulimit -n
```

ulimit命令是用来显示和修改当前用户进程的基础限制命令，-n选项用于引用或设置当前的文件句柄数量的限制值，Linux系统的默认值为1024。

/etc/rc.local
```
ulimit -SHn 1000000
```

- 要彻底解开需要修改如下配置（ES也需要）

/etc/security/limits.conf

```
* soft nofile 1000000
* hard nofile 1000000
```

- 突破全局文件句柄(cat /proc/sys/fs/file-max)

在/etc/sysctl.conf文件末尾加上下面的内容。
```
# vim /etc/sysctl.conf
fs.file-max = 1000000
```

