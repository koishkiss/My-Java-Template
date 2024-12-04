package kiss.depot.util;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecureDigestAlgorithm;
import kiss.depot.model.constant.STATIC;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.LinkedHashMap;

/*
* JWT加解密工具类
* 对jwt实现一定封装，包装的数据更改为一个类，方便存取
* author: koishikiss
* launch: 2024/11/1
* last update: 2024/12/5
* */

public class JwtUtil {

    //jwt密钥解析
    public static final SecretKey KEY = Keys.hmacShaKeyFor(STATIC.VALUE.secret.getBytes());

    //jwt加密方式
    public static final SecureDigestAlgorithm<SecretKey,SecretKey> ALGORITHM = Jwts.SIG.HS256;

    //设定claim使用的键
    public static final String CLAIM_KEY = "claims";

    //生成token，可以包裹更多东西，这里包装一个obj
    public static String generate(String uid, String otherInfo) {
        return Jwts.builder()
                .header().add("type","JWT")
                .and()
                .claim(CLAIM_KEY, new CLAIMS(uid, otherInfo))
                .expiration(new Date(System.currentTimeMillis() + STATIC.VALUE.expire))
                .signWith(KEY,ALGORITHM)
                .compact();
    }

    //解析token，得到包装的obj
    @SuppressWarnings("unchecked")
    public static CLAIMS getClaims(String token){
        try {
            return new CLAIMS((LinkedHashMap<String, Object>) Jwts.parser()
                    .verifyWith(KEY)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .get(CLAIM_KEY));
        } catch (JwtException | IllegalArgumentException e) {
            return null;
        }
    }

    //使用内部类来完成固定化的jwt操作流程
    public static class CLAIMS {
        public String uid;
        public String otherInfo;

        public CLAIMS(String uid, String otherInfo) {
            this.uid = uid;
            this.otherInfo = otherInfo;
        }

        public CLAIMS(LinkedHashMap<String, Object> linkedHashMap) {
            uid = (String) linkedHashMap.get("uid");
            otherInfo = (String) linkedHashMap.get("otherInfo");
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
