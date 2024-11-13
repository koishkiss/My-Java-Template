package kiss.depot.util;

import java.util.List;

/*
* 列表相关操作
* author: koishikiss
* launch: 2024/11/1
* last update: 2024/11/1
* */

public class ArrayUtil {

    private ArrayUtil() {}

    //将 {1,2,3} 转换为 (1,2,3) 利于sql拼接
    public static<T> String arrayToString(T[] array) {
        if (array.length == 0) throw new RuntimeException("数组为空!");
        StringBuilder arrayString = new StringBuilder();
        arrayString.append("(").append(array[0]);
        for (int i = 1; i < array.length; i++) {
            arrayString.append(",").append(array[i]);
        }
        arrayString.append(")");
        return arrayString.toString();
    }

    //将 [1,2,3] 转换为 (1,2,3) 利于sql拼接
    public static<T> String listToString(List<T> list) {
        if (list.isEmpty()) throw new RuntimeException("列表为空!");
        StringBuilder listString = new StringBuilder();
        listString.append("(").append(list.get(0));
        for (int i = 1; i < list.size(); i++) {
            listString.append(",").append(list.get(i));
        }
        listString.append(")");
        return listString.toString();
    }
}
