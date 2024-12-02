# Java-Template

---

### 简介

A personal repository for storing template of java 

个人代码模版仓库，欢迎补充。

---

### 项目结构

```shell
│* depot  # 工具类库
│   ├┈ allPageSearch  # 全页码分页查询示例
│   ├┈ singlePageSearch  # 无页码分页查询示例
│   └┈ morePageSearch  # 多页码分页查询示例
│* redis  # redis模版库
│* websocket  # websocket模版库
```

---

### 日志

#### *2024/12/2 前*

```text
创建了基础库depot，将以往的工具类和架构模式搬运到了这里面。
此外，还将之前一直停留在设想中的三种分页查询的业务逻辑封装在了BO包里，
虽然我完全无法确定我该如何命名以区分这些分页方式。（笑）
此后想完成这些操作的话，会简单很多罢。


创建了redis模版库，
并在里面简单封装了redisTemplate。
为了写这个库，大概是把redis从头学了遍，之前使用的太模糊了，受限于时间一直没怎么学。
不过之前学的时候确实没接触过几个项目，学的浅是自然的。
```

#### *2024/12/2*

```text
创建了websocket模版库。
坦白讲，websocket竟然在我人生中第二个项目就用上了，
还没玩透http就去使用websocket了啊，写出来的bug也是很多呢。（笑）

在这个项目结束后，也许我应该考虑使用spring cloud将项目联合起来
```
