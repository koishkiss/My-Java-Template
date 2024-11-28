package kiss.depot.redis.model.constant;

public class REDIS_KEY {

    public static class AUTH {
        private static final String auth = "auth";

        public static class CODE {
            private static final String code = auth + ":code";

            public static String join(String key) {
                return code + ":" + key;
            }
        }

        public static class SESSION {
            private static final String session = auth + ":session";

            public static String join(String key) {
                return auth + ":" + key;
            }
        }
    }

    public static class USER {
        private static final String user = "user";

        public static class INFO {
            private static final String info = user + ":info";

            public static String join(String key) {
                return info + ":" + key;
            }
        }
    }

}
