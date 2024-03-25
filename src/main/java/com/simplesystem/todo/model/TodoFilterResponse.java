package com.simplesystem.todo.model;

import java.util.List;

import lombok.Data;

@Data
public class TodoFilterResponse {
    List<TodoResponse> todoResponseList;
}
