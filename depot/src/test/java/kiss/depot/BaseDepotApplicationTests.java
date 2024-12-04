package kiss.depot;

import kiss.depot.util.JwtUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BaseDepotApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	void testJwt() {
		String token = JwtUtil.generate("123","otherInfo");

		System.out.println(token);


		JwtUtil.CLAIMS claims = JwtUtil.getClaims(token);

		if (claims == null) {
			System.out.println("解析出null");
		} else {
			System.out.println(claims.uid);
			System.out.println(claims.otherInfo);
		}
	}

}
