package com.simplesystem.todo.controller;

import java.util.UUID;

import com.simplesystem.todo.model.Todo;
import com.simplesystem.todo.service.TodoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/todo")
@RequiredArgsConstructor
public class TodoController {
    private final TodoService todoService;

    @PostMapping
    public ResponseEntity createTodoItemWithBody(@RequestBody Todo todoRequest) {
        var createdTodoItem = todoService.createTodo(todoRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTodoItem);
    }

    @GetMapping("/{todoId}")
    public ResponseEntity getTodoItemById(@PathVariable UUID todoId) {
        var todoItemReturned = todoService.getTodoById(todoId);
        return ResponseEntity.status(HttpStatus.OK).body(todoItemReturned);
    }

}
