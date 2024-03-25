package com.simplesystem.todo.service;

import java.util.UUID;

import com.simplesystem.todo.model.Todo;
import com.simplesystem.todo.model.TodoRequest;
import com.simplesystem.todo.model.TodoResponse;

public interface TodoService {
    TodoResponse createTodo(TodoRequest todoItem);

    Todo getTodoById(UUID todoId) throws InvalidInputException;

    TodoResponse updateTodo(TodoRequest any);
}
