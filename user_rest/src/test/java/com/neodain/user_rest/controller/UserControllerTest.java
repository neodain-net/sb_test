package com.neodain.user_rest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.neodain.user_rest.entity.User;

import com.neodain.user_rest.service.IUserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
// import org.springframework.test.web.servlet.MvcResult;

// 아래 static import 잘 되지 않음. 확인 필요
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// @WebMvcTest(controllers = UserController.class)
// Controller만 메모리에 로딩하여 오직 UserController만 테스트 대상에 포함시켜서 가볍고 빠름.
@SpringBootTest
@AutoConfigureMockMvc  // MockMvc 객체 Bean을 사용하기 위해 사용. Controller는 @WebMvcTest 사용하는것이 좋다고 하는데 여기서는 오류 발생.
@DisplayName("UserController 단위 테스트 + Mockito")
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;   // Test 에서 MVC 테스트를 위해 필요한 객체 : HTTP 요청 시뮬레이션 도구

    @Autowired
    private ObjectMapper objectMapper; // JSON 변환도구 : Java객체를 JSON으로 바꾸는 Jackson의 기능

    @MockitoBean
    private IUserService userService;

    @Test
    @DisplayName("UserController 테스트 : register()")
    void register() throws Exception {

        User newUser = new User("David", "david@example.com");
        Mockito.when(userService.register(newUser)).thenReturn(newUser);

        String content = objectMapper.writeValueAsString(newUser);

        mockMvc.perform(post("/users")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(newUser.getId()))
                .andExpect(jsonPath("$.name").value(newUser.getName()))
                .andExpect(jsonPath("$.email").value(newUser.getEmail()))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        verify(userService, Mockito.times(1)).register(newUser);
    }

    @Test
    @DisplayName("UserController 테스트 : find()")
    void find() throws Exception {

        User user = new User("David", "david@example.com");
        Mockito.when(userService.find(user.getId())).thenReturn(user);

//        String content = objectMapper.writeValueAsString(user);

        mockMvc.perform(get("/users/{id}", user.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(user.getId()))
                .andExpect(jsonPath("$.name").value(user.getName()))
                .andExpect(jsonPath("$.email").value(user.getEmail()))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        verify(userService, Mockito.times(1)).find(user.getId());
    }

    @Test
    @DisplayName("UserController 테스트 : findAll()")
    void findAll() throws Exception {

        User user1 = new User("Alice", "alice@example.com");
        User user2 = new User("David", "david@example.com");

        List<User> users = new ArrayList<>();

        users.add(user1);
        users.add(user2);

        Mockito.when(userService.findAll()).thenReturn(users);

        mockMvc.perform(get("/users")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(user1.getId()))
                .andExpect(jsonPath("$[0].name").value(user1.getName()))
                .andExpect(jsonPath("$[0].email").value(user1.getEmail()))
                .andExpect(jsonPath("$[1].id").value(user2.getId()))
                .andExpect(jsonPath("$[1].name").value(user2.getName()))
                .andExpect(jsonPath("$[1].email").value(user2.getEmail()))
                .andExpect(status().isOk())
                .andDo(print());

        verify(userService, Mockito.times(1)).findAll();
    }

    @Test
    @DisplayName("UserController 테스트 : modify()")
    void modify() throws Exception {

        User user = new User("Alice", "alice@example.com");
        Mockito.when(userService.modify(user)).thenReturn(null);

        user.setName("David");
        user.setEmail("david@gmail.com");
        String content = objectMapper.writeValueAsString(user);
        mockMvc.perform(put("/users")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());

        verify(userService, Mockito.times(1)).modify(user);
    }

    @Test
    @DisplayName("UserController 테스트 : remove()")
    void remove() throws Exception {

        User user = new User("David", "david@example.com");

        mockMvc.perform(delete("/users/{id}", user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());

        verify(userService, Mockito.times(1)).remove(user.getId());
    }
}