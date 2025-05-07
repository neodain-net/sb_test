package com.neodain.user_rest.service.logic;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.neodain.user_rest.entity.User;
import com.neodain.user_rest.service.IUserService;
import com.neodain.user_rest.store.IUserStore;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@SpringBootTest
@DisplayName("UserService 단위 테스트 : (Mockito + Spring Boot)")
public class UserServiceLogicTest {

    @MockitoBean // Sping의 ApplicationContext에 가짜 Bean을 주입한다.
    private IUserStore userStore; // 테스트 대상인 UserService는 가짜 IUserStore를 주입 받는다.

    // @InjectMocks  // 단위 Test에서는 생성자를 통한 주입과 LomBok RequiredArgConstructor에 의한 주입은 할 수 없다.
    @Autowired
    private IUserService userService;

    // private ObjectMapper objectMapper; // JSON 변환도구 : Java객체를 JSON으로 바꾸는 Jackson의 기능

    private User user;

    @BeforeEach // @BeforeEach, @AfterEach : 여러 테스트가 있을 경우, 테스트 전/후 초기화 작업 지정
    void setUp() {
        user = new User("Alice", "alice@example.com");

        // User 조회 시 가짜 결과 반환 : 특정 조건일 때 어떤 값을 반환할지 설정한다
        // 실제 저장 데이터를 호출하지 않고 설정한 결과만 반환하게 한다.
        Mockito.when(userStore.retrieve(user.getId()))
                .thenReturn(Optional.of(user));

        Mockito.when(userStore.retrieveAll())
                .thenReturn(List.of(user));

        // User 저장 시 저장된 객체 그대로 반환 : 실제 저장 데이터를 호출하지 않고 설정한 결과만 반환하게 한다.
        Mockito.when(userStore.create(any(User.class)))
                .thenAnswer(invocationOnMock -> {
                    return invocationOnMock.getArgument(0);
                });

        Mockito.when(userStore.update(any(User.class)))
                .thenAnswer(invocationOnMock -> {
                    User u = invocationOnMock.getArgument(0);
                    assertEquals(u.getId(), user.getId());
                    user.setName(u.getName());
                    user.setEmail(u.getEmail());
                    return u;
                });
    }

    @Test
    @DisplayName("UserService 단위 테스트 : register()")
    public void register() {
        User newUser = new User("Bob", "bob@example.com");
        User result = userService.register(newUser);
        assertNotNull(result.getId());
        assertEquals("Bob", result.getName());
        assertEquals("bob@example.com", result.getEmail());
        verify(userStore, Mockito.times(1)).create(newUser);
    }

    @Test
    @DisplayName("UserService 단위 테스트 : find()")
    public void find() {
        User result = userService.find(user.getId());
        assertNotNull(result.getId());
        assertEquals("Alice", result.getName());
        assertEquals("alice@example.com", result.getEmail());
        verify(userStore, Mockito.times(1)).retrieve(user.getId());
    }

    @Test
    @DisplayName("UserService 단위 테스트 : findAll()")
    public void findAll() {
        List<User> results = userService.findAll();
        assertThat(results.size()).isEqualTo(1);
        assertThat(results.get(0).getId()).isEqualTo(user.getId());
        assertThat(results.get(0).getName()).isEqualTo("Alice");
        verify(userStore, Mockito.times(1)).retrieveAll();
    }

    @Test
    @DisplayName("UserService 단위 테스트 : modify()")
    public void modify() {
        User newUser = new User("Bob", "bob@example.com");
        System.out.println(new Gson().toJson(user));
        newUser.setId(user.getId());
        userService.modify(newUser);
        assertNotNull(newUser.getId());
        assertEquals("Bob", user.getName());
        assertEquals("bob@example.com", user.getEmail());
        verify(userStore, Mockito.times(1)).update(any(User.class));
    }

    @Test
    @DisplayName("UserService 단위 테스트 : remove()")
    public void remove() {
        userService.remove(user.getId());
        assertNotNull(user.getId());
        verify(userStore, Mockito.times(1)).delete(user.getId());
    }

    @AfterEach
    public void tearDown() {
        System.out.println(new Gson().toJson(user));
    }

//    public void registerTest(){
//        User sample = User.sampleUser();
//        User sample2 = User.sampleUser("Park", "park@naver.com");
//
//        // this.userService.register(sample);
//        assertThat(this.userService.register(sample).getId()).isEqualTo(sample.getId());
//        assertThat(this.userService.register(sample2).getId()).isEqualTo(sample2.getId());
//        assertThat(this.userService.findAll().size()).isEqualTo(4);
//        this.userService.remove(sample.getId());
//        this.userService.remove(sample2.getId());
//    }

}
