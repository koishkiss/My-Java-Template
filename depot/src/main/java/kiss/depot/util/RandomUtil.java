package kiss.depot.util;

/*
* 随机案例生成工具类
* author: koishikiss
* launch: 2024/11/2
* last update: 2024/12/15
* */

import java.util.Random;
import java.util.Set;
import java.util.UUID;

public class RandomUtil {

    private RandomUtil() {}

    //静态random类
    private static final Random random = new Random();

    //生成所用的字符集
    private static final char[] characterSet = "0123456789".toCharArray();

    //按一定长度生成一段随机code
    public static String generateRandomCode(int length) {
        StringBuilder randomString = new StringBuilder();

        //随机组合
        for (int i = 0; i < length; i++) {
            randomString.append(characterSet[random.nextInt(characterSet.length)]);
        }

        return randomString.toString();
    }

    //按一定长度生成一段随机code并排除一个不可用code
    public static String generateRandomCode(int length, String except) {
        String result = generateRandomCode(length);
        while (result.equals(except)) {
            result = generateRandomCode(length);
        }
        return result;
    }

    //按一定长度生成一段随机code并排除一些不可用code
    public static String generateRandomCode(int length, Set<String> excepts) {
        String result = generateRandomCode(length);
        while (excepts.contains(result)) {
            result = generateRandomCode(length);
        }
        return result;
    }

    public static long generateRandomLongNum(long start, long end) {
        return random.nextLong(start,end+1);
    }

    public static String generateRandomUUID() {
        return UUID.randomUUID().toString();
    }
}
