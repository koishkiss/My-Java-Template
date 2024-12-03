package kiss.depot.websocket.model.enums;

import lombok.AllArgsConstructor;

/*
* redis键前缀枚举类
* author: koishikiss
* launch: 2024/11/28
* last update: 2024/11/30
* */

@AllArgsConstructor
public enum RedisKey {
    USER_SESSION("user:session"),
    USER_INFO("user:info");

    public final String prefix;

    public String concat(String... keys) {
        StringBuilder builder = new StringBuilder(this.prefix);
        for (String key : keys) {
            builder.append(':').append(key);
        }
        return builder.toString();
    }

}
