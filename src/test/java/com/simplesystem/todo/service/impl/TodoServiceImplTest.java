package com.simplesystem.todo.service.impl;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.UUID;

import com.simplesystem.todo.model.Todo;
import com.simplesystem.todo.model.TodoStatus;
import com.simplesystem.todo.repository.TodoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TodoServiceImplTest {
    @InjectMocks
    private TodoServiceImpl todoService;

    @Mock
    private TodoRepository todoRepository;

    @Test
    void shouldAddATodoItem() {
        var mockTodoItemId = UUID.randomUUID().toString();
        var mockCreatedDate = LocalDateTime.now();
        var todoItem = Todo.builder()
                .id(mockTodoItemId)
                .description("i will do this")
                .createdDate(mockCreatedDate)
                .dueDate(mockCreatedDate.minusDays(10L))
                .status(TodoStatus.NOT_DONE)
                .build();
        when(todoRepository.save(any(Todo.class))).thenReturn(todoItem);

        var todoItemVerificationEntity = todoService.createTodo(todoItem);

        assertEquals(mockTodoItemId, todoItemVerificationEntity.getId());
        verify(todoRepository, times(1)).save(todoItem);
    }

}
