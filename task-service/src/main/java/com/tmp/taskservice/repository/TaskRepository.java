package com.tmp.taskservice.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tmp.taskservice.entity.Task;
import com.tmp.taskservice.enums.Status;

public interface TaskRepository extends JpaRepository<Task, UUID> {

	List<Task> findByProjectId(UUID projectId);

	List<Task> findByProjectIdAndStatus(UUID projectId, Status status);

	List<Task> findByAssigneeUserId(UUID assigneeUserId);

}