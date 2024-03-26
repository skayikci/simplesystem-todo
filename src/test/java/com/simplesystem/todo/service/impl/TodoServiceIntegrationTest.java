package com.simplesystem.todo.service.impl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import com.simplesystem.todo.model.Todo;
import com.simplesystem.todo.model.TodoRequest;
import com.simplesystem.todo.model.TodoStatus;
import com.simplesystem.todo.repository.TodoRepository;
import com.simplesystem.todo.util.TodoRequestBuilder;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@TestPropertySource(properties = {"spring.test.database.replace=none"})
@ActiveProfiles("dev")
@Testcontainers
class TodoServiceIntegrationTest {
    @Container
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres")
            .withDatabaseName("test")
            .withUsername("test")
            .withPassword("test");

    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private TodoServiceImpl todoService;

    @BeforeEach
    void setup() {
        System.setProperty("spring.datasource.url", postgreSQLContainer.getJdbcUrl());
        System.setProperty("spring.datasource.username", postgreSQLContainer.getUsername());
        System.setProperty("spring.datasource.password", postgreSQLContainer.getPassword());
    }

    @AfterEach
    void cleanup() {
        todoRepository.deleteAll();
    }

    @Test
    void shouldUpdateTodoStatus() {
        var originalCreatedDate = LocalDateTime.now().minusDays(1);
        var todoEntity = Todo.builder()
                .description("I will test this with integration")
                .createdDate(originalCreatedDate)
                .id(getNextIdForEntity())
                .build();
        var createdTodo = todoRepository.save(todoEntity);
        var formatter = DateTimeFormatter.ofPattern("ddMMyyyy HH:mm:ss");
        TodoRequest todoRequest = new TodoRequestBuilder()
                .withDescription("generated todo request")
                .withStatus(TodoStatus.DONE)
                .withId(createdTodo.getId())
                .build();

        var updatedTodo = todoService.updateTodo(todoRequest);

        assertEquals(createdTodo.getCreatedDate().format(formatter), LocalDateTime.parse(updatedTodo.createdDate()).format(formatter));
        assertEquals(todoRequest.description(), updatedTodo.description());
        assertEquals(TodoStatus.DONE, updatedTodo.status());
    }

    @Test
    void shouldNotUpdateWhenTodoWithIdNotFound() {
        var originalCreatedDate = LocalDateTime.now().minusDays(1);
        var todoEntity = Todo.builder()
                .description("I will test this with integration")
                .createdDate(originalCreatedDate)
                .id(getNextIdForEntity())
                .build();
        todoRepository.save(todoEntity);
        var arbitraryId = UUID.randomUUID();
        var todoRequest = new TodoRequestBuilder()
                .withId(arbitraryId)
                .withDescription("test with some arbitrary generated uuid")
                .withStatus(TodoStatus.DONE)
                .build();

        assertThrows(EntityNotFoundException.class, () -> todoService.updateTodo(todoRequest));

        var updatedTodoVerificationEntity = todoRepository.findById(arbitraryId);
        assertThat(updatedTodoVerificationEntity).isEmpty();
    }

    @Test
    void shouldCreateTodoItemCorrectly() {
        var todoRequest = new TodoRequestBuilder()
                .withDescription("Create todo item with integration")
                .withStatus(TodoStatus.NOT_DONE)
                .build();

        var createdTodo = todoService.createTodo(todoRequest);

        assertEquals(todoRequest.description(), createdTodo.description());
        assertNotNull(createdTodo.createdDate());
        assertEquals(TodoStatus.NOT_DONE, createdTodo.status());
    }

    private UUID getNextIdForEntity() {
        return UUID.randomUUID();
    }
}
