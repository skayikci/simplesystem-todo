package com.simplesystem.todo.repository;

import java.time.LocalDateTime;
import java.util.UUID;

import com.simplesystem.todo.model.Todo;
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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@TestPropertySource(properties = {"spring.test.database.replace=none"})
@ActiveProfiles("dev")
@Testcontainers
class TodoRepositoryIntegrationTest {

    @Autowired
    private TodoRepository todoRepository;

    @Container
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres")
            .withDatabaseName("test")
            .withUsername("test")
            .withPassword("test");

    @BeforeEach
    void setup() {
        System.setProperty("spring.datasource.url", postgreSQLContainer.getJdbcUrl());
        System.setProperty("spring.datasource.driver-class-name", postgreSQLContainer.getDriverClassName());
        System.setProperty("spring.datasource.username", postgreSQLContainer.getUsername());
        System.setProperty("spring.datasource.password", postgreSQLContainer.getPassword());
    }

    @AfterEach
    void cleanup() {
        todoRepository.deleteAll();
    }

    @Test
    void shouldRunDatabase() {
        assertTrue(postgreSQLContainer.isCreated());
        assertTrue(postgreSQLContainer.isRunning());
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

        createdTodo.setDescription("generated todo request");
        todoRepository.save(createdTodo);

        var updatedTodo = todoRepository.findById(createdTodo.getId()).orElse(null);
        assertEquals(createdTodo.getDescription(), updatedTodo.getDescription());
    }

    private UUID getNextIdForEntity() {
        return UUID.randomUUID();
    }
}
