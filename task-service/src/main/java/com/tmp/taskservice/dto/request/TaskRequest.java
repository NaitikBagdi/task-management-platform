package com.tmp.taskservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskRequest {
    @NotBlank(message = "Task title is required")
    private String title;
    private String description;
    private String priority; // LOW, MEDIUM, HIGH
}