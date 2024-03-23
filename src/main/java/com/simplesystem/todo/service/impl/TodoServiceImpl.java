package com.simplesystem.todo.service.impl;

import com.simplesystem.todo.model.Todo;
import com.simplesystem.todo.service.TodoService;
import org.springframework.stereotype.Service;

@Service
public class TodoServiceImpl implements TodoService {
    @Override
    public Todo createTodo(Todo todoRequest) {
        return Todo.builder().id(todoRequest.getId()).build();
    }
}
