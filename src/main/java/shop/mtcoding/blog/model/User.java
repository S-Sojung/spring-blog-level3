package shop.mtcoding.blog.model;

import java.sql.Timestamp;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {
    private int id;
    private String username;
    private String password;
    private String email;
    private Timestamp createdAt;
    // 당장 쓰지 않을 프로필 사진은 지금은 넣지 않음.
}