package com.simplesystem.todo.service.impl;

import java.util.List;

import com.simplesystem.todo.model.Todo;
import com.simplesystem.todo.model.TodoFilterResponse;
import com.simplesystem.todo.model.TodoRequest;
import com.simplesystem.todo.model.TodoResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface TodoMapper {
    @Mapping(target = "status", defaultValue = "NOT_DONE")
    @Mapping(target = "createdDate", defaultExpression = "java(java.time.LocalDateTime.now())", dateFormat = "ddMMyyyy HH:mm:ss")
    Todo mapRequestToEntity(TodoRequest todoRequest);

    @Mapping(target = "status", defaultValue = "NOT_DONE")
    @Mapping(target = "createdDate", defaultExpression = "java(java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern(\"ddMMyyyy HH:mm:ss\")))")
    @Mapping(target = "dueDate", dateFormat = "ddMMyyyy HH:mm:ss")
    @Mapping(target = "doneDate", dateFormat = "ddMMyyyy HH:mm:ss")
    TodoResponse mapEntityToResponse(Todo todoEntity);

    void updateTodoFromRequest(@MappingTarget Todo requestedTodoItem, TodoRequest todoRequest);

    List<TodoResponse> mapEntityListToResponse(List<Todo> filteredTodoList);
}
