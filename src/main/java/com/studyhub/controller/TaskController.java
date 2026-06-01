package com.studyhub.controller;

import com.studyhub.annotation.RequireRole;
import com.studyhub.config.RoleConstants;
import com.studyhub.model.Task;
import com.studyhub.service.TaskService;
import com.studyhub.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/hubs/{hubId}/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @PostMapping
    @RequireRole({RoleConstants.ROLE_CREATOR, RoleConstants.ROLE_AIDE})
    public ResponseEntity<Task> createTask(@PathVariable String hubId, @RequestBody Task task) {
        try {
            String uid = SecurityUtil.getCurrentUserUid();
            if (uid == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            task.setHubId(hubId);
            task.setCreatorId(uid);
            Task created = taskService.createTask(task);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping
    public ResponseEntity<List<Task>> getTasks(@PathVariable String hubId) {
        try {
            List<Task> tasks = taskService.getTasksForHub(hubId);
            return ResponseEntity.ok(tasks);
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<Task> getTask(@PathVariable String hubId, @PathVariable String taskId) {
        try {
            Task task = taskService.getTaskById(taskId);
            if (task == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            return ResponseEntity.ok(task);
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{taskId}")
    @RequireRole({RoleConstants.ROLE_CREATOR, RoleConstants.ROLE_AIDE, RoleConstants.ROLE_MEMBER})
    public ResponseEntity<Task> updateTask(@PathVariable String hubId, @PathVariable String taskId, @RequestBody Task taskUpdates) {
        try {
            Task updated = taskService.updateTask(taskId, taskUpdates);
            if (updated == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            return ResponseEntity.ok(updated);
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{taskId}")
    @RequireRole({RoleConstants.ROLE_CREATOR, RoleConstants.ROLE_AIDE})
    public ResponseEntity<Void> deleteTask(@PathVariable String hubId, @PathVariable String taskId) {
        try {
            taskService.deleteTask(taskId);
            return ResponseEntity.noContent().build();
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
