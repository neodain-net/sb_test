package com.neodain.user_rest.service.logic;

import com.neodain.user_rest.entity.User;
import com.neodain.user_rest.service.IUserService;
import com.neodain.user_rest.store.IUserStore;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

// DI 을 하는 방식
// 1. @AutoWired 어노테이션을 사용하는 방법
// 2. 생성자를 사용하여 주입 (추천)
// 3. LomBok의 @RequiredArgConstructor 어노테이션을 사용하는 방법
// @RequiredArgsConstructor 어노테이션을 사용할 경우, private final UserService userService;
// final을 추가하여 초기화를 반드시 수행하도록 I/F를 선언한다.
@Service
public class UserServiceLogic implements IUserService {

    // @Autowired
    private final IUserStore userStore;

    public UserServiceLogic(IUserStore userStore) {
        this.userStore = userStore;
    }

    @Override
    public User register(User newUser) {
        return this.userStore.create(newUser);
    }
    @Override
    public User find(String id) {
        return this.userStore.retrieve(id).orElseThrow(() -> new NoSuchElementException("User not found"));
    }

    @Override
    public List<User> findAll() {
        return this.userStore.retrieveAll();
    }

    @Override
    public User modify(User user) {
        this.userStore.update(user);
        return user;
    }

    @Override
    public void remove(String id) {
        this.userStore.delete(id);
    }

    @Override
    public void clear() {
        this.userStore.clear();
    }
}
