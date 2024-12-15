package kiss.depot.websocket.model.dto.messageQueue;

import kiss.depot.websocket.util.WebsocketUtil;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/*
* 强制下线操作需要一些检查数据来确定获取该消息的用户设备是否是需要被下线的设备
* author: koishikiss
* launch: 2024/12/15
* last update: 2024/12/15
* */

@Getter
@Setter
@NoArgsConstructor
public class ForceLogoutCheckInfo {

    private String logoutWebsocketSessionId;  //需要被登出的设备的websocketSessionId

    private LocalDateTime commandTime;  //该命令的有效时间

    public ForceLogoutCheckInfo(String uid, String sessionId) {
        logoutWebsocketSessionId = WebsocketUtil.generatorWebsocketSessionId(uid, sessionId);
        commandTime = LocalDateTime.now().plusSeconds(20);
    }

    //检查命令是否匹对
    public boolean checkMatched(String websocketSessionId) {
        return websocketSessionId.equals(logoutWebsocketSessionId);
    }

    //检查命令是否过期
    public boolean checkUnexpired() {
        return LocalDateTime.now().isBefore(commandTime);
    }

}
