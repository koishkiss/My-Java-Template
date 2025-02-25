### 简介

websocket使用模版。

也是个尝试。

---

### 描述

项目结构：

```shell
* main
  ├┈ annotation  # 自定义注解包
  │   └┈ BeforeLogin  # 标记无需登入就可使用的接口
  │
  ├┈ config  # 配置类包
  │   ├┈ exceptionConfig  # 配置错误处理架构
  │   ├┈ webMvcConfig  # 配置Mvc操作
  │   ├┈ websocketConfig  # 配置websocket
  │   │   ├┈ WebsocketConfig  # websocket核心配置
  │   │   ├┈ WebsocketHandler  # websocket消息接收处理配置
  │   │   └┈ WebsocketInterceptor  # websocket握手过滤器
  │   └┈ RedisConfig  # 配置Redis
  │
  ├┈ controller  # controller包
  │   ├┈ AuthController  # 用户权限接口
  │   │   ├┈ register  # 注册
  │   │   ├┈ login  # 登入
  │   │   ├┈ logout  # 登出
  │   │   └┈ logoff  # 注销（未完成）
  │   ├┈ ChatRoomController  # 聊天室接口
  │   └┈ UserController  #  用户信息接口
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
      ├┈ BCryptUtil  # 封装BCrypt操作
      ├┈ JwtUtil  # 封装jwt操作
      ├┈ ObjectUtil  # 操作对象工具
      ├┈ RandomUtil  # 随机生成工具
      └┈ TextUtil  # 文本操作工具

```

mysql数据库结构：

```shell
ws_test  # 库名称
  ├┈ chat_room  # 聊天群表
  ├┈ private_chat_record  # 私聊记录表
  ├┈ room_join  # 用户加入的聊天群记录表
  └┈ user_auth  # 用户认证信息表


==================== chat_room表结构 ====================

CREATE TABLE `chat_room` (
  `room_id` bigint NOT NULL COMMENT '房间号id',
  `creator` bigint NOT NULL COMMENT '创建者id',
  `room_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '房间名',
  PRIMARY KEY (`room_id`),
  KEY `chat_room_room_name_IDX` (`room_name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='聊天室';


==================== private_chat_record表结构 ====================

CREATE TABLE `private_chat_record` (
  `chat_id` bigint NOT NULL COMMENT '记录编号',
  `user_from` bigint NOT NULL COMMENT '发起用户',
  `user_to` bigint NOT NULL COMMENT '接收用户',
  `chat_content` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '内容',
  PRIMARY KEY (`chat_id`),
  KEY `private_chat_record_user_to_IDX` (`user_to`,`user_from`) USING BTREE,
  KEY `private_chat_record_user_from_IDX` (`user_from`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='私聊记录';


==================== room_join表结构 ====================

CREATE TABLE `room_join` (
  `uid` bigint NOT NULL COMMENT '用户id',
  `room_id` bigint NOT NULL COMMENT '房间id',
  PRIMARY KEY (`uid`,`room_id`),
  KEY `room_join_roomId_IDX` (`room_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='聊天室加入情况';


==================== user_auth表结构 ====================

CREATE TABLE `user_auth` (
  `uid` bigint NOT NULL COMMENT '用户id',
  `nickname` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户昵称',
  `password` varchar(100) NOT NULL COMMENT '用户密码',
  PRIMARY KEY (`uid`),
  KEY `user_auth_nickname_IDX` (`nickname`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户登入';
```

redis数据结构：

```shell
库index：2
  └┈ user  # 用户相关存储
      ├┈ session:uid  # 用户sessionId存储
      ├┈ info:uid  # 用户身份信息缓存
      ├┈ online:uid  # 用户在线状态标记
      └┈ message:uid  # 用户接收消息队列
```

---

### 日志

#### *2024/12/2*

```text
初始化了项目。
```

#### *2024/12/3*

```text
把基本的用户系统搞定了。包括注册、登入、登出、查看信息。当然依然存在很多问题。
中间遇到了lettuce无法连接到redis的问题，最后发现是父pom指定的版本低了导致不兼容的问题，值得注意。
此外，发现ObjectUtil中存在大量问题，之后考虑把纯手写的反射改成对objectMapper的封装。
```

#### *2024/12/4*

```text
改善了12月三号所指的问题。
现在的用户登入模式是：单点登入，登入后立即作废原先登入状态（如果有），使其它节点下线。
在需要长连接的场景下，这样做是合理的。
当然可以实现让手机电脑同时登入。以后可能会考虑加上这点。
不过话说回来，现在还没正式开始使用websocket呢。
```

#### *2024/12/5*

```text
简单地写了ws握手流程和长连接要实现的功能相关的注释，
接下来就要正式实现功能了。
不过好像还有更多的地方要用到http请求...比如，创建聊天室。
```

#### *2024/12/8*

```text
初步结构化了ws消息收发的流程
还在考虑是否应该为每个用户独立创建一个线程——也许有替代方法
```

#### *2024/12/9*

```text
结构化了路径方法映射方式，并写好了私聊、群聊的路径映射，改善了ws连接中发生错误的处理，改变为返回错误信息而不是直接断开连接。
改善了记录用户在线状态的方式。将这个数据从user:info中移出，方便管理和独立设置过期时间。
此外，我认为我找到了为能在一个线程内为多个用户提供消息推送服务的方法。
```

#### *2024/12/15*

```text
好几次更新后没时间写日志——前期架构把控不是很清晰，不好写——现在来补一补。

现在的基本设计思路还是给每个用户开一个消息队列来接收消息，给每个上线的用户分配一个线程用于将消息队列的消息推送给用户

想要搞好一个websocket+redis服务确实不是简单的事情，很考验设计能力，在设计中可能出现各种各样的问题。现在来复盘一下：

1.关于如何在用户下线时关闭线程防止内存泄漏。
因为是通过循环地阻塞读取消息队列，所以不通过外力是无法使线程自动停止的。
一开始考虑通过外力强制关闭线程，但这很有可能影响到线程中正在进行的任务，导致处理异常。
随后考虑到如果一个用户离线，那么websocket session也必然被释放，因此就通过检测websocket session是否存在作为结束线程的契机。
当然，如果线程还在被阻塞读取操作阻塞着，那么就无法检测websocket session是否存在，也无法结束线程，因此设置了10秒轮询一次的阻塞读取方式。
这样，只要能将websocket session释放，就能在10秒内结束了这个线程。

2.关于用户重复登入时如何下线另一个设备的用户。
考虑到服务器的可拓展性，我们不能直接关闭jvm中存储的websocket session——session可能不在同一台服务器上
因此将强制下线命令放进消息队列是一个选择。
但也要考虑到万一发生异常，该命令没有被正常接收，导致该命令被不应该下线的设备获取，就会导致错误下线。
通过检查机制，我将发生这个问题的概率降到了可以接受的程度。（？）
此外还有很多难以控制的问题。比如为了区分不同的登入，便将新sessionId设定为不可与上次重复、SESSION_MAP的键改为由uid+sessionId组成。

3.出现异常时正在处理消息队列，导致消息丢失。
因为redis的List的pop操作在读取数据时也会删除数据，因此处理消息时，消息已经不在redis中了。
因此出现异常后，消息没有正确返回给用户，就会发生丢失。（不过mysql里还有存储）
解决方法是，如果在处理消息的过程中发生异常，那么将消息塞回消息队列就行。当然考虑什么时候该放回去，什么时候不该，也是个问题。

...

现在项目的情况是，已经完成了一对一聊天的功能，而且可以开启分布式服务器用于处理长连接，实现了跨服务推送消息，虽然不完善。

快期末周了，后续看有时间改进。
```
