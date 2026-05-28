package com.tmp.authservice.service.serviceImpl;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.tmp.authservice.config.JwtTokenProvider;
import com.tmp.authservice.dto.request.LoginRequest;
import com.tmp.authservice.dto.request.RegisterRequest;
import com.tmp.authservice.dto.response.AuthResponse;
import com.tmp.authservice.dto.response.UserResponse;
import com.tmp.authservice.entity.User;
import com.tmp.authservice.enums.Role;
import com.tmp.authservice.repository.UserRepository;
import com.tmp.authservice.service.AuthService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceServiceImpl implements AuthService {

	private final UserRepository userRepository;
	private final BCryptPasswordEncoder passwordEncoder;
	private final JwtTokenProvider tokenProvider;

	@Override
	public UserResponse registerUser(RegisterRequest request) {
		if (userRepository.existsByEmail(request.getEmail())) {
			throw new RuntimeException("Email already registered!");
		}

		Role userRole = Role.USER;
		if (request.getRole() != null && request.getRole().equalsIgnoreCase("ADMIN")) {
			userRole = Role.ADMIN;
		}

		// Lombok Builder usage
		User user = User.builder().email(request.getEmail()).passwordHash(passwordEncoder.encode(request.getPassword()))
				.fullName(request.getFullName()).role(userRole).build();

		User savedUser = userRepository.save(user);

		return UserResponse.builder().id(savedUser.getId()).email(savedUser.getEmail())
				.fullName(savedUser.getFullName()).role(savedUser.getRole().name()).isActive(savedUser.isActive())
				.build();
	}

	@Override
	public AuthResponse loginUser(LoginRequest request) {
		User user = userRepository.findByEmail(request.getEmail())
				.orElseThrow(() -> new RuntimeException("Invalid email or password"));

		if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
			throw new RuntimeException("Invalid email or password");
		}

		if (!user.isActive()) {
			throw new RuntimeException("User account is inactive");
		}

		String token = tokenProvider.generateToken(user.getId(), user.getEmail(), user.getRole().name());
		return new AuthResponse(token);
	}

	@Override
	public UserResponse getUserById(UUID id) {
		User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
		return UserResponse.builder().id(user.getId()).email(user.getEmail()).fullName(user.getFullName()).role(user.getRole().name()).isActive(user.isActive()).build();
	}

	@Override
	public List<UserResponse> getAllUsers() {
		return userRepository.findAll().stream().map(user -> UserResponse.builder().id(user.getId()).email(user.getEmail()).fullName(user.getFullName()).role(user.getRole().name()).isActive(user.isActive()).build()).collect(Collectors.toList());
	}
}
