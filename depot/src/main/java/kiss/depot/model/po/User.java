package kiss.depot.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@TableName("depot_user")
public class User {

    @TableId(value = "uid", type = IdType.AUTO)
    private Integer uid;

    @TableField(value = "sid")
    private String sid;

    @TableField(value = "name")
    private String name;

    @TableField(value = "password")
    private String password;

    @TableField(value = "nickname")
    private String nickname;

    public User(String sid, String name, String password) {
        this.sid = sid;
        this.name = name;
        this.password = password;
        nickname = "u_" + sid;
    }

}


/*
* 表结构：
CREATE TABLE `depot_user` (
  `uid` int NOT NULL AUTO_INCREMENT,
  `sid` varchar(100) NOT NULL,
  `name` varchar(100) NOT NULL,
  `password` varchar(100) NOT NULL,
  `nickname` varchar(100) NOT NULL,
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
* */
