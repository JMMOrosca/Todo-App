package com.main.todo.repository;

import com.main.todo.model.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TodoRepo extends JpaRepository<Todo, UUID> {
}
