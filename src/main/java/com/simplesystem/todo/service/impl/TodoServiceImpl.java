package com.simplesystem.todo.service.impl;

import java.util.UUID;

import com.simplesystem.todo.model.Todo;
import com.simplesystem.todo.model.TodoFilterResponse;
import com.simplesystem.todo.model.TodoRequest;
import com.simplesystem.todo.model.TodoResponse;
import com.simplesystem.todo.model.TodoStatus;
import com.simplesystem.todo.repository.TodoRepository;
import com.simplesystem.todo.service.InvalidInputException;
import com.simplesystem.todo.service.TodoService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class TodoServiceImpl implements TodoService {

    private final TodoRepository todoRepository;
    private final TodoMapper todoMapper;

    @Override
    @Modifying
    @Transactional
    public TodoResponse createTodo(TodoRequest todoRequest) {
        Todo todoToCreate = todoMapper.mapRequestToEntity(todoRequest);
        Todo createdTodo = todoRepository.save(todoToCreate);
        return todoMapper.mapEntityToResponse(createdTodo);
    }

    @Override
    public TodoResponse getTodoById(UUID todoId) {
        if (todoId == null) {
            throw new InvalidInputException("Id of the todo item is not provided or invalid");
        }
        Todo foundTodo = todoRepository.getReferenceById(todoId);
        return todoMapper.mapEntityToResponse(foundTodo);
    }

    @Override
    @Modifying
    @Transactional
    public TodoResponse updateTodo(TodoRequest todoRequest) {
        var requestedTodoItem = todoRepository.findById(todoRequest.id());
        if (requestedTodoItem.isPresent()) {
            var updateTodoItem = requestedTodoItem.get();
            todoMapper.updateTodoFromRequest(updateTodoItem, todoRequest);
            var updateResult = todoRepository.save(updateTodoItem);
            return todoMapper.mapEntityToResponse(updateResult);
        } else {
            log.error("Error while updating the todo item, please check the id: {}", todoRequest.id());
            throw new EntityNotFoundException("Given entity with id not found");
        }
    }

    @Override
    public TodoFilterResponse filterByStatus(TodoStatus todoStatus) {
        var filteredTodoList = todoRepository.findByStatus(todoStatus);
        TodoFilterResponse todoFilterResponse = new TodoFilterResponse();
        todoFilterResponse.setTodoResponseList(todoMapper.mapEntityListToResponse(filteredTodoList));
        return todoFilterResponse;
    }
}
