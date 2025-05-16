package com.neodain.user_rest.entity;

import com.google.gson.Gson;
import lombok.*;
import org.springframework.boot.autoconfigure.domain.EntityScan;

import java.util.UUID;

@Data
@Getter
@Setter
@EntityScan
@AllArgsConstructor  // 모든 Arg 가 있는 생성자 자동 생성
public class User {
    private String id;
    private String name;
    private String email;

    public User() {
        this.id = UUID.randomUUID().toString();
    }

    public User(String name, String email) {
        this();
        this.name = name;
        this.email = email;
    }

    public static void main(String[] args) {
        User user = new User("Park", "park@neodain.net");
    }
}
