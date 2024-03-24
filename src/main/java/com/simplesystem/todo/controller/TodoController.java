package com.simplesystem.todo.controller;

import java.time.LocalDateTime;
import java.util.UUID;

import com.simplesystem.todo.controller.exception.InvalidEntityException;
import com.simplesystem.todo.model.Todo;
import com.simplesystem.todo.model.TodoStatus;
import com.simplesystem.todo.service.TodoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
    public ResponseEntity createTodoItemWithBody(@RequestBody Todo todoRequest) throws InvalidEntityException {
        if (todoRequest.getCreatedDate() == null) {
            LocalDateTime createdDate = LocalDateTime.now();
            todoRequest.setCreatedDate(createdDate);
        }
        if (todoRequest.getDueDate() != null && todoRequest.getDueDate().isBefore(todoRequest.getCreatedDate())) {
            throw new InvalidEntityException("Invalid entity, please check due date");
        }
        if (TodoStatus.PAST_DUE.equals(todoRequest.getStatus())) {
            throw new InvalidEntityException("Invalid entity, please check input status");
        }
        var createdTodoItem = todoService.createTodo(todoRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTodoItem);
    }

    @GetMapping("/{todoId}")
    public ResponseEntity getTodoItemById(@PathVariable String todoId) {
        var todoItemReturned = todoService.getTodoById(UUID.fromString(todoId));
        return ResponseEntity.status(HttpStatus.OK).body(todoItemReturned);
    }

}
