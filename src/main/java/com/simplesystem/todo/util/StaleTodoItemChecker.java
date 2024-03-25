package com.simplesystem.todo.util;

import java.time.LocalDateTime;
import java.util.List;

import com.simplesystem.todo.model.Todo;
import com.simplesystem.todo.model.TodoStatus;
import com.simplesystem.todo.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class StaleTodoItemChecker {

    private final TodoRepository todoRepository;

    @Scheduled(fixedRate = 60000)
    public void checkForStaleTodoItems() {
        List<Todo> staleItems = todoRepository.findByDueDateBefore(LocalDateTime.now());
        log.debug("Checking for stale items...");
        for (Todo item : staleItems) {
            item.setStatus(TodoStatus.PAST_DUE);
             todoRepository.save(item);
        }
    }
}
