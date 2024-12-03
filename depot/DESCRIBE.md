### 简介

主要存放后端开发时的各种工具收集。

因为放在一个spring boot项目中，因此可以对这些工具进行一些测试，

保留的测试代码可供后续参考。

此后遇到的新东西都可以试着往这里写。

---

### 描述

项目结构：

```shell
* main
  ├┈ config  # 配置类包
  │   ├┈ exceptionConfig  # 配置错误处理架构
  │   ├┈ webMvcConfig  # 配置Mvc操作
  │   ├┈ DateFormatConfig  # 配置接口返回的时间格式
  │   └┈ OriginConfig  # 配置跨域
  │
  ├┈ controller  # controller包
  │   └┈ TestPageController  # 测试分页查询
  │       ├┈ allPageSearch  # 全页码分页查询示例
  │       ├┈ singlePageSearch  # 无页码分页查询示例
  │       └┈ morePageSearch  # 多页码分页查询示例
  │
  ├┈ service  # service包，与controller包一一对应
  │
  ├┈ mapper  # mapper包
  │
  ├┈ model  # 数据模型包
  │   ├┈ bo  # 业务逻辑对象包
  │   │   └┈ pageBo  # 分页查询
  │   │       ├┈ AllPageSearchBo  # 全页码分页查询
  │   │       ├┈ SinglePageSearchBo  # 无页码分页查询
  │   │       └┈ MorePageSearchBo  # 多页码分页查询
  │   ├┈ constant  # 静态提供包
  │   │   ├┈ MAPPER  # mapper静态注入
  │   │   └┈ STATIC  # 静态数据存放
  │   ├┈ enums  # 枚举类包
  │   │   └┈ CommonErr  # 常用错误返回枚举
  │   └┈ vo  # 视图对象包
  │       ├┈ pageVo  # 分页查询结果返回vo对象
  │       └┈ response  # 最终返回响应包装类Response
  │
  └┈ util  # 工具类包
      ├┈ ArrayUtil  # 处理列表工具
      ├┈ BCryptUtil  # 封装BCrypt操作
      ├┈ CheckFormatUtil  # 检查数据格式工具
      ├┈ ConditionalSqlMaker  # sql拼接工具（已废弃）
      ├┈ DateUtil  # 时间工具（已废弃）
      ├┈ FileUtil  # 文件存储工具
      ├┈ HtmlHandleUtil  # html处理工具
      ├┈ JwtUtil  # 封装jwt操作
      ├┈ ObjectUtil  # 操作对象工具
      ├┈ PageUtil  # 分页工具（已废弃）
      ├┈ RandomUtil  # 随机生成工具
      └┈ TextUtil  # 文本操作工具
```

mysql数据库结构：

```shell
depot_test  # 库名称
  ├┈ depot_post  # 帖子表
  └┈ depot_user  # 用户表


==================== depot_post表结构 ====================

CREATE TABLE `depot_post` (
  `id` int NOT NULL AUTO_INCREMENT,
  `content` varchar(100) NOT NULL COMMENT '随机字符串',
  `post_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '字段产生时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


==================== depot_user表结构 ====================

CREATE TABLE `depot_user` (
  `uid` int NOT NULL AUTO_INCREMENT,
  `sid` varchar(100) NOT NULL,
  `name` varchar(100) NOT NULL,
  `password` varchar(100) NOT NULL,
  `nickname` varchar(100) NOT NULL,
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
```

---

### 日志

#### *2024/12/2 前*

```text
补充之写完代码但未进行的说明。
大概是把之前写过的各个项目中整出来的util包给整了进来，以及架构模式、config类。
给有些util包打上了已过期标识。如果直接扔掉这些util的话也太可惜了吧。
此外，对接口返回数据结构也进一步规范了，
并且实现了三种分页查询的类，并在这里第二次尝试了函数式编程的思想。
```

