package shop.mtcoding.blog.dto.board;

import java.sql.Timestamp;

import lombok.Getter;
import lombok.Setter;

public class BoardResp {

    @Getter
    @Setter
    public static class BoardMainRespDto {
        private int id;
        private String title;
        private String thumbnail;
        private String username;
        // 섬네일 다음에 추가
    }

    @Getter
    @Setter
    public static class BoardDetailRespDto {
        private int id;
        private String title;
        private String content;
        private int userId;
        private String username;
        // 좋아요, 댓글 추가
    }

    @Getter
    @Setter
    public static class BoardAllRespDto {
        private int id;
        private String title;
        private String content;
        private String thumbnail;
        private int userId;
        private String username;
        private Timestamp createdAt;
    }
}
