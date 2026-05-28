package com.tmp.taskservice.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tmp.taskservice.annotation.IsAdmin;
import com.tmp.taskservice.annotation.IsAdminOrUser;
import com.tmp.taskservice.dto.request.AssignUserRequest;
import com.tmp.taskservice.dto.request.ProjectRequest;
import com.tmp.taskservice.dto.request.StatusTransitionRequest;
import com.tmp.taskservice.dto.request.TaskRequest;
import com.tmp.taskservice.entity.Project;
import com.tmp.taskservice.entity.Task;
import com.tmp.taskservice.service.ProjectTaskService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ProjectTaskController {

	private final ProjectTaskService projectTaskService;

	@IsAdmin
    @PostMapping("/projects")
    public ResponseEntity<Project> createProject(@Valid @RequestBody ProjectRequest request, @AuthenticationPrincipal String userId) {
        return ResponseEntity.ok(projectTaskService.createProject(request, userId));
    }

    @IsAdminOrUser
    @GetMapping("/projects")
    public ResponseEntity<List<Project>> getAllProjects() {
        return ResponseEntity.ok(projectTaskService.getAllProjects());
    }

    @IsAdminOrUser
    @GetMapping("/projects/{id}")
    public ResponseEntity<Project> getProjectById(@PathVariable UUID id) {
        return ResponseEntity.ok(projectTaskService.getProjectById(id));
    }

    @IsAdmin
    @PutMapping("/projects/{id}")
    public ResponseEntity<Project> updateProject(@PathVariable UUID id, @Valid @RequestBody ProjectRequest request) {
        return ResponseEntity.ok(projectTaskService.updateProject(id, request));
    }

    @IsAdmin
    @DeleteMapping("/projects/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable UUID id) {
        projectTaskService.deleteProject(id);
        return ResponseEntity.noContent().build();
    }

    // --- TASKS ENDPOINTS ---
    
    @IsAdminOrUser
    @PostMapping("/projects/{pid}/tasks")
    public ResponseEntity<Task> createTask(@PathVariable UUID pid, @Valid @RequestBody TaskRequest request) {
        return ResponseEntity.ok(projectTaskService.createTask(pid, request));
    }

    @IsAdminOrUser
    @GetMapping("/projects/{pid}/tasks")
    public ResponseEntity<List<Task>> getTasksByProject(@PathVariable UUID pid, @RequestParam(required = false) String status) {
        return ResponseEntity.ok(projectTaskService.getTasksByProject(pid, status));
    }

    @IsAdminOrUser
    @GetMapping("/projects/{pid}/tasks/{tid}")
    public ResponseEntity<Task> getTaskById(@PathVariable UUID pid, @PathVariable UUID tid) {
        return ResponseEntity.ok(projectTaskService.getTaskById(tid));
    }

    @IsAdminOrUser
    @PutMapping("/projects/{pid}/tasks/{tid}")
    public ResponseEntity<Task> updateTask(@PathVariable UUID pid, @PathVariable UUID tid, @Valid @RequestBody TaskRequest request) {
        return ResponseEntity.ok(projectTaskService.updateTask(tid, request));
    }

    @IsAdmin
    @PatchMapping("/projects/{pid}/tasks/{tid}/assign")
    public ResponseEntity<Task> assignTask(@PathVariable UUID pid, @PathVariable UUID tid, @Valid @RequestBody AssignUserRequest request) {
        return ResponseEntity.ok(projectTaskService.assignTask(tid, UUID.fromString(request.getAssigneeUserId())));
    }

    @IsAdminOrUser
    @PatchMapping("/projects/{pid}/tasks/{tid}/status")
    public ResponseEntity<Task> transitionStatus(@PathVariable UUID pid, @PathVariable UUID tid, @Valid @RequestBody StatusTransitionRequest request) {
        return ResponseEntity.ok(projectTaskService.transitionStatus(tid, request.getStatus()));
    }

    @IsAdmin
    @DeleteMapping("/projects/{pid}/tasks/{tid}")
    public ResponseEntity<Void> deleteTask(@PathVariable UUID pid, @PathVariable UUID tid) {
        projectTaskService.deleteTask(tid);
        return ResponseEntity.noContent().build();
    }

    @IsAdminOrUser
    @GetMapping("/tasks/my-tasks")
    public ResponseEntity<List<Task>> getMyTasks(@AuthenticationPrincipal String userId) {
        return ResponseEntity.ok(projectTaskService.getMyTasks(userId));
    }

}