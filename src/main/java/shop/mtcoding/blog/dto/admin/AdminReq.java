package shop.mtcoding.blog.dto.admin;

import lombok.Getter;
import lombok.Setter;

public class AdminReq {

    @Getter
    @Setter
    public static class AdminMailReq {
        private String email;
        private String title;
        private String message;
    }
}
