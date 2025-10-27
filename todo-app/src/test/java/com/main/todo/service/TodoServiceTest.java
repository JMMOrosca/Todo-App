package com.main.todo.service;

import com.main.todo.enums.Status;
import com.main.todo.exception.ResourceNotFoundException;
import com.main.todo.model.Todo;
import com.main.todo.repository.TodoRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class TodoServiceTest {

    @Mock
    private TodoRepo todoRepository;

    @InjectMocks
    private TodoServiceImpl todoService;

    private UUID testId;
    private Todo testTodo;

    @BeforeEach
    public void setUp() {
        testId = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();
        testTodo = Todo.builder()
                .id(testId)
                .title("Test Todo")
                .description("Test Description")
                .status(Status.PENDING)
                .createdAt(now)
                .updatedAt(now)
                .build();
    }

    @Test
    public void testCreateTodo_Success() {
        Todo newTodo = testTodo;
        when(todoRepository.save(any(Todo.class))).thenReturn(testTodo);
        Todo createdTodo = todoService.createTodo(newTodo);
        assertNotNull(createdTodo);
        assertEquals(testId, createdTodo.getId());
        assertEquals("Test Todo", createdTodo.getTitle());
        assertEquals("Test Description", createdTodo.getDescription());
        assertEquals(Status.PENDING, createdTodo.getStatus());
        assertNotNull(createdTodo.getCreatedAt());
        assertNotNull(createdTodo.getUpdatedAt());
        verify(todoRepository, times(1)).save(any(Todo.class));
    }

    @Test
    public void testGetAllTodos_Success() {
        Todo todo1 = testTodo;
        todo1.setId(UUID.randomUUID());
        Todo todo2 = Todo.builder()
                .title("Todo 2")
                .description("Description 2")
                .status(Status.COMPLETED)
                .build();
        todo2.setId(UUID.randomUUID());
        List<Todo> expectedTodos = Arrays.asList(todo1, todo2);
        when(todoRepository.findAll()).thenReturn(expectedTodos);
        List<Todo> actualTodos = todoService.getAllTodos();
        assertNotNull(actualTodos);
        assertEquals(2, actualTodos.size());
        assertEquals("Test Todo", actualTodos.get(0).getTitle());
        assertEquals("Todo 2", actualTodos.get(1).getTitle());
        assertEquals(Status.PENDING, actualTodos.get(0).getStatus());
        assertEquals(Status.COMPLETED, actualTodos.get(1).getStatus());
        verify(todoRepository, times(1)).findAll();
    }

    @Test
    public void testGetAllTodos_EmptyList() {
        when(todoRepository.findAll()).thenReturn(Arrays.asList());
        List<Todo> actualTodos = todoService.getAllTodos();
        assertNotNull(actualTodos);
        assertTrue(actualTodos.isEmpty());
        verify(todoRepository, times(1)).findAll();
    }

    @Test
    public void testGetTodoById_Success() {
        when(todoRepository.findById(testId)).thenReturn(Optional.of(testTodo));
        Todo foundTodo = todoService.getTodoById(testId);
        assertNotNull(foundTodo);
        assertEquals(testId, foundTodo.getId());
        assertEquals("Test Todo", foundTodo.getTitle());
        assertEquals("Test Description", foundTodo.getDescription());
        assertEquals(Status.PENDING, foundTodo.getStatus());
        verify(todoRepository, times(1)).findById(testId);
    }

    @Test
    public void testGetTodoById_NotFound() {
        UUID randomId = UUID.randomUUID();
        when(todoRepository.findById(randomId)).thenReturn(Optional.empty());
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> todoService.getTodoById(randomId)
        );
        assertTrue(exception.getMessage().contains("Todo not found with id: " + randomId));
        verify(todoRepository, times(1)).findById(randomId);
    }

    @Test
    public void testUpdateTodo_Success() {
        Todo updatedDetails = Todo.builder()
                .title("Updated Title")
                .description("Updated Description")
                .status(Status.COMPLETED)
                .build();
        when(todoRepository.findById(testId)).thenReturn(Optional.of(testTodo));
        when(todoRepository.save(any(Todo.class))).thenReturn(testTodo);
        Todo updatedTodo = todoService.updateTodo(testId, updatedDetails);
        assertNotNull(updatedTodo);
        assertEquals(testId, updatedTodo.getId());
        verify(todoRepository, times(1)).findById(testId);
        verify(todoRepository, times(1)).save(any(Todo.class));
    }

    @Test
    public void testUpdateTodo_NotFound() {
        UUID randomId = UUID.randomUUID();
        Todo updatedDetails = Todo.builder()
                .title("Updated Title")
                .description("Updated Description")
                .status(Status.COMPLETED)
                .build();
        when(todoRepository.findById(randomId)).thenReturn(Optional.empty());
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> todoService.updateTodo(randomId, updatedDetails)
        );
        assertTrue(exception.getMessage().contains("Todo not found with id: " + randomId));
        verify(todoRepository, times(1)).findById(randomId);
        verify(todoRepository, never()).save(any(Todo.class));
    }

    @Test
    public void testUpdateTodo_UpdatesAllFields() {
        Todo updatedDetails = Todo.builder()
                .title("Updated Title")
                .description("Updated Description")
                .status(Status.COMPLETED)
                .build();
        Todo existingTodo = Todo.builder()
                .title("Old Title")
                .description("Old Description")
                .status(Status.PENDING)
                .build();
        existingTodo.setId(testId);
        when(todoRepository.findById(testId)).thenReturn(Optional.of(existingTodo));
        when(todoRepository.save(any(Todo.class))).thenAnswer(invocation -> invocation.getArgument(0));
        Todo result = todoService.updateTodo(testId, updatedDetails);
        assertEquals("Updated Title", result.getTitle());
        assertEquals("Updated Description", result.getDescription());
        assertEquals(Status.COMPLETED, result.getStatus());
        verify(todoRepository, times(1)).findById(testId);
        verify(todoRepository, times(1)).save(existingTodo);
    }

    @Test
    public void testDeleteTodo_Success() {
        when(todoRepository.existsById(testId)).thenReturn(true);
        doNothing().when(todoRepository).deleteById(testId);
        todoService.deleteTodo(testId);
        verify(todoRepository, times(1)).existsById(testId);
        verify(todoRepository, times(1)).deleteById(testId);
    }

    @Test
    public void testDeleteTodo_NotFound() {
        UUID randomId = UUID.randomUUID();
        when(todoRepository.existsById(randomId)).thenReturn(false);
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> todoService.deleteTodo(randomId)
        );
        assertTrue(exception.getMessage().contains("Todo not found with id: " + randomId));
        verify(todoRepository, times(1)).existsById(randomId);
        verify(todoRepository, never()).deleteById(any(UUID.class));
    }

    @Test
    public void testCreateTodo_WithNullStatus_DefaultsToPending() {
        Todo newTodo = Todo.builder()
                .title("New Todo")
                .description("Description")
                .status(null)
                .build();
        Todo savedTodo = Todo.builder()
                .title("New Todo")
                .description("Description")
                .status(Status.PENDING)
                .build();
        savedTodo.setId(UUID.randomUUID());
        when(todoRepository.save(any(Todo.class))).thenReturn(savedTodo);
        Todo result = todoService.createTodo(newTodo);
        assertNotNull(result);
        assertEquals(Status.PENDING, result.getStatus());
        verify(todoRepository, times(1)).save(any(Todo.class));
    }
}

