package kiss.depot.websocket.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecureDigestAlgorithm;
import kiss.depot.websocket.model.constant.STATIC;

import javax.crypto.SecretKey;
import java.util.Date;

/*
* JWT加解密工具类
* 对jwt实现一定封装，包装的数据暂时写死
* author: koishikiss
* launch: 2024/11/1
* last update: 2024/11/4
* */

public class JwtUtil {

    //jwt密钥解析
    private final SecretKey KEY = Keys.hmacShaKeyFor(STATIC.VALUE.jwt_secret.getBytes());

    //jwt加密方式
    private final SecureDigestAlgorithm<SecretKey,SecretKey> ALGORITHM = Jwts.SIG.HS256;

    //设定claim使用的键
    public static final String CLAIM_UID = "uid";
    public static final String CLAIM_SESSION_ID = "sessionId";

    public static final JwtUtil jwt = new JwtUtil();

    //生成token，可以包裹更多东西，这里包装一个obj
    public String generate(Long uid, String sessionId) {
        return Jwts.builder()
                .header().add("type","JWT")
                .and()
                .claim(CLAIM_UID, uid)
                .claim(CLAIM_SESSION_ID, sessionId)
                .expiration(new Date(System.currentTimeMillis() + STATIC.VALUE.jwt_expire))
                .signWith(KEY,ALGORITHM)
                .compact();
    }

    //解析token，得到包装的obj
    public Claims getClaim(String token){
        try {
            return Jwts.parser()
                    .verifyWith(KEY)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (JwtException | IllegalArgumentException e) {
            return  null;
        }
    }
}

/*
* using dependency:
<!--Java JSON Web Token , 生成Token-->
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt</artifactId>
    <version>0.12.3</version>
</dependency>
* */
