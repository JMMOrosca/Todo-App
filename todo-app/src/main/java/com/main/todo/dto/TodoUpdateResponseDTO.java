package com.main.todo.dto;

import com.main.todo.enums.Status;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
public class TodoUpdateResponseDTO {
    private UUID id;
    private String title;
    private String description;
    private Status status;
    private LocalDateTime updatedAt;
}
