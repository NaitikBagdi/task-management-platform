package com.tmp.authservice.service;

import java.util.List;
import java.util.UUID;

import com.tmp.authservice.dto.request.LoginRequest;
import com.tmp.authservice.dto.request.RegisterRequest;
import com.tmp.authservice.dto.response.AuthResponse;
import com.tmp.authservice.dto.response.UserResponse;

public interface AuthService {

	public UserResponse registerUser(RegisterRequest request);

	public AuthResponse loginUser(LoginRequest request);

	public UserResponse getUserById(UUID id);

	public List<UserResponse> getAllUsers();
}
