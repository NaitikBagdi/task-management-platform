package com.tmp.taskservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AssignUserRequest {
    @NotBlank(message = "Assignee user ID is required")
    private String assigneeUserId;
}