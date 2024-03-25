package com.simplesystem.todo.service.impl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.simplesystem.todo.model.Todo;
import com.simplesystem.todo.model.TodoFilterResponse;
import com.simplesystem.todo.model.TodoRequest;
import com.simplesystem.todo.model.TodoResponse;
import com.simplesystem.todo.model.TodoStatus;
import com.simplesystem.todo.repository.TodoRepository;
import com.simplesystem.todo.service.InvalidInputException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TodoServiceImplTest {
    @InjectMocks
    private TodoServiceImpl todoService;

    @Mock
    private TodoRepository todoRepository;

    @Mock
    private TodoMapper todoMapper;

    @Test
    void shouldAddATodoItem() {
        var mockTodoItemId = UUID.randomUUID();
        var mockCreatedDate = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyyyy HH:mm:ss");
        Todo todoItem = getTodo(mockTodoItemId, mockCreatedDate, "i will do this", mockCreatedDate.minusDays(-10L), TodoStatus.NOT_DONE);
        TodoRequest todoRequest = generateTodoRequest(mockTodoItemId, mockCreatedDate, "i will do this", mockCreatedDate.minusDays(-10L));
        TodoResponse todoResponse = generateTodoResponse(mockTodoItemId, "i will do this", formatter.format(mockCreatedDate), formatter.format(mockCreatedDate.minusDays(-10L)), TodoStatus.NOT_DONE);
        when(todoRepository.save(any(Todo.class))).thenReturn(todoItem);
        when(todoMapper.mapRequestToEntity(todoRequest)).thenReturn(todoItem);
        when(todoMapper.mapEntityToResponse(todoItem)).thenReturn(todoResponse);

        var todoItemVerificationEntity = todoService.createTodo(todoRequest);

        assertEquals(mockTodoItemId, todoItemVerificationEntity.id());
        assertEquals(formatter.format(mockCreatedDate), todoItemVerificationEntity.createdDate());

        verify(todoRepository, times(1)).save(todoItem);
    }

    private TodoResponse generateTodoResponse(UUID mockTodoItemId, String description, String mockCreatedDate, String dueDate, TodoStatus status) {
        return new TodoResponse(mockTodoItemId, description, mockCreatedDate, dueDate, null, status);
    }

    private TodoRequest generateTodoRequest(UUID mockTodoItemId, LocalDateTime mockCreatedDate, String description, LocalDateTime dueDate) {
        return new TodoRequest(mockTodoItemId, description, mockCreatedDate, dueDate, null, TodoStatus.NOT_DONE);
    }

    @Test
    void shouldGetATodoItemById() {
        var mockTodoItemId = UUID.randomUUID();
        var mockCreatedDate = LocalDateTime.now();
        String description = "i will get this todo by id in the service";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyyyy HH:mm:ss");
        LocalDateTime mockDueDate = mockCreatedDate.minusDays(-10L);
        Todo todoItem = getTodo(mockTodoItemId, mockCreatedDate, description, mockDueDate, TodoStatus.NOT_DONE);
        when(todoRepository.getReferenceById(mockTodoItemId)).thenReturn(todoItem);
        TodoResponse todoResponse = generateTodoResponse(mockTodoItemId, description, formatter.format(mockCreatedDate), formatter.format(mockDueDate), TodoStatus.NOT_DONE);
        when(todoMapper.mapEntityToResponse(todoItem)).thenReturn(todoResponse);


        var todoItemVerificationEntity = todoService.getTodoById(mockTodoItemId);

        assertEquals(mockTodoItemId, todoItemVerificationEntity.id());
        assertEquals(description, todoItemVerificationEntity.description());
        assertEquals(formatter.format(mockCreatedDate), todoItemVerificationEntity.createdDate());
        assertEquals(formatter.format(mockDueDate), todoItemVerificationEntity.dueDate());
        assertEquals(TodoStatus.NOT_DONE, todoItemVerificationEntity.status());
        verify(todoRepository, times(1)).getReferenceById(mockTodoItemId);
    }

    @Test
    void shouldGetInvalidInputExceptionWhenIdNotProvided() {
        assertThrows(InvalidInputException.class, () -> todoService.getTodoById(null));

        verifyNoInteractions(todoRepository);
    }

    @Test
    void shouldGetIllegalArgumentExceptionWhenIdInvalid() {
        assertThrows(IllegalArgumentException.class, () -> todoService.getTodoById(UUID.fromString("123")));

        verifyNoInteractions(todoRepository);
    }

    @Test
    void shouldUpdateATodoItem() {
        var mockTodoItemId = UUID.randomUUID();
        var mockCreatedDate = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyyyy HH:mm:ss");
        String description = "i will update this todo in the service";
        LocalDateTime mockDueDate = mockCreatedDate.plusDays(1L);
        Todo existingTodo = getTodo(mockTodoItemId, mockCreatedDate, description, mockDueDate, TodoStatus.NOT_DONE);
        Todo todoUpdateRequest = getTodo(mockTodoItemId, mockCreatedDate, "updated description", mockDueDate.plusDays(1L), TodoStatus.NOT_DONE);
        when(todoRepository.findById(mockTodoItemId)).thenReturn(Optional.ofNullable(existingTodo));
        when(todoRepository.save(any(Todo.class))).thenReturn(todoUpdateRequest);
        TodoRequest todoRequest = generateTodoRequest(mockTodoItemId, mockCreatedDate, "updated description", mockDueDate.plusDays(1L));
        TodoResponse todoResponse = generateTodoResponse(mockTodoItemId, "updated description", formatter.format(mockCreatedDate), formatter.format(mockCreatedDate.plusDays(1L)), TodoStatus.NOT_DONE);
        when(todoMapper.mapEntityToResponse(todoUpdateRequest)).thenReturn(todoResponse);

        var todoItemVerificationEntity = todoService.updateTodo(todoRequest);

        assertEquals(mockTodoItemId, todoItemVerificationEntity.id());
        assertEquals("updated description", todoItemVerificationEntity.description());
        assertEquals(formatter.format(mockCreatedDate), todoItemVerificationEntity.createdDate());
        assertEquals(formatter.format(mockDueDate), todoItemVerificationEntity.dueDate());
        assertEquals(TodoStatus.NOT_DONE, todoItemVerificationEntity.status());

        verify(todoRepository, times(1)).findById(mockTodoItemId);
        verify(todoRepository, times(1)).save(any(Todo.class));
    }

    @Test
    void shouldNotUpdateATodoItemWhenNotFound() {
        var mockTodoItemId = UUID.randomUUID();
        var mockCreatedDate = LocalDateTime.now();
        LocalDateTime mockDueDate = mockCreatedDate.minusDays(-10L);
        TodoRequest todoUpdateRequest = generateTodoRequest(mockTodoItemId, mockCreatedDate, "updated description", mockDueDate.plusDays(1L));
        when(todoRepository.findById(mockTodoItemId)).thenReturn(Optional.empty());


        assertThrows(EntityNotFoundException.class, () -> todoService.updateTodo(todoUpdateRequest));

        verify(todoRepository, times(1)).findById(mockTodoItemId);
        verifyNoMoreInteractions(todoRepository);
    }

    @Test
    void shouldFilterTodosByStatus() {
        var mockTodoItemId = UUID.randomUUID();
        var mockCreatedDate = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyyyy HH:mm:ss");
        LocalDateTime mockDueDate = mockCreatedDate.plusDays(1L);
        Todo todoFilterEntity1 = getTodo(mockTodoItemId, mockCreatedDate, "i will filter todos by status - 1", mockDueDate, TodoStatus.NOT_DONE);
        TodoResponse todoResponseItem1 = generateTodoResponse(mockTodoItemId, "i will filter todos by status - 1", formatter.format(mockCreatedDate), formatter.format(mockCreatedDate.plusDays(1L)), TodoStatus.NOT_DONE);
        TodoFilterResponse todoFilterResponse = new TodoFilterResponse();
        todoFilterResponse.setTodoResponseList(List.of(todoResponseItem1));
        when(todoRepository.findByStatus(TodoStatus.NOT_DONE)).thenReturn(List.of(todoFilterEntity1));
        when(todoMapper.mapEntityListToResponse(List.of(todoFilterEntity1))).thenReturn(List.of(todoResponseItem1));

        var todoFilterResult = todoService.filterByStatus(TodoStatus.NOT_DONE);

        assertIterableEquals(todoFilterResult.getTodoResponseList(), todoFilterResponse.getTodoResponseList());
    }

    private Todo getTodo(UUID mockTodoItemId, LocalDateTime mockCreatedDate, String description, LocalDateTime mockDueDate, TodoStatus status) {
        return Todo.builder()
                .id(mockTodoItemId)
                .description(description)
                .createdDate(mockCreatedDate)
                .dueDate(mockDueDate)
                .status(status)
                .build();
    }

}
