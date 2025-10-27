package com.main.todo.service;

import com.main.todo.enums.Status;
import com.main.todo.exception.ResourceNotFoundException;
import com.main.todo.model.Todo;
import com.main.todo.repository.TodoRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class TodoServiceImpl implements TodoService{

    private final TodoRepo todoRepo;

    @Override
    public Todo createTodo(Todo todo) {
        todo.setStatus(Status.PENDING);
        return todoRepo.save(todo);
    }

    @Override
    public List<Todo> getAllTodos() {
        return todoRepo.findAll();
    }

    @Override
    public Todo getTodoById(UUID id) {
        return todoRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Todo not found with id: " + id));
    }

    @Override
    public Todo updateTodo(UUID id, Todo todoDetails) {
        Todo existingTodo = getTodoById(id);

        existingTodo.setTitle(todoDetails.getTitle());
        existingTodo.setDescription(todoDetails.getDescription());
        existingTodo.setStatus(todoDetails.getStatus());
        return todoRepo.save(existingTodo);
    }

    @Override
    public void deleteTodo(UUID id) {
        if(!todoRepo.existsById(id)){
            throw new ResourceNotFoundException("Todo not found with id: " + id);
        }
        todoRepo.deleteById(id);
    }
}
