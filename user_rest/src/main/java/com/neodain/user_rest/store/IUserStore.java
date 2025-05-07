package com.neodain.user_rest.store;

import com.neodain.user_rest.entity.User;

import java.util.List;
import java.util.Optional;

public interface IUserStore {
    User create(User user);
    User update(User user);
    void delete(String id);
    void clear();
    Optional<User> retrieve(String id);
    List<User> retrieveAll();

}
