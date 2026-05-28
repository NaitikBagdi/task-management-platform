package com.tmp.taskservice.service.serviceImpl;

import java.util.Map;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import com.tmp.taskservice.service.AuthServiceClient;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AuthServiceClientImpl implements AuthServiceClient {

	private final RestTemplate authServiceRestTemplate;

	@Override
	public boolean isUserActive(UUID userId) {
        String endpoint = "/api/v1/users/" + userId;
        try {
            ResponseEntity<Map> responseEntity = authServiceRestTemplate.getForEntity(endpoint, Map.class);
            if (responseEntity.getStatusCode().isError()) {
                throw new IllegalArgumentException("User verification failed: Assigned user ID does not exist in identity database.");
            }
            if (responseEntity != null && responseEntity.getBody().containsKey("active")) {
                return (boolean) responseEntity.getBody().get("active");
            }
            return false;
        } catch (HttpStatusCodeException e) {
            throw new IllegalArgumentException("User verification failed: Assigned user ID not found with status 404");
        } catch (Exception e) {
            throw new RuntimeException("External Identity service communication failure");
        }
    }

}
