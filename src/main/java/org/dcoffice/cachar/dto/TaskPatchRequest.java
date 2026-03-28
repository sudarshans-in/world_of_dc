package org.dcoffice.cachar.dto;

import org.dcoffice.cachar.entity.Department;
import org.dcoffice.cachar.entity.Priority;
import org.dcoffice.cachar.entity.TaskScope;
import org.dcoffice.cachar.entity.TaskStatus;

import java.time.LocalDateTime;
import java.util.List;

public class TaskPatchRequest {

    private String title;
    private String description;
    private Department department;
    private TaskScope scope;
    private TaskStatus status;
    private Priority priority;
    private String assignedToId;
    private String assignedToName;
    private LocalDateTime dueDate;
    private List<String> tags;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public TaskScope getScope() {
        return scope;
    }

    public void setScope(TaskScope scope) {
        this.scope = scope;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public String getAssignedToId() {
        return assignedToId;
    }

    public void setAssignedToId(String assignedToId) {
        this.assignedToId = assignedToId;
    }

    public String getAssignedToName() {
        return assignedToName;
    }

    public void setAssignedToName(String assignedToName) {
        this.assignedToName = assignedToName;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }
}
