package kiss.depot.websocket;

import kiss.depot.websocket.model.constant.STATIC;
import kiss.depot.websocket.model.enums.RedisKey;
import kiss.depot.websocket.util.RedisUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.TimeUnit;

@SpringBootTest
class WebsocketApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	void storedSession() {
		RedisUtil.S.VALUE.set(
				RedisKey.USER_SESSION.concat("123123"),
				String.valueOf(123123),
				STATIC.VALUE.jwt_expire,
				TimeUnit.MILLISECONDS
		);
	}

}
