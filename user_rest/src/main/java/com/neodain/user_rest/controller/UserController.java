package com.neodain.user_rest.controller;

import com.neodain.user_rest.entity.User;
import com.neodain.user_rest.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor  // 생성자를 사용하지 않아도 되지만, 주입할 대상을 final로 선언해야 한다.
// @RequestMapping("/users") // 아래 Mapping에서 /users를 사용해서 생략, 만약 이것을 사용할 경우, 아래 매핑들은 /user를 생략해야한다.
public class UserController {
    // Controller에 userService를 주입하여 사용하기 위해
    private final IUserService userService;

//    // Lombok의 @RequiredArgsConstructor 어노테이션을 사용해서 생성자는 생략해도 된다.
//    public UserController(IUserService userService) {
//        this.userService = userService;
//    }

    @PostMapping("/users")  // Post 방식이기 때문에 데이터는 body에 담겨져 오기 때문에 @RequestBody 어노테이션을 사용해야 한다.
    public User register(@RequestBody User newUser) {
        return userService.register(newUser);
    }
    @GetMapping("/users/{id}")
    public User find(@PathVariable String id){
        return userService.find(id);
    }
    @GetMapping("/users")
    public List<User> findAll(){
        return userService.findAll();
    }
    @PutMapping("/users")
    public void modify(@RequestBody User newUser) {
        userService.modify(newUser);
    }
    @DeleteMapping("/users/{id}")
    public void remove(@PathVariable String id) {
        userService.remove(id);
    }
}
