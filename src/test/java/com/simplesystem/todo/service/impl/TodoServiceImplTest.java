package com.simplesystem.todo.service.impl;

import java.time.LocalDateTime;
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
        var mockTodoItemId = UUID.randomUUID();
        var mockCreatedDate = LocalDateTime.now();
        Todo todoItem = getTodo(mockTodoItemId, mockCreatedDate, "i will do this", mockCreatedDate.minusDays(-10L));
        when(todoRepository.save(any(Todo.class))).thenReturn(todoItem);

        var todoItemVerificationEntity = todoService.createTodo(todoItem);

        assertEquals(mockTodoItemId, todoItemVerificationEntity.getId());
        assertEquals(mockCreatedDate, todoItemVerificationEntity.getCreatedDate());
        verify(todoRepository, times(1)).save(todoItem);
    }

    @Test
    void shouldGetATodoItemById() {
        var mockTodoItemId = UUID.randomUUID();
        var mockCreatedDate = LocalDateTime.now();
        String description = "i will get this todo by id in the service";
        LocalDateTime mockDueDate = mockCreatedDate.minusDays(-10L);
        Todo todoItem = getTodo(mockTodoItemId, mockCreatedDate, description, mockDueDate);
        when(todoRepository.getReferenceById(mockTodoItemId.toString())).thenReturn(todoItem);

        var todoItemVerificationEntity = todoService.getTodoById(mockTodoItemId);

        assertEquals(mockTodoItemId, todoItemVerificationEntity.getId());
        assertEquals(description, todoItemVerificationEntity.getDescription());
        assertEquals(mockCreatedDate, todoItemVerificationEntity.getCreatedDate());
        assertEquals(mockDueDate, todoItemVerificationEntity.getDueDate());
        assertEquals(TodoStatus.NOT_DONE, todoItemVerificationEntity.getStatus());
        verify(todoRepository, times(1)).getReferenceById(mockTodoItemId.toString());
    }

    private Todo getTodo(UUID mockTodoItemId, LocalDateTime mockCreatedDate, String description, LocalDateTime mockDueDate) {
        return Todo.builder()
                .id(mockTodoItemId)
                .description(description)
                .createdDate(mockCreatedDate)
                .dueDate(mockDueDate)
                .status(TodoStatus.NOT_DONE)
                .build();
    }

}
