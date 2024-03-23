package com.simplesystem.todo.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.simplesystem.todo.controller.adapter.LocalDateTimeTypeAdapter;
import com.simplesystem.todo.model.Todo;
import com.simplesystem.todo.service.TodoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
class TodoControllerTest {

    @MockBean
    TodoService todoService;

    @Autowired
    MockMvc mockMvc;

    @Test
    void shouldCreateATodoItemViaRestPOST() throws Exception {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter())
                .create();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyyyy HH:mm:ss");

        var todoRequest = Todo.builder()
                .description("i will do this via rest post")
                .createdDate(LocalDateTime.now())
                .dueDate(LocalDateTime.now().minusDays(-10L))
                .build();
        String requestUrl = "/api/v1/todo";
        MockHttpServletRequestBuilder mockMvcRequestBuilders = MockMvcRequestBuilders
                .post(requestUrl)
                .content(gson.toJson(todoRequest))
                .contentType(MediaType.APPLICATION_JSON);

        System.out.println("body: " + gson.toJson(todoRequest));

        mockMvc.perform(mockMvcRequestBuilders)
                .andExpect(status().isCreated());
        verify(todoService, times(1)).createTodo(any(Todo.class));
    }
}
