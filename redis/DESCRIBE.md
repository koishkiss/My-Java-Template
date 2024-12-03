### 简介

预计在这里写一个使用redis的模版。

顺便重新学习一遍redis。

---

### 描述

项目结构：

```shell
* main
  ├┈ config  # 配置类包
  │   └┈ RedisConfig  # 配置redis
  │
  ├┈ model  # 数据模型包
  │   └┈ enums  # 枚举类包
  │       └┈ RedisKey  # redis键前缀枚举
  │
  └┈ util  # 工具类包
      ├┈ ObjectUtil  # 对象操作工具类
      └┈ RedisUtil  # redis操作提供工具
  
* test
  └┈ RdisTemplateApplicationTest  # redis基本操作相关测试
```

redis数据结构：

```shell
库index：1
```

---

### 日志

#### *2024/12/2前*

```text
完成了redisUtil对redisTemplate部分操作的封装与测试，
接下来预计将在库中添加更多实例项目。
```

#### *2024/12/3*

```text
大量减少了ObjectUtil的使用。
有时候不得不承认自己写一个序列化反序列化过程根本行不通。
```

