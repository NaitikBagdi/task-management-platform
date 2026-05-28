package com.tmp.taskservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StatusTransitionRequest {
  
	@NotBlank(message = "Status is required")
    private String status; // TODO, IN_PROGRESS, DONE
}