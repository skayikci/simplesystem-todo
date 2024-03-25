package com.simplesystem.todo.service.impl;

import com.simplesystem.todo.model.Todo;
import com.simplesystem.todo.model.TodoRequest;
import com.simplesystem.todo.model.TodoResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TodoMapper {
    @Mapping(target = "status", constant = "NOT_DONE")
    @Mapping(target = "createdDate", defaultExpression = "java(LocalDateTime.now())", dateFormat = "ddMMyyyy HH:mm:ss")
    Todo mapRequestToEntity(TodoRequest todoRequest);

    @Mapping(target = "status", constant = "NOT_DONE")
    @Mapping(target = "createdDate", defaultExpression = "java(LocalDateTime.now())", dateFormat = "ddMMyyyy HH:mm:ss")
    TodoResponse mapEntityToResponse(Todo todoEntity);
}
