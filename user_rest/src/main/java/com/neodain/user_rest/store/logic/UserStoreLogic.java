package com.neodain.user_rest.store.logic;

import com.neodain.user_rest.entity.User;
import com.neodain.user_rest.store.IUserStore;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class UserStoreLogic implements IUserStore {

    private final Map<String, User> userMap = new ConcurrentHashMap<>();

//    public UserStoreLogic() {
//
//        // this.userMap = new HashMap<>(); // HashMap은 스레드 안전하지 않음
//        // 멀티스레드 환경에서는 스레드 안전을 위해 ConcurrentHashMap 사용
//        this.userMap = new ConcurrentHashMap<>();
//    }

    @Override
    public User create(User user) {
        if (user.getId() == null) {
            user.setId(UUID.randomUUID().toString());
        }
        userMap.put(user.getId(), user);
        return user;
    }

    @Override
    public Optional<User> retrieve(String id) {
        return Optional.ofNullable(userMap.get(id));
    }

    @Override
    public List<User> retrieveAll() {
//        return userMap.values().stream().collect(Collectors.toList());
        return new ArrayList<>(userMap.values());
    }

    @Override
    public User update(User user) {
        // update는 해당 id의 객체가 있다면 그대로 엎어친다.
        // userMap.put(user.getId(), user);
        if(userMap.containsKey(user.getId())) {
            userMap.put(user.getId(), user);
            return user;
        } else {
            Exception notFound = new Exception("User not found");
            throw new RuntimeException(notFound);
        }
    }

    @Override
    public void delete(String id) {
        userMap.remove(id);
    }

    @Override
    public void clear() {
        userMap.clear();
    }
}
