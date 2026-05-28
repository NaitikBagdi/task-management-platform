package com.tmp.taskservice.service;

import java.util.UUID;

public interface AuthServiceClient {

	boolean isUserActive(UUID userId);

}
