package com.main.todo.service;

import com.main.todo.model.Todo;

import java.util.List;
import java.util.UUID;

public interface TodoService {

    public Todo createTodo(Todo todo);
    public List<Todo> getAllTodos();
    public Todo getTodoById(UUID id);
    public Todo updateTodo(UUID id, Todo todoDetails);
    public void deleteTodo(UUID id);
}
