package kiss.depot.redis.model.enums;

import lombok.AllArgsConstructor;

/*
* redis键前缀枚举类
* author: koishikiss
* launch: 2024/11/28
* last update: 2024/11/30
* */

@AllArgsConstructor
public enum RedisKey {
    USER_INFO("user:info"),
    USER_SESSION("user:session");

    public final String prefix;

    public String concat(String... keys) {
        StringBuilder builder = new StringBuilder(this.prefix);
        for (String key : keys) {
            builder.append(':').append(key);
        }
        return builder.toString();
    }


// 试了试以下写法，但并不好用
//    public static class AUTH {
//        private static final String auth = "auth";
//
//        public static class CODE {
//            private static final String code = auth + ":code";
//
//            public static String join(String key) {
//                return code + ":" + key;
//            }
//        }
//
//        public static class SESSION {
//            private static final String session = auth + ":session";
//
//            public static String join(String key) {
//                return auth + ":" + key;
//            }
//        }
//    }
//
//    public static class USER {
//        private static final String user = "user";
//
//        public static class INFO {
//            private static final String info = user + ":info";
//
//            public static String join(String key) {
//                return info + ":" + key;
//            }
//        }
//    }

}
