package com.main.todo.controller;

import com.main.todo.dto.TodoCreateResponseDTO;
import com.main.todo.dto.TodoUpdateResponseDTO;
import com.main.todo.mapper.TodoMapper;
import com.main.todo.model.Todo;
import com.main.todo.service.TodoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "v1/todos")
@RequiredArgsConstructor
public class TodoController {

    private final TodoService todoService;

    @PostMapping
    public ResponseEntity<TodoCreateResponseDTO> createTodo(@Valid @RequestBody Todo todo){
        Todo createdTodo = todoService.createTodo(todo);
        TodoCreateResponseDTO responseDTO = TodoMapper.toCreationDTO(createdTodo);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Todo>> getAllTodos(){
        List<Todo> todos = todoService.getAllTodos();
        return ResponseEntity.ok(todos);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Todo> getTodoById(@PathVariable UUID id){
        Todo todo = todoService.getTodoById(id);
        return ResponseEntity.ok(todo);
    }
    @PutMapping(value = "/{id}")
    public ResponseEntity<TodoUpdateResponseDTO> updateTodo(@PathVariable UUID id,
                                           @Valid @RequestBody Todo todoDetails){
        Todo updatedTodo = todoService.updateTodo(id, todoDetails);
        TodoUpdateResponseDTO responseDTO = TodoMapper.toUpdateDTO(updatedTodo);
        return ResponseEntity.ok(responseDTO);
    }
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteTodo(@PathVariable UUID id){
        todoService.deleteTodo(id);
        return ResponseEntity.noContent().build();
    }
}
