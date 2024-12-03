package kiss.depot.redis;

import kiss.depot.redis.model.enums.RedisKey;
import kiss.depot.redis.model.po.User;
import kiss.depot.redis.util.RedisUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@SpringBootTest
class RedisTemplateApplicationTests {

	/**
	 * 测试基本的键值对设置操作
	 */
	@Test
	void testForSet() {
		//设置键为a，值为b的键值对
		RedisUtil.S.set("a","b");

		//获取键为a的值
		String a = RedisUtil.S.get("a");

		//打印值
		System.out.println(a);
	}

	/**
	 * 测试将对象以json串形式存储
	 */
	@Test
	void testForSetObject() {
		//新建一个用户
		User user = new User("514", "koishi", "415");

		//预先打印一下用户信息
		System.out.println(user);

		//将user对象存入
		RedisUtil.S.setObject(RedisKey.USER_INFO.concat(user.getUid()), user);

		//将user对象取出
		User user1 = RedisUtil.S.getObject(RedisKey.USER_INFO.concat(user.getUid()), user.getClass());

		//打印取出的user对象
		System.out.println(user1);

	}

	/**
	 * 测试删除键及其返回值
	 */
	@Test
	void testForDelete() {
		//删除一下键c
		boolean deleted = RedisUtil.delete("c");

		//查看删除是否成功
		System.out.println(deleted);

		//预先设置好两个键值对
		RedisUtil.S.set("d","123");
		RedisUtil.S.set("e","123");

		//将c、d、e一并删除
		long deletes = RedisUtil.delete(List.of(new String[]{"c", "d", "e"}));

		//打印删除结果
		System.out.println(deletes);
	}

	/**
	 * 测试设置过期时间
	 */
	@Test
	void testForSetExpire() {
		//预先删除一下键c
		RedisUtil.delete("c");

		//给键c设置过期时间
		boolean c = RedisUtil.setExpire("c",100);

		//查看设置是否成功
		System.out.println(c);

		//查看此时键c的过期时间
		System.out.println(RedisUtil.getExpire("c"));


		//预先设置一下键值对b
		RedisUtil.S.set("b","111");

		//设置键的过期时间
		boolean b = RedisUtil.setExpire("b",100);

		if (b) {
			//输出键的过期时间
			System.out.println((RedisUtil.getExpire("b")));
		}

	}

	/**
	 * 测试将对象存入Hash
	 */
	@Test
	void setObjectTOHash() {
		//新建一个用户
		User user = new User("5145", "koishikiss", "415");

		//打印
		System.out.println(user);

		//存入
		RedisUtil.H.setObject(RedisKey.USER_INFO.concat(user.getUid()), user);

	}

	/**
	 * 测试将对象从Hash取出
	 */
	@Test
	void getObjectFromHash() {
		//获取用户
		User user1 = RedisUtil.H.getObject(RedisKey.USER_INFO.concat("112"), User.class);

		//打印
		System.out.println(user1);
	}



	/**
	 * 测试redis自增键实现自增
	 */

	//新建一线程池用于测试redis自增键
	private final ThreadPoolExecutor threadPool = new ThreadPoolExecutor(
			10,
			50,
			5,
			TimeUnit.SECONDS,
			new ArrayBlockingQueue<>(50));

	//新建操作计数器用于测试redis自增键添加用户所用速度
	CountDownLatch countDown = new CountDownLatch(50);

	@Test
	void testForIncrementByRedis() throws InterruptedException {
		long start = System.currentTimeMillis();

		//模拟50个用户的新建
		for (int i = 0; i < 50; i++) {
			//创建线程任务
			Runnable task = () -> {
				//获取自增键
				Long uid = RedisUtil.S.increment(RedisKey.INCR.concat("uid"));

				//打印获取的自增id
				System.out.println(uid);

				//uid不可为null
				if (uid == null) return;

				//根据自增键创建新用户
				User user = new User();
				user.setUid(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")) + "-" + uid);
				user.setNickname("u_" + uid);

				//保存用户数据到数据库（省略）

				//将用户缓存至redis
				RedisUtil.H.setObject(RedisKey.USER_INFO.concat(user.getUid()), user);

				//任务结束，操作计数器减一
				countDown.countDown();
            };

			//将任务添加到线程池
			threadPool.execute(task);
		}

		//等待全部操作完成
		countDown.await();

		long end = System.currentTimeMillis();

		//计算用时
		System.out.println("time: " + (end - start));
	}

}
