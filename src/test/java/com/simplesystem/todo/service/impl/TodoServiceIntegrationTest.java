package com.simplesystem.todo.service.impl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import com.simplesystem.todo.model.Todo;
import com.simplesystem.todo.model.TodoRequest;
import com.simplesystem.todo.model.TodoStatus;
import com.simplesystem.todo.repository.TodoRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@TestPropertySource(properties = {"spring.test.database.replace=none"})
@ActiveProfiles("dev")
@Testcontainers
@ComponentScan(basePackages = {"com.simplesystem.todo.service.impl"})
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

        var todoRequest = new TodoRequest(createdTodo.getId(), "generated todo request",
                null, null, null, TodoStatus.NOT_DONE);
        var updatedTodo = todoService.updateTodo(todoRequest);


        assertEquals(createdTodo.getCreatedDate().format(formatter), LocalDateTime.parse(updatedTodo.createdDate()).format(formatter));
        assertEquals(todoRequest.description(), updatedTodo.description());
    }

    private UUID getNextIdForEntity() {
        return UUID.randomUUID();
    }
}
