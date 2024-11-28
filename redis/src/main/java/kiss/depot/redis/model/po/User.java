package kiss.depot.redis.model.po;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class User {

    private String uid;

    private String nickname;

    private String password;

}
