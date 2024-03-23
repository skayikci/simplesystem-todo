package com.simplesystem.todo.repository;

import com.simplesystem.todo.model.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TodoRepository extends JpaRepository<Todo, String>, JpaSpecificationExecutor<Todo> {
}
