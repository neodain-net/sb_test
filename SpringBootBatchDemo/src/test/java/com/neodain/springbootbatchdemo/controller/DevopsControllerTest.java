package com.neodain.springbootbatchdemo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.neodain.springbootbatchdemo.entity.DevopsDto.DevopsRequest;
import com.neodain.springbootbatchdemo.entity.DevopsDto.DevopsResponse;
import com.neodain.springbootbatchdemo.service.IDevopsService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = DevopsController.class)
@DisplayName("DevopsController WebMvcTest")
class DevopsControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean 
    private IDevopsService devopsService;

    @Test
    @DisplayName("create devops")
    void create() throws Exception {
        DevopsRequest request = new DevopsRequest("team", "intro");
        DevopsResponse response = new DevopsResponse("id1", "team", "intro", LocalDateTime.now());
        Mockito.when(devopsService.create(any(DevopsRequest.class))).thenReturn(response);

        mockMvc.perform(post("/devops")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.devopsId").value(response.devopsId()))
                .andExpect(jsonPath("$.name").value("team"));

        verify(devopsService).create(any(DevopsRequest.class));
    }

    @Test
    @DisplayName("get devops")
    void getDevops() throws Exception {
        DevopsResponse response = new DevopsResponse("id1", "team", "intro", LocalDateTime.now());
        Mockito.when(devopsService.get("id1")).thenReturn(response);

        mockMvc.perform(get("/devops/{id}", "id1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.devopsId").value("id1"))
                .andExpect(jsonPath("$.name").value("team"));

        verify(devopsService).get("id1");
    }

    @Test
    @DisplayName("get all devops")
    void getAll() throws Exception {
        DevopsResponse response = new DevopsResponse("id1", "team", "intro", LocalDateTime.now());
        Mockito.when(devopsService.getAll()).thenReturn(List.of(response));

        mockMvc.perform(get("/devops"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].devopsId").value("id1"))
                .andExpect(jsonPath("$[0].name").value("team"));

        verify(devopsService).getAll();
    }

    @Test
    @DisplayName("update devops")
    void update() throws Exception {
        DevopsRequest request = new DevopsRequest("team2", "intro2");
        DevopsResponse response = new DevopsResponse("id1", "team2", "intro2", LocalDateTime.now());
        Mockito.when(devopsService.update(eq("id1"), any(DevopsRequest.class))).thenReturn(response);

        mockMvc.perform(put("/devops/{id}", "id1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("team2"));

        verify(devopsService).update(eq("id1"), any(DevopsRequest.class));
    }

    @Test
    @DisplayName("delete devops")
    void deleteDevops() throws Exception {
        mockMvc.perform(delete("/devops/{id}", "id1"))
                .andExpect(status().isNoContent());

        verify(devopsService).delete("id1");
    }
}