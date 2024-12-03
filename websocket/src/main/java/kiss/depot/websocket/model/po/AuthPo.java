package kiss.depot.websocket.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import kiss.depot.websocket.util.BCryptUtil;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@TableName("user_auth")
public class AuthPo {

    @TableId(value = "uid", type = IdType.ASSIGN_ID)
    private Long uid;

    @TableField(value = "nickname")
    private String nickname;

    @TableField(value = "password")
    private String password;

    //检查昵称
    public boolean checkNicknameIsNotValid() {
        return nickname == null || nickname.isBlank();
    }

    public boolean checkPasswordIsNotValid() {
        return password == null || password.isBlank();
    }

    //加密密码
    public void encryptPassword() {
        password = BCryptUtil.encrypt(password);
    }

    //比对密码
    public boolean validatePassword(String storedPassword) {
        return BCryptUtil.validate(password, storedPassword);
    }

}
