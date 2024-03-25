package com.simplesystem.todo.service.impl;

import java.util.UUID;

import com.simplesystem.todo.model.Todo;
import com.simplesystem.todo.repository.TodoRepository;
import com.simplesystem.todo.service.TodoService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class TodoServiceImpl implements TodoService {

    private final TodoRepository todoRepository;

    @Override
    public Todo createTodo(Todo todoRequest) {
        return todoRepository.save(todoRequest);
    }

    @Override
    public Todo getTodoById(UUID todoId) {
        return todoRepository.getReferenceById(todoId);
    }

    @Override
    public Todo updateTodo(Todo todoRequest) {
        var requestedTodoItem = todoRepository.findById(todoRequest.getId());
        if (requestedTodoItem.isPresent()) {
            return todoRepository.save(todoRequest);
        } else {
            log.error("Error while updating the todo item, please check the id: {}", todoRequest.getId());
            throw new EntityNotFoundException("Given entity with id not found");
        }
    }
}
