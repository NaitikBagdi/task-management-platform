package com.tmp.taskservice.service;

import java.util.List;
import java.util.UUID;

import com.tmp.taskservice.dto.request.ProjectRequest;
import com.tmp.taskservice.dto.request.TaskRequest;
import com.tmp.taskservice.entity.Project;
import com.tmp.taskservice.entity.Task;

public interface ProjectTaskService {

	public Project createProject(ProjectRequest request, String ownerId);

	public List<Project> getAllProjects();

	public Project getProjectById(UUID id);

	public Project updateProject(UUID id, ProjectRequest request);

	public void deleteProject(UUID id);

	public Task createTask(UUID projectId, TaskRequest request);

	public List<Task> getTasksByProject(UUID projectId, String statusFilter);

	public Task getTaskById(UUID taskId);

	public Task updateTask(UUID taskId, TaskRequest request);

	public Task assignTask(UUID taskId, UUID assigneeUserId);

	public Task transitionStatus(UUID taskId, String newStatusStr);

	public void deleteTask(UUID taskId);

	public List<Task> getMyTasks(String currentUserId);
}
