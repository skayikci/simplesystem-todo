package com.simplesystem.todo.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.simplesystem.todo.model.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TodoRepository extends JpaRepository<Todo, UUID>, JpaSpecificationExecutor<Todo> {
    List<Todo> findByDueDateBefore(LocalDateTime currentDate);
}
