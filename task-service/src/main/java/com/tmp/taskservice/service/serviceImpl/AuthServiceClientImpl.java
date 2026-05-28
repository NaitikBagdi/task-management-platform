package com.tmp.taskservice.service.serviceImpl;

import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.tmp.taskservice.service.AuthServiceClient;

import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AuthServiceClientImpl implements AuthServiceClient {

	private final RestTemplate authServiceRestTemplate;

	@Override
	public boolean isUserActive(UUID userId) {
        String endpoint = "/api/v1/users/" + userId;
        try {
            Map<String, Object> response = authServiceRestTemplate.getForObject(endpoint, Map.class);
            if (response != null && response.containsKey("active")) {
                return (boolean) response.get("active");
            }
            return false;
        } catch (HttpClientErrorException.NotFound e) {
            throw new IllegalArgumentException("User verification failed: Assigned user ID not found with status 404");
        } catch (Exception e) {
            throw new RuntimeException("External Identity service communication failure");
        }
    }

}
