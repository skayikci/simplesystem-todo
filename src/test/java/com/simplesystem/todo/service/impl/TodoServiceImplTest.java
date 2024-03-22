package com.simplesystem.todo.service.impl;

import java.time.OffsetDateTime;
import java.util.UUID;

import com.simplesystem.todo.model.Todo;
import com.simplesystem.todo.model.TodoStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class TodoServiceImplTest {

    @InjectMocks
    private TodoServiceImpl todoService;

    @Test
    void shouldAddATodoItem() {
        var mockTodoItemId = UUID.randomUUID().toString();
        var mockCreatedDate = OffsetDateTime.now();
        var todoItem = Todo.builder()
                .id(mockTodoItemId)
                .description("i will do this")
                .createdDate(mockCreatedDate)
                .dueDate(mockCreatedDate.minusDays(10L))
                .status(TodoStatus.NOT_DONE)
                .build();

        var todoItemVerificationEntity = todoService.createTodo(todoItem);

        assertEquals(mockTodoItemId, todoItemVerificationEntity.getId());
    }

}
