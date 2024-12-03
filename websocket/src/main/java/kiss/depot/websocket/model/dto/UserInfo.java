package kiss.depot.websocket.model.dto;

import kiss.depot.websocket.model.po.AuthPo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserInfo {

    private Long uid;

    private String nickname;

    public UserInfo(AuthPo authPo) {
        uid = authPo.getUid();
        nickname = authPo.getNickname();
    }

}
