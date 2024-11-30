package kiss.depot.redis;

import kiss.depot.redis.model.enums.RedisKey;
import kiss.depot.redis.model.po.User;
import kiss.depot.redis.util.RedisUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

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

}
