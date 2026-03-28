package org.dcoffice.cachar.controller;

import org.dcoffice.cachar.dto.ApiResponse;
import org.dcoffice.cachar.dto.TaskAssignRequest;
import org.dcoffice.cachar.dto.TaskCommentRequest;
import org.dcoffice.cachar.dto.TaskCreateRequest;
import org.dcoffice.cachar.dto.TaskPatchRequest;
import org.dcoffice.cachar.dto.TaskStatusUpdateRequest;
import org.dcoffice.cachar.dto.TaskUpdateRequest;
import org.dcoffice.cachar.entity.Department;
import org.dcoffice.cachar.entity.Officer;
import org.dcoffice.cachar.entity.Task;
import org.dcoffice.cachar.entity.TaskStatus;
import org.dcoffice.cachar.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/tasks")
@CrossOrigin(origins = "*")
public class TaskController {

    private static final Logger logger = LoggerFactory.getLogger(TaskController.class);

    @Autowired
    private TaskService taskService;

    @PostMapping
    public ResponseEntity<ApiResponse<Task>> createTask(@Valid @RequestBody TaskCreateRequest request,
                                                        Authentication authentication) {
        try {
            String actorId = authentication.getName();
            String actorName = getActorName(authentication);
            Task created = taskService.createTask(request, actorId, actorName);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("Task created successfully", created));
        } catch (Exception e) {
            logger.error("Failed to create task: {}", e.getMessage());
            return ResponseEntity.badRequest().body(ApiResponse.error("Failed to create task: " + e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Task>>> getTasks(
            @RequestParam(value = "status", required = false) TaskStatus status,
            @RequestParam(value = "department", required = false) Department department,
            @RequestParam(value = "assignedToId", required = false) String assignedToId,
            @RequestParam(value = "mine", defaultValue = "false") boolean mine,
            Authentication authentication) {
        try {
            String officerId = authentication.getName();
            String role = authentication.getAuthorities().iterator().next().getAuthority();
            List<Task> tasks = taskService.getTasks(officerId, role, status, department, assignedToId, mine);
            return ResponseEntity.ok(ApiResponse.success("Tasks retrieved successfully", tasks));
        } catch (Exception e) {
            logger.error("Failed to fetch tasks: {}", e.getMessage());
            return ResponseEntity.badRequest().body(ApiResponse.error("Failed to fetch tasks: " + e.getMessage()));
        }
    }

    @GetMapping("/overdue")
    public ResponseEntity<ApiResponse<List<Task>>> getOverdueTasks(Authentication authentication) {
        try {
            String officerId = authentication.getName();
            String role = authentication.getAuthorities().iterator().next().getAuthority();
            List<Task> tasks = taskService.getOverdueTasks(role, officerId);
            return ResponseEntity.ok(ApiResponse.success("Overdue tasks retrieved successfully", tasks));
        } catch (Exception e) {
            logger.error("Failed to fetch overdue tasks: {}", e.getMessage());
            return ResponseEntity.badRequest().body(ApiResponse.error("Failed to fetch overdue tasks: " + e.getMessage()));
        }
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<ApiResponse<Task>> getTaskById(@PathVariable String taskId,
                                                         Authentication authentication) {
        try {
            String officerId = authentication.getName();
            String role = authentication.getAuthorities().iterator().next().getAuthority();
            Task task = taskService.getTaskById(taskId, officerId, role);
            return ResponseEntity.ok(ApiResponse.success("Task retrieved successfully", task));
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            logger.error("Failed to fetch task {}: {}", taskId, e.getMessage());
            return ResponseEntity.badRequest().body(ApiResponse.error("Failed to fetch task: " + e.getMessage()));
        }
    }

    @GetMapping("/number/{taskNumber}")
    public ResponseEntity<ApiResponse<Task>> getTaskByNumber(@PathVariable String taskNumber,
                                                             Authentication authentication) {
        try {
            String officerId = authentication.getName();
            String role = authentication.getAuthorities().iterator().next().getAuthority();

            Optional<Task> taskOpt = taskService.findByTaskNumber(taskNumber);
            if (!taskOpt.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("Task not found with number: " + taskNumber));
            }

            Task task = taskService.getTaskById(taskOpt.get().getId(), officerId, role);
            return ResponseEntity.ok(ApiResponse.success("Task retrieved successfully", task));
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            logger.error("Failed to fetch task by number {}: {}", taskNumber, e.getMessage());
            return ResponseEntity.badRequest().body(ApiResponse.error("Failed to fetch task: " + e.getMessage()));
        }
    }

    @PutMapping("/{taskId}")
    public ResponseEntity<ApiResponse<Task>> updateTask(@PathVariable String taskId,
                                                        @Valid @RequestBody TaskUpdateRequest request,
                                                        Authentication authentication) {
        try {
            String actorId = authentication.getName();
            String actorName = getActorName(authentication);
            String role = authentication.getAuthorities().iterator().next().getAuthority();
            Task updated = taskService.updateTask(taskId, request, actorId, actorName, role);
            return ResponseEntity.ok(ApiResponse.success("Task updated successfully", updated));
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            logger.error("Failed to update task {}: {}", taskId, e.getMessage());
            return ResponseEntity.badRequest().body(ApiResponse.error("Failed to update task: " + e.getMessage()));
        }
    }

    @PatchMapping("/{taskId}")
    public ResponseEntity<ApiResponse<Task>> patchTask(@PathVariable String taskId,
                                                       @RequestBody TaskPatchRequest request,
                                                       Authentication authentication) {
        try {
            String actorId = authentication.getName();
            String actorName = getActorName(authentication);
            String role = authentication.getAuthorities().iterator().next().getAuthority();
            Task updated = taskService.patchTask(taskId, request, actorId, actorName, role);
            return ResponseEntity.ok(ApiResponse.success("Task patched successfully", updated));
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            logger.error("Failed to patch task {}: {}", taskId, e.getMessage());
            return ResponseEntity.badRequest().body(ApiResponse.error("Failed to patch task: " + e.getMessage()));
        }
    }

    @PatchMapping("/{taskId}/status")
    public ResponseEntity<ApiResponse<Task>> updateTaskStatus(@PathVariable String taskId,
                                                              @Valid @RequestBody TaskStatusUpdateRequest request,
                                                              Authentication authentication) {
        try {
            String actorId = authentication.getName();
            String actorName = getActorName(authentication);
            String role = authentication.getAuthorities().iterator().next().getAuthority();
            Task updated = taskService.updateTaskStatus(taskId, request, actorId, actorName, role);
            return ResponseEntity.ok(ApiResponse.success("Task status updated successfully", updated));
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            logger.error("Failed to update task status {}: {}", taskId, e.getMessage());
            return ResponseEntity.badRequest().body(ApiResponse.error("Failed to update task status: " + e.getMessage()));
        }
    }

    @PatchMapping("/{taskId}/assign")
    public ResponseEntity<ApiResponse<Task>> assignTask(@PathVariable String taskId,
                                                        @Valid @RequestBody TaskAssignRequest request,
                                                        Authentication authentication) {
        try {
            String actorId = authentication.getName();
            String actorName = getActorName(authentication);
            String role = authentication.getAuthorities().iterator().next().getAuthority();
            Task updated = taskService.assignTask(taskId, request, actorId, actorName, role);
            return ResponseEntity.ok(ApiResponse.success("Task assigned successfully", updated));
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            logger.error("Failed to assign task {}: {}", taskId, e.getMessage());
            return ResponseEntity.badRequest().body(ApiResponse.error("Failed to assign task: " + e.getMessage()));
        }
    }

    @PostMapping("/{taskId}/comments")
    public ResponseEntity<ApiResponse<Task>> addComment(@PathVariable String taskId,
                                                        @Valid @RequestBody TaskCommentRequest request,
                                                        Authentication authentication) {
        try {
            String actorId = authentication.getName();
            String actorName = getActorName(authentication);
            String role = authentication.getAuthorities().iterator().next().getAuthority();
            Task updated = taskService.addComment(taskId, request, actorId, actorName, role);
            return ResponseEntity.ok(ApiResponse.success("Task comment added successfully", updated));
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            logger.error("Failed to add comment to task {}: {}", taskId, e.getMessage());
            return ResponseEntity.badRequest().body(ApiResponse.error("Failed to add comment: " + e.getMessage()));
        }
    }

    @GetMapping("/{taskId}/workflow")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getTaskWorkflow(@PathVariable String taskId,
                                                                             Authentication authentication) {
        try {
            String officerId = authentication.getName();
            String role = authentication.getAuthorities().iterator().next().getAuthority();
            Map<String, Object> workflow = taskService.getWorkflow(taskId, officerId, role);
            return ResponseEntity.ok(ApiResponse.success("Task workflow retrieved successfully", workflow));
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            logger.error("Failed to fetch workflow for task {}: {}", taskId, e.getMessage());
            return ResponseEntity.badRequest().body(ApiResponse.error("Failed to fetch workflow: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<ApiResponse<Void>> deleteTask(@PathVariable String taskId,
                                                        Authentication authentication) {
        try {
            String actorId = authentication.getName();
            String role = authentication.getAuthorities().iterator().next().getAuthority();
            taskService.deleteTask(taskId, actorId, role);
            return ResponseEntity.ok(ApiResponse.success("Task deleted successfully"));
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            logger.error("Failed to delete task {}: {}", taskId, e.getMessage());
            return ResponseEntity.badRequest().body(ApiResponse.error("Failed to delete task: " + e.getMessage()));
        }
    }

    private String getActorName(Authentication authentication) {
        if (authentication != null && authentication.getDetails() instanceof Officer) {
            Officer officer = (Officer) authentication.getDetails();
            if (officer.getName() != null && !officer.getName().trim().isEmpty()) {
                return officer.getName().trim();
            }
        }
        return "Officer";
    }
}
