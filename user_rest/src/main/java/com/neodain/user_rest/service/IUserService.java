package com.neodain.user_rest.service;

import com.neodain.user_rest.entity.User;

import java.util.List;

public interface IUserService {
    User register(User user);
    User modify(User user);
    void remove(String id);
    void clear();

    User find(String id);
    List<User> findAll();
}
