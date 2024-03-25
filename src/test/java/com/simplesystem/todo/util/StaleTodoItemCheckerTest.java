package com.simplesystem.todo.util;

import com.simplesystem.todo.model.Todo;
import com.simplesystem.todo.model.TodoStatus;
import com.simplesystem.todo.repository.TodoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class StaleTodoItemCheckerTest {

    @Mock
    private TodoRepository todoRepository;

    @InjectMocks
    private StaleTodoItemChecker staleTodoItemChecker;

    @Test
    void shouldUpdateStaleItemStatusToPAST_DUE() {
        LocalDateTime currentTime = LocalDateTime.now();
        List<Todo> staleItems = new ArrayList<>();
        Todo staleItem1 = new Todo();
        staleItem1.setId(UUID.randomUUID());
        staleItem1.setDueDate(currentTime.minusDays(1));
        staleItem1.setStatus(TodoStatus.NOT_DONE);
        staleItems.add(staleItem1);

        when(todoRepository.findByDueDateBefore(any(LocalDateTime.class))).thenReturn(staleItems);

        staleTodoItemChecker.checkForStaleTodoItems();

        verify(todoRepository, times(1)).findByDueDateBefore(any(LocalDateTime.class));
        verify(todoRepository, times(1)).save(staleItem1);
        assertEquals(TodoStatus.PAST_DUE, staleItem1.getStatus());
    }

    @Test
    void shouldNotCallRepositorySaveWhenEmptyList() {
        List<Todo> emptyList = new ArrayList<>();
        when(todoRepository.findByDueDateBefore(any(LocalDateTime.class))).thenReturn(emptyList);

        staleTodoItemChecker.checkForStaleTodoItems();

        verify(todoRepository, times(1)).findByDueDateBefore(any(LocalDateTime.class));
        verify(todoRepository, never()).save(any());
    }
}
