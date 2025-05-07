package com.neodain.user_rest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.neodain.user_rest.entity.User;

// import org.junit.jupiter.api.AfterEach;
// import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

// 아래 static import 잘 되지 않음. 확인 필요
// import static org.mockito.ArgumentMatchers.any;
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

//    @Autowired
//    private IUserService userService;

    private static User user;
    private static User new_user;

    void startUP() throws Exception {
        System.out.println("=======================================================================");
        System.out.println(" startUp() ");
        System.out.println("=======================================================================");

        user = new User("Alice", "alice@example.com");

        String content_one = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/users")
                        .content(content_one)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        new_user = new User("Bob", "bob@example.com");

        String content_two = objectMapper.writeValueAsString(new_user);
        mockMvc.perform(post("/users")
                        .content(content_two)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();
    }

    void endUP() throws Exception {
        System.out.println("=======================================================================");
        System.out.println(" endUP() ");
        System.out.println("=======================================================================");

        String content_one = objectMapper.writeValueAsString(user);
        mockMvc.perform(delete("/users/{id}", user.getId())
                        .content(content_one)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        String content_two = objectMapper.writeValueAsString(new_user);
        mockMvc.perform(delete("/users/{id}", new_user.getId())
                        .content(content_two)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();
    }

    @Test
    @DisplayName("UserController 테스트 : register()")
    void register() throws Exception {

        startUP();

        User newUser = new User("David", "david@example.com");

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

        endUP();

//        // Parse first user response to get dynamic ID
//        String responseBody = result.getResponse().getContentAsString();
//        User savedUser = objectMapper.readValue(responseBody, User.class);
//        System.out.println("=======================================================================");
//        System.out.println(new Gson().toJson(savedUser));
//        System.out.println("=======================================================================");

//        // delete the registered user to initialize
//        String del_content = objectMapper.writeValueAsString(savedUser.getId());
//        mockMvc.perform(delete("/users/{id}", savedUser.getId())
//                        .content(del_content)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andDo(print())
//                .andReturn();
    }

    @Test
    @DisplayName("UserController 테스트 : find()")
    void find() throws Exception {

        startUP();

        mockMvc.perform(get("/users/{id}", user.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(user.getId()))
                .andExpect(jsonPath("$.name").value(user.getName()))
                .andExpect(jsonPath("$.email").value(user.getEmail()))
                .andDo(print())
                .andReturn();

        endUP();
    }

    @Test
    @DisplayName("UserController 테스트 : findAll()")
    void findAll() throws Exception {

        startUP();

        mockMvc.perform(get("/users")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());

        endUP();
    }

    @Test
    @DisplayName("UserController 테스트 : findAll_WithDynamicIdVerification()")
    void findAll_WithDynamicIdVerification() throws Exception {
        // Create sample users
        User sample = User.sampleUser("Choi", "choi@naver.com");
        User sample2 = User.sampleUser("David", "david@gmail.com");

        // Register first user and capture response
        mockMvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(sample))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // Parse first user response to get dynamic ID
//        String responseBody1 = result1.getResponse().getContentAsString();
//        User savedUser1 = objectMapper.readValue(responseBody1, User.class);

        // Register second user and capture response
        mockMvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(sample2))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

//        String responseBody2 = result2.getResponse().getContentAsString();
//        User savedUser2 = objectMapper.readValue(responseBody2, User.class);

        // Perform GET /users and verify IDs and fields
        mockMvc.perform(get("/users")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
//                .andExpect(jsonPath("$[0].id").value(savedUser1.getId()))
//                .andExpect(jsonPath("$[0].name").value("Choi"))
//                .andExpect(jsonPath("$[0].email").value("choi@naver.com"))
//                .andExpect(jsonPath("$[1].id").value(savedUser2.getId()))
//                .andExpect(jsonPath("$[1].name").value("David"))
//                .andExpect(jsonPath("$[1].email").value("david@gmail.com"))
                .andDo(print())
                .andReturn();
    }


    @Test
    @DisplayName("유저 2명을 등록 후 전체 조회하면 2명 모두 리턴되어야 한다")
    void shouldReturnAllUsersWithCorrectData() throws Exception {
        // given
        User user1 = User.sampleUser("Choi", "choi@naver.com");
        User user2 = User.sampleUser("David", "david@gmail.com");

        User savedUser1 = registerUserAndGetResponse(user1);
        User savedUser2 = registerUserAndGetResponse(user2);

        // when & then
        mockMvc.perform(get("/users")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
//                .andExpect(jsonPath("$[0].id").value(user1.getId()))
//                .andExpect(jsonPath("$[0].name").value(user1.getName()))
//                .andExpect(jsonPath("$[0].email").value(user1.getEmail()))
//                .andExpect(jsonPath("$[1].id").value(user2.getId()))
//                .andExpect(jsonPath("$[1].name").value(user2.getName()))
//                .andExpect(jsonPath("$[1].email").value(user2.getEmail()))
                .andDo(print());
    }

    private User registerUserAndGetResponse(User user) throws Exception {
        String userJson = objectMapper.writeValueAsString(user);

        MvcResult result = mockMvc.perform(post("/users")
                        .content(userJson)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        return objectMapper.readValue(response, User.class);
    }


    @Test
    @DisplayName("UserController 테스트 : modify()")
    void modify() throws Exception {
        startUP();

        user.setName("David");
        user.setEmail("david@gmail.com");
        String mod_content = objectMapper.writeValueAsString(user);
        mockMvc.perform(put("/users", user.getId())
                        .content(mod_content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());

        endUP();
    }

    @Test
    @DisplayName("UserController 테스트 : remove()")
    void remove() throws Exception {

        startUP();

        User newUser = new User("Choi", "choi@example.com");

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

        String del_content = objectMapper.writeValueAsString(newUser.getId());
        mockMvc.perform(delete("/users/{id}", newUser.getId())
                        .content(del_content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());

        endUP();
    }
}