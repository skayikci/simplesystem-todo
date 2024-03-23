package com.simplesystem.todo.controller;

import java.time.OffsetDateTime;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.simplesystem.todo.model.Todo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
class TodoControllerTest {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();

    @Autowired
    MockMvc mockMvc;

    @Test
    void shouldCreateATodoItemViaRestPOST() throws Exception {
        var todoRequest = Todo.builder()
                .description("i will do this via rest post")
                .createdDate(OffsetDateTime.now())
                .dueDate(OffsetDateTime.now().minusDays(-10L))
                .build();
        String requestUrl = "/api/v1/todo";
        MockHttpServletRequestBuilder mockMvcRequestBuilders = MockMvcRequestBuilders
                .post(requestUrl)
                .content(GSON.toJson(todoRequest))
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(mockMvcRequestBuilders)
                .andExpect(status().isCreated());
    }
}
