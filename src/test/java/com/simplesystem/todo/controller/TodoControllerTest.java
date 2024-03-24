package com.simplesystem.todo.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.simplesystem.todo.controller.adapter.LocalDateTimeTypeAdapter;
import com.simplesystem.todo.model.Todo;
import com.simplesystem.todo.model.TodoStatus;
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
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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

        mockMvc.perform(mockMvcRequestBuilders)
                .andExpect(status().isCreated());

        verify(todoService, times(1)).createTodo(any(Todo.class));
    }

    @Test
    void shouldGetTodoItemById() throws Exception {
        var todoId = UUID.randomUUID();
        String description = "i will get this todo by id";
        LocalDateTime createdDate = LocalDateTime.now();
        LocalDateTime dueDate = createdDate.minusDays(-10L);
        var todoRequest = Todo.builder()
                .id(todoId)
                .description(description)
                .createdDate(createdDate)
                .dueDate(dueDate)
                .build();
        String requestUrl = "/api/v1/todo/".concat(todoId.toString());
        MockHttpServletRequestBuilder mockMvcRequestBuilders = MockMvcRequestBuilders
                .get(requestUrl)
                .contentType(MediaType.APPLICATION_JSON);
        when(todoService.getTodoById(todoId)).thenReturn(todoRequest);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyyyy HH:mm:ss");


        mockMvc.perform(mockMvcRequestBuilders)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(equalTo(todoId.toString())))
                .andExpect(jsonPath("$.description").value(equalTo(description)))
                .andExpect(jsonPath("$.createdDate").value(equalTo(formatter.format(createdDate))))
                .andExpect(jsonPath("$.dueDate").value(equalTo(formatter.format(dueDate))))
                .andExpect(jsonPath("$.status").value(equalTo(TodoStatus.NOT_DONE.name())));


        verify(todoService, times(1)).getTodoById(todoId);
    }
}
