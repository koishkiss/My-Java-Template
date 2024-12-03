package kiss.depot.websocket.util;

import org.springframework.data.util.CastUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
* 对象操作工具类
* 大量反射警告
* author: koishikiss
* launch: 2024/11/30
* last update: 2024/12/3
* */

public class ObjectUtil {

    //将实体类转换为Map
    public static <T> Map<String, T> convertToMap(Object o) {
        Map<String, T> m = new HashMap<>();

        try {
            Class<?> clazz = o.getClass();
            for (Field field : clazz.getDeclaredFields()) {
                field.setAccessible(true);
                m.put(field.getName(), CastUtils.cast(field.get(o)));
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        return m;
    }

    //将实体类转换为值为String的Map
    public static <T> Map<String, String> convertToStringMap(T o) {
        Map<String, String> m = new HashMap<>();

        try {
            Class<?> clazz = o.getClass();
            for (Field field : clazz.getDeclaredFields()) {
                field.setAccessible(true);
                m.put(field.getName(), String.valueOf(field.get(o)));
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        return m;
    }

    //从Map转换出实体类
    public static <T> T buildFromMap(Map<String, Object> m, Class<T> clazz) {
        try {
            T o = clazz.getDeclaredConstructor().newInstance();

            for (Field field : clazz.getDeclaredFields()) {
                int mod = field.getModifiers();
                if (Modifier.isFinal(mod) || Modifier.isStatic(mod)) {
                    continue;
                }
                field.setAccessible(true);
                field.set(o, m.get(field.getName()));
            }

            return clazz.cast(o);

        } catch (IllegalAccessException |
                 NoSuchMethodException |
                 InstantiationException |
                 InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    //从键列表和值列表得到
    public static <T> T buildFromFieldsAndValuesList(List<String> fields, List<?> values, Class<T> clazz)  {

        try {
            T o = clazz.getDeclaredConstructor().newInstance();

            for (int i = 0; i < fields.size() && i < values.size(); i++) {
                Field field = clazz.getDeclaredField(fields.get(i));
                int mod = field.getModifiers();
                if (Modifier.isFinal(mod) || Modifier.isStatic(mod)) {
                    continue;
                }
                field.setAccessible(true);
                field.set(o, values.get(i));
            }

            return o;

        } catch (NoSuchFieldException |
                 IllegalAccessException |
                 NoSuchMethodException |
                 InstantiationException |
                 InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    //得到对象的字段列表
    public static List<String> getFields(Class<?> clazz) {
        List<String> fieldsList = new ArrayList<>();

        for (Field field : clazz.getDeclaredFields()) {
            int mod = field.getModifiers();
            if (Modifier.isFinal(mod) || Modifier.isStatic(mod)) {
                continue;
            }
            fieldsList.add(field.getName());
        }

        return fieldsList;
    }

}
