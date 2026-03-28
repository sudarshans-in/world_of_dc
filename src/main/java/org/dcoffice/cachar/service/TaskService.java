package org.dcoffice.cachar.service;

import org.dcoffice.cachar.dto.TaskAssignRequest;
import org.dcoffice.cachar.dto.TaskCommentRequest;
import org.dcoffice.cachar.dto.TaskCreateRequest;
import org.dcoffice.cachar.dto.TaskPatchRequest;
import org.dcoffice.cachar.dto.TaskStatusUpdateRequest;
import org.dcoffice.cachar.dto.TaskUpdateRequest;
import org.dcoffice.cachar.entity.Department;
import org.dcoffice.cachar.entity.Officer;
import org.dcoffice.cachar.entity.Priority;
import org.dcoffice.cachar.entity.Task;
import org.dcoffice.cachar.entity.TaskActivity;
import org.dcoffice.cachar.entity.TaskComment;
import org.dcoffice.cachar.entity.TaskScope;
import org.dcoffice.cachar.entity.TaskStatus;
import org.dcoffice.cachar.exception.TaskNotFoundException;
import org.dcoffice.cachar.repository.TaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class TaskService {

    private static final Logger logger = LoggerFactory.getLogger(TaskService.class);

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private CounterService counterService;

    @Autowired
    private OfficerService officerService;

    public Task createTask(TaskCreateRequest request, String actorId, String actorName) {
        Task task = new Task();
        task.setId("task-" + counterService.getNextSequence("taskId"));
        task.setTaskNumber(generateTaskNumber());
        task.setTitle(request.getTitle().trim());
        task.setDescription(request.getDescription().trim());
        task.setDepartment(request.getDepartment());
        task.setScope(request.getScope() != null ? request.getScope() : TaskScope.DEPARTMENT);
        task.setPriority(request.getPriority() != null ? request.getPriority() : Priority.MEDIUM);
        task.setCreatedById(actorId);
        task.setCreatedByName(actorName);
        task.setDueDate(request.getDueDate());
        task.setTags(request.getTags() != null ? request.getTags() : new ArrayList<>());

        if (request.getAssignedToId() != null && !request.getAssignedToId().trim().isEmpty()) {
            String assignedToId = request.getAssignedToId().trim();
            task.setAssignedToId(assignedToId);
            task.setAssignedToName(resolveOfficerName(assignedToId, request.getAssignedToName()));
            task.setStatus(TaskStatus.ASSIGNED);
        }

        addActivity(task, "TASK_CREATED", "Task created", actorId, actorName, null, task.getStatus());

        Task saved = taskRepository.save(task);
        logger.info("Task created: {} by {}", saved.getTaskNumber(), actorId);
        return saved;
    }

    public List<Task> getTasks(String currentOfficerId, String currentRole, TaskStatus status,
                               Department department, String assignedToId, boolean mineOnly) {

        List<Task> tasks;

        if (mineOnly) {
            tasks = mergeTaskLists(
                    taskRepository.findByCreatedById(currentOfficerId),
                    taskRepository.findByAssignedToId(currentOfficerId)
            );
        } else if (isAdminRole(currentRole)) {
            tasks = taskRepository.findAll();
        } else {
            tasks = mergeTaskLists(
                    taskRepository.findByCreatedById(currentOfficerId),
                    taskRepository.findByAssignedToId(currentOfficerId)
            );
        }

        return tasks.stream()
                .filter(task -> status == null || task.getStatus() == status)
                .filter(task -> department == null || task.getDepartment() == department)
                .filter(task -> assignedToId == null || assignedToId.trim().isEmpty() || assignedToId.equals(task.getAssignedToId()))
                .sorted((a, b) -> b.getUpdatedAt().compareTo(a.getUpdatedAt()))
                .collect(Collectors.toList());
    }

    public List<Task> getOverdueTasks(String currentRole, String currentOfficerId) {
        List<Task> overdue = taskRepository.findOverdueTasks(LocalDateTime.now());
        if (isAdminRole(currentRole)) {
            return overdue;
        }
        return overdue.stream()
                .filter(task -> currentOfficerId.equals(task.getCreatedById()) || currentOfficerId.equals(task.getAssignedToId()))
                .collect(Collectors.toList());
    }

    public Task getTaskById(String taskId, String currentOfficerId, String currentRole) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Task not found with ID: " + taskId));

        if (!canViewTask(task, currentOfficerId, currentRole)) {
            throw new SecurityException("Access denied: You do not have permission to view this task");
        }

        return task;
    }

    public Task updateTask(String taskId, TaskUpdateRequest request, String actorId, String actorName, String role) {
        Task task = getTaskById(taskId, actorId, role);
        ensureCanEdit(task, actorId, role);

        task.setTitle(request.getTitle().trim());
        task.setDescription(request.getDescription().trim());
        task.setDepartment(request.getDepartment());
        task.setScope(request.getScope());
        task.setPriority(request.getPriority());
        task.setDueDate(request.getDueDate());
        task.setTags(request.getTags() != null ? request.getTags() : Collections.emptyList());

        if (request.getAssignedToId() != null && !request.getAssignedToId().trim().isEmpty()) {
            task.setAssignedToId(request.getAssignedToId().trim());
            task.setAssignedToName(resolveOfficerName(request.getAssignedToId().trim(), request.getAssignedToName()));
            if (task.getStatus() == TaskStatus.CREATED) {
                task.setStatus(TaskStatus.ASSIGNED);
            }
        } else {
            task.setAssignedToId(null);
            task.setAssignedToName(null);
        }

        addActivity(task, "TASK_UPDATED", "Task updated via PUT", actorId, actorName, null, task.getStatus());
        return taskRepository.save(task);
    }

    public Task patchTask(String taskId, TaskPatchRequest request, String actorId, String actorName, String role) {
        Task task = getTaskById(taskId, actorId, role);
        ensureCanEdit(task, actorId, role);

        if (request.getTitle() != null && !request.getTitle().trim().isEmpty()) {
            task.setTitle(request.getTitle().trim());
        }
        if (request.getDescription() != null && !request.getDescription().trim().isEmpty()) {
            task.setDescription(request.getDescription().trim());
        }
        if (request.getDepartment() != null) {
            task.setDepartment(request.getDepartment());
        }
        if (request.getScope() != null) {
            task.setScope(request.getScope());
        }
        if (request.getPriority() != null) {
            task.setPriority(request.getPriority());
        }
        if (request.getDueDate() != null) {
            task.setDueDate(request.getDueDate());
        }
        if (request.getTags() != null) {
            task.setTags(request.getTags());
        }

        TaskStatus previousStatus = task.getStatus();
        if (request.getStatus() != null) {
            if (!TaskStatus.isValidTransition(task.getStatus(), request.getStatus())) {
                throw new IllegalArgumentException("Invalid status transition: " + task.getStatus() + " -> " + request.getStatus());
            }
            task.setStatus(request.getStatus());
        }

        if (request.getAssignedToId() != null) {
            String id = request.getAssignedToId().trim();
            if (id.isEmpty()) {
                task.setAssignedToId(null);
                task.setAssignedToName(null);
            } else {
                task.setAssignedToId(id);
                task.setAssignedToName(resolveOfficerName(id, request.getAssignedToName()));
                if (task.getStatus() == TaskStatus.CREATED) {
                    task.setStatus(TaskStatus.ASSIGNED);
                }
            }
        }

        addActivity(task, "TASK_PATCHED", "Task partially updated", actorId, actorName,
                request.getStatus() != null ? previousStatus : null,
                request.getStatus() != null ? task.getStatus() : null);

        return taskRepository.save(task);
    }

    public Task updateTaskStatus(String taskId, TaskStatusUpdateRequest request,
                                 String actorId, String actorName, String role) {
        Task task = getTaskById(taskId, actorId, role);
        ensureCanEdit(task, actorId, role);

        TaskStatus previous = task.getStatus();
        TaskStatus next = request.getStatus();

        if (!TaskStatus.isValidTransition(previous, next)) {
            throw new IllegalArgumentException("Invalid status transition: " + previous + " -> " + next);
        }

        task.setStatus(next);
        String description = request.getRemarks() != null && !request.getRemarks().trim().isEmpty()
                ? "Status updated: " + request.getRemarks().trim()
                : "Status updated";
        addActivity(task, "STATUS_UPDATED", description, actorId, actorName, previous, next);

        return taskRepository.save(task);
    }

    public Task assignTask(String taskId, TaskAssignRequest request, String actorId, String actorName, String role) {
        Task task = getTaskById(taskId, actorId, role);
        ensureCanEdit(task, actorId, role);

        String assigneeId = request.getAssignedToId().trim();
        String previousAssigneeId = task.getAssignedToId();
        task.setAssignedToId(assigneeId);
        task.setAssignedToName(resolveOfficerName(assigneeId, request.getAssignedToName()));

        TaskStatus previousStatus = task.getStatus();
        if (task.getStatus() == TaskStatus.CREATED) {
            task.setStatus(TaskStatus.ASSIGNED);
        }

        String description = "Task assigned to " + task.getAssignedToName();
        if (request.getRemarks() != null && !request.getRemarks().trim().isEmpty()) {
            description += " (" + request.getRemarks().trim() + ")";
        }

        addActivity(task, "TASK_ASSIGNED",
                description + " | previous assignee: " + (previousAssigneeId != null ? previousAssigneeId : "none"),
                actorId, actorName, previousStatus, task.getStatus());

        return taskRepository.save(task);
    }

    public Task addComment(String taskId, TaskCommentRequest request, String actorId, String actorName, String role) {
        Task task = getTaskById(taskId, actorId, role);
        ensureCanEdit(task, actorId, role);

        TaskComment comment = new TaskComment();
        comment.setCommentId("tcom-" + counterService.getNextSequence("taskCommentId"));
        comment.setMessage(request.getMessage().trim());
        comment.setCreatedById(actorId);
        comment.setCreatedByName(actorName);

        task.getComments().add(comment);
        addActivity(task, "COMMENT_ADDED", "Comment added to task", actorId, actorName, null, null);

        return taskRepository.save(task);
    }

    public void deleteTask(String taskId, String actorId, String role) {
        Task task = getTaskById(taskId, actorId, role);
        if (!isAdminRole(role) && !actorId.equals(task.getCreatedById())) {
            throw new SecurityException("Access denied: Only task creator or admin can delete task");
        }
        taskRepository.delete(task);
    }

    public Optional<Task> findByTaskNumber(String taskNumber) {
        return taskRepository.findByTaskNumber(taskNumber);
    }

    public Map<String, Object> getWorkflow(String taskId, String currentOfficerId, String currentRole) {
        Task task = getTaskById(taskId, currentOfficerId, currentRole);
        Map<String, Object> workflow = new HashMap<>();
        workflow.put("taskId", task.getId());
        workflow.put("taskNumber", task.getTaskNumber());
        workflow.put("currentStatus", task.getStatus());
        workflow.put("allowedNextStatuses", task.getStatus().allowedNextStatuses());
        workflow.put("activity", task.getActivity());
        workflow.put("comments", task.getComments());
        workflow.put("assignedToId", task.getAssignedToId());
        workflow.put("assignedToName", task.getAssignedToName());
        return workflow;
    }

    private String resolveOfficerName(String officerId, String fallbackName) {
        try {
            Officer officer = officerService.getOfficerById(officerId);
            return officer.getName();
        } catch (Exception ex) {
            if (fallbackName != null && !fallbackName.trim().isEmpty()) {
                return fallbackName.trim();
            }
            throw new IllegalArgumentException("Invalid assigned officer ID: " + officerId);
        }
    }

    private void ensureCanEdit(Task task, String currentOfficerId, String currentRole) {
        boolean creator = currentOfficerId.equals(task.getCreatedById());
        boolean assignee = currentOfficerId.equals(task.getAssignedToId());
        if (!isAdminRole(currentRole) && !creator && !assignee) {
            throw new SecurityException("Access denied: Only task creator, assignee or admin roles can modify task");
        }
    }

    private boolean canViewTask(Task task, String currentOfficerId, String currentRole) {
        if (isAdminRole(currentRole)) {
            return true;
        }
        return currentOfficerId.equals(task.getCreatedById()) || currentOfficerId.equals(task.getAssignedToId());
    }

    private boolean isAdminRole(String role) {
        return "ROLE_ADMIN".equals(role)
                || "ROLE_DISTRICT_COMMISSIONER".equals(role)
                || "ROLE_ADDITIONAL_DISTRICT_COMMISSIONER".equals(role);
    }

    private String generateTaskNumber() {
        String datePrefix = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMM"));
        long serial = counterService.getNextSequence("taskNumber");
        return "TSK-" + datePrefix + String.format("%03d", serial % 1000);
    }

    private void addActivity(Task task, String action, String description,
                             String actorId, String actorName,
                             TaskStatus previousStatus, TaskStatus newStatus) {
        TaskActivity activity = new TaskActivity();
        activity.setActivityId("tact-" + counterService.getNextSequence("taskActivityId"));
        activity.setAction(action);
        activity.setDescription(description);
        activity.setActorId(actorId);
        activity.setActorName(actorName);
        activity.setPreviousStatus(previousStatus);
        activity.setNewStatus(newStatus);
        task.getActivity().add(activity);
    }

    private List<Task> mergeTaskLists(List<Task> first, List<Task> second) {
        Map<String, Task> merged = new LinkedHashMap<>();
        for (Task task : first) {
            merged.put(task.getId(), task);
        }
        for (Task task : second) {
            merged.put(task.getId(), task);
        }
        return new ArrayList<>(merged.values());
    }
}
