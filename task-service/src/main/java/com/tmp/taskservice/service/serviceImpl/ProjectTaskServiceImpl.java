package com.tmp.taskservice.service.serviceImpl;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.tmp.taskservice.dto.request.ProjectRequest;
import com.tmp.taskservice.dto.request.TaskRequest;
import com.tmp.taskservice.entity.Project;
import com.tmp.taskservice.entity.Task;
import com.tmp.taskservice.enums.Priority;
import com.tmp.taskservice.enums.Status;
import com.tmp.taskservice.repository.ProjectRepository;
import com.tmp.taskservice.repository.TaskRepository;
import com.tmp.taskservice.service.AuthServiceClient;
import com.tmp.taskservice.service.ProjectTaskService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProjectTaskServiceImpl implements ProjectTaskService {

	private final ProjectRepository projectRepository;
	private final TaskRepository taskRepository;
	private final AuthServiceClient authServiceClient;

	// --- PROJECT METHODS ---
	@Override
	public Project createProject(ProjectRequest request, String ownerId) {
		Project project = Project.builder().name(request.getName()).description(request.getDescription())
				.ownerUserId(UUID.fromString(ownerId)).build();
		return projectRepository.save(project);
	}

	@Override
	public List<Project> getAllProjects() {
		return projectRepository.findAll();
	}

	@Override
	public Project getProjectById(UUID id) {
		return projectRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Project not found with status 404"));
	}

	@Override
	public Project updateProject(UUID id, ProjectRequest request) {
		Project project = getProjectById(id);
		project.setName(request.getName());
		project.setDescription(request.getDescription());
		return projectRepository.save(project);
	}

	@Override
	public void deleteProject(UUID id) {
		Project project = getProjectById(id);
		projectRepository.delete(project);
	}

	// --- TASK METHODS ---
	@Override
	public Task createTask(UUID projectId, TaskRequest request) {
		// Confirm project exists
		getProjectById(projectId);

		Priority taskPriority = Priority.LOW;
		if (request.getPriority() != null) {
			taskPriority = Priority.valueOf(request.getPriority().toUpperCase());
		}

		Task task = Task.builder().projectId(projectId).title(request.getTitle()).description(request.getDescription())
				.priority(taskPriority).status(Status.TODO).build();

		return taskRepository.save(task);
	}

	@Override
	public List<Task> getTasksByProject(UUID projectId, String statusFilter) {
		getProjectById(projectId);
		if (statusFilter != null && !statusFilter.isBlank()) {
			Status status = Status.valueOf(statusFilter.toUpperCase());
			return taskRepository.findByProjectIdAndStatus(projectId, status);
		}
		return taskRepository.findByProjectId(projectId);
	}

	@Override
	public Task getTaskById(UUID taskId) {
		return taskRepository.findById(taskId)
				.orElseThrow(() -> new RuntimeException("Task not found with status 404"));
	}

	@Override
	public Task updateTask(UUID taskId, TaskRequest request) {
		Task task = getTaskById(taskId);
		task.setTitle(request.getTitle());
		task.setDescription(request.getDescription());
		if (request.getPriority() != null) {
			task.setPriority(Priority.valueOf(request.getPriority().toUpperCase()));
		}
		return taskRepository.save(task);
	}

	@Override
	public Task assignTask(UUID projectId, UUID taskId, UUID assigneeUserId) {
		Task task = getTaskById(taskId);

        // 2. Pure Business Check: Path and resources verification
        if (!task.getProjectId().equals(projectId)) {
            throw new IllegalArgumentException("Cross-resource mismatch: Task does not belong to the specified project");
        }

        // 3. Clean Cross-Service Check via Client Gateway (Requirement 4.3)
        boolean isUserValidAndActive = authServiceClient.isUserActive(assigneeUserId);
        if (!isUserValidAndActive) {
            throw new IllegalArgumentException("Assignment failed: The requested assignee user account is inactive");
        }

        // 4. Persistence State Update
        task.setAssigneeUserId(assigneeUserId);
        return taskRepository.save(task);
	}
	
	@Override
	public Task transitionStatus(UUID taskId, String newStatusStr) {
		Task task = getTaskById(taskId);
		Status currentStatus = task.getStatus();
		Status newStatus = Status.valueOf(newStatusStr.toUpperCase());

		// Strict Status Transition Specification (Requirement 4.3 Rules)
		if (currentStatus == Status.TODO && newStatus != Status.IN_PROGRESS) {
			throw new IllegalArgumentException("Invalid status transition: TODO can only move to IN_PROGRESS");
		}
		if (currentStatus == Status.IN_PROGRESS && newStatus != Status.DONE) {
			throw new IllegalArgumentException("Invalid status transition: IN_PROGRESS can only move to DONE");
		}
		if (currentStatus == Status.DONE && newStatus != Status.IN_PROGRESS) {
			throw new IllegalArgumentException("Invalid status transition: DONE can only move back to IN_PROGRESS");
		}

		task.setStatus(newStatus);
		return taskRepository.save(task);
	}

	@Override
	public void deleteTask(UUID taskId) {
		Task task = getTaskById(taskId);
		taskRepository.delete(task);
	}

	@Override
	public List<Task> getMyTasks(String currentUserId) {
		return taskRepository.findByAssigneeUserId(UUID.fromString(currentUserId));
	}
}
