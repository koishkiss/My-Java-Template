package kiss.depot.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@TableName("depot_post")
public class Post {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField(value = "content")
    private String content;

    @TableField(value = "post_time")
    private LocalDateTime postTime;

    public Post(String content) {
        this.content = content;
    }

    //修改content的测试
    public void convertContent() {
        content = "test_" + content;
    }

}


/*
* 表结构：
CREATE TABLE `depot_post` (
  `id` int NOT NULL AUTO_INCREMENT,
  `content` varchar(100) NOT NULL COMMENT '随机字符串',
  `post_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '字段产生时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
* */