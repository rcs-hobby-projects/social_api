package com.example.socialapi.user;

import com.example.socialapi.user.dto.CreateUserRequest;

import jakarta.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.json.JsonMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JsonMapper jsonMapper;

    @Test
    void mustCreateUser() throws Exception {
        var request = new CreateUserRequest(
                "Rafael", "rafael@email.com", "senha123");

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Rafael"))
                .andExpect(jsonPath("$.email").value("rafael@email.com"));
    }

    @Test
    void mustSearchUserByIdWithSuccess() throws Exception {
        var createRequest = new CreateUserRequest("Rafael", "rafael@email.com", "password123");

        var createResult = mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andReturn();

        var responseBody = createResult.getResponse().getContentAsString();
        var createdUser = jsonMapper.readTree(responseBody);
        var userId = createdUser.get("id").asLong();

        mockMvc.perform(get("/users/{id}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId))
                .andExpect(jsonPath("$.name").value("Rafael"));
    }

    @Test
    void mustReturn404WhenUserThereIsnt() throws Exception {
        mockMvc.perform(get("/users/{id}", 999L))
                .andExpect(status().isNotFound());
    }

    void mustListEveryUsers() throws Exception {
        mockMvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON)
                .content(jsonMapper.writeValueAsString(
                        new CreateUserRequest("Rafael", "rafael@email.com", "password123"))))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON)
                .content(jsonMapper.writeValueAsString(
                        new CreateUserRequest("Maria", "maria@email.com", "password456"))))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void mustDeleteUserWithSuccess() throws Exception {
        var createResult = mockMvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON)
                .content(jsonMapper
                        .writeValueAsString(new CreateUserRequest("Rafael", "rafael@email.com", "password123"))))
                .andExpect((status().isCreated())).andReturn();

        var responseBody = createResult.getResponse().getContentAsString();
        var userId = jsonMapper.readTree(responseBody).get("id").asLong();

        mockMvc.perform(delete("/users/{id}", userId)).andExpect(status().isNoContent());

        mockMvc.perform(get("/users/{id}", userId))
                .andExpect(status().isNotFound());
    }
}
