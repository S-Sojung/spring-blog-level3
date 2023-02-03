package shop.mtcoding.blog.dto.user;

import lombok.Getter;
import lombok.Setter;

public class UserReq {

    // 통신을 위해서 적재적소에 필요한 애들이지 변할 일은 거의 없음
    @Getter
    @Setter
    public static class JoinReqDto {
        private String username;
        private String password;
        private String email;
    }
}
