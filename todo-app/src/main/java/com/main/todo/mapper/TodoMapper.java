package com.main.todo.mapper;

import com.main.todo.dto.TodoCreateResponseDTO;
import com.main.todo.dto.TodoUpdateResponseDTO;
import com.main.todo.model.Todo;

public class TodoMapper {

    public static TodoCreateResponseDTO toCreationDTO(Todo todo){
        TodoCreateResponseDTO dto = new TodoCreateResponseDTO();
        dto.setId(todo.getId());
        dto.setTitle(todo.getTitle());
        dto.setDescription(todo.getDescription());
        dto.setStatus(todo.getStatus());
        dto.setCreatedAt(todo.getCreatedAt());
        return dto;
    }

    public static TodoUpdateResponseDTO toUpdateDTO(Todo todo){
        TodoUpdateResponseDTO dto = new TodoUpdateResponseDTO();
        dto.setId(todo.getId());
        dto.setTitle(todo.getTitle());
        dto.setDescription(todo.getDescription());
        dto.setStatus(todo.getStatus());
        dto.setUpdatedAt(todo.getCreatedAt());
        return dto;
    }
}
