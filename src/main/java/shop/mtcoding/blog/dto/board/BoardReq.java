package shop.mtcoding.blog.dto.board;

import lombok.Getter;
import lombok.Setter;

public class BoardReq {

    @Getter
    @Setter // 인증과 관련된게 아니기 때문에 앞에 Board라 붙여줌
    public static class BoardSaveReqDto {
        private String title;
        private String content;
    }
}
