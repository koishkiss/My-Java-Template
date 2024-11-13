package kiss.depot.util;

import org.mindrot.jbcrypt.BCrypt;

/*
* BCrypt简单封装
* author: koishikiss
* launch: 2024/11/1
* last update: 2024/11/3
* */

public class BCryptUtil {

    private BCryptUtil() {}

    //加密密码
    public static String encrypt(String plainText) {
        return BCrypt.hashpw(plainText, BCrypt.gensalt());
    }

    //解密密码
    @Deprecated
    public static String decrypt(String cipherText) {
        throw new UnsupportedOperationException("BCrypt does not support decryption.");
    }

    //验证密码和密文对应性
    public static boolean validate(String plainText, String cipherText) {
        return BCrypt.checkpw(plainText, cipherText);
    }
}


/*
* using dependency:
<!--BCrypt加密算法-->
<dependency>
    <groupId>org.mindrot</groupId>
    <artifactId>jbcrypt</artifactId>
    <version>0.4</version>
</dependency>
* */