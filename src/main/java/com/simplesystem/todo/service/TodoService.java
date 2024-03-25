package com.simplesystem.todo.service;

import java.util.UUID;

import com.simplesystem.todo.model.Todo;

public interface TodoService {
    Todo createTodo(Todo todoItem);

    Todo getTodoById(UUID todoId) throws InvalidInputException;

    Todo updateTodo(Todo any);
}
