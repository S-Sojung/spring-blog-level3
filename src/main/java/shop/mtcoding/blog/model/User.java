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
    private String profile; // 사진의 경로. (static/image 폴더에 사진 추가하기)
    private String role;
    // 만약 다른 서버에 옮긴다면 폴더명이 바뀌면 안된다!!!
}