package kiss.depot.util;

import java.util.regex.Pattern;

/*
* 检查字符串格式工具类
* author: koishikiss
* launch: 2024/11/1
* last update: 2024/11/4
* */

public class CheckFormatUtil {

    private CheckFormatUtil() {}

    //------------------------------------------------SDU学号------------------------------------------------//

    //检查是否可能是按sid搜索
    public static boolean checkSidSearch(String searchText) {
        if (searchText.isEmpty() || searchText.length() > 12) return false;
        for (char c : searchText.toCharArray()) if (c < 48 || c >= 58) return false;
        return true;
    }

    //检查学号格式
    public static boolean checkSid(String sid) {
        if (sid.length() != 12) return false;
        for (char c : sid.toCharArray()) if (c < 48 || c >= 58) return false;
        return true;
    }

    //------------------------------------------------邮箱------------------------------------------------//

    //邮箱格式
    private static final Pattern emailFormat = Pattern.compile("^(\\w+([-.][A-Za-z0-9]+)*){3,18}@\\w+([-.][A-Za-z0-9]+)*\\.\\w+([-.][A-Za-z0-9]+)*$");

    //检查邮箱格式
    public static boolean checkEmail(String email) {
        return emailFormat.matcher(email).find();
    }

    //------------------------------------------------电话------------------------------------------------//

    //电话格式(大陆)
    private static final Pattern phoneFormat = Pattern.compile("^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\\d{8}$");

    //检查电话格式
    public static boolean checkPhone(String phone) {
        return phoneFormat.matcher(phone).find();
    }

    //------------------------------------------------密码------------------------------------------------//

    //严格模式密码检查，必须包含字母、数字、特殊符号，必须在8到20位之间，不能包含回车和空格
    private static final Pattern strictPasswordPattern = Pattern.compile("(?=^.{8,20}$)(?=.*\\d)(?=.*\\W)(?=.*[a-zA-Z])(?!.*[\\n\\s]).*$");

    //普通模式密码检查，必须在8到20位之间，不能包含回车和空格
    private static final Pattern passwordPattern = Pattern.compile("^(?!.*\\s)[a-z0-9A-Z\\W]{8,20}$");

    //检查密码格式，可选严格模式或简单模式
    public static boolean checkPassword(String password, boolean strict) {
        return (strict ? strictPasswordPattern : passwordPattern).matcher(password).find();
    }

    //检查密码格式，简单格式
    public static boolean checkPassword(String password) {
        return passwordPattern.matcher(password).find();
    }

    //------------------------------------------------身份证号------------------------------------------------//

    //身份证号码(新)每位号码的权重
    private static final int[] positionWeight = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};

    //身份证号码(新)校验码对应的校验值
    private static final byte[] checkCode = {'1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2'};

    //身份证号码(新)格式检查
    public boolean checkIdentityNumber(String idNumber) {
        if (idNumber.length() != 18) {
            return false;
        }

        char[] idArray = idNumber.toCharArray();

        // 计算校验值
        int sum = 0;
        for (int i = 0; i < 17; i++) {
            //保证前17位都是数字
            if (idArray[i] < 48 || idArray[i] >= 58) {
                return false;
            }
            sum += (idArray[i] - 48) * positionWeight[i];
        }

        //比较最后一位的校验码是否正确
        return idArray[17] == checkCode[sum % 11];
    }

    //------------------------------------------------用户输入安全检查------------------------------------------------//

    //可能含有sql注入的严格格式
    public static final Pattern sql = Pattern.compile("\\b(and|exec|insert|select|drop|grant|alter|delete|update|count|chr|mid|master|truncate|char|declare|or)\\b|([*;+'%])");

    //严格检查字符串中是否含有sql语句
    public static boolean checkHasSQL(String str) {
        return sql.matcher(str).find();
    }

}
