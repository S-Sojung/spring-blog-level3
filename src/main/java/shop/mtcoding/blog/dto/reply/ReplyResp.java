package shop.mtcoding.blog.dto.reply;

import java.sql.Timestamp;

import lombok.Getter;
import lombok.Setter;

public class ReplyResp {

    @Getter
    @Setter
    public static class ReplyDetailRespDto {
        private Integer id;
        private String comment;
        private Integer userId;
        private Integer boardId;
        private String username;
    }

    @Getter
    @Setter
    public static class ReplyAllRespDto {
        private Integer id;
        private String comment;
        private Integer userId;
        private String username;
        private Integer boardId;
        private Timestamp createdAt;
    }
}
