package com.simplesystem.todo.service.impl;

import com.simplesystem.todo.model.Todo;
import com.simplesystem.todo.repository.TodoRepository;
import com.simplesystem.todo.service.TodoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TodoServiceImpl implements TodoService {

    private final TodoRepository todoRepository;

    @Override
    public Todo createTodo(Todo todoRequest) {
        return todoRepository.save(todoRequest);
    }
}
