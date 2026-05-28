package com.tmp.taskservice.exception;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ErrorResponse {

	private LocalDateTime timestamp;
	private Integer status;
	private String message;
	private String path;
}
