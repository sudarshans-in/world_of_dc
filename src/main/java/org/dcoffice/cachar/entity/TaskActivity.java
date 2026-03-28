package org.dcoffice.cachar.entity;

import java.time.LocalDateTime;

public class TaskActivity {

    private String activityId;
    private String action;
    private String description;
    private String actorId;
    private String actorName;
    private TaskStatus previousStatus;
    private TaskStatus newStatus;
    private LocalDateTime timestamp;

    public TaskActivity() {
        this.timestamp = LocalDateTime.now();
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getActorId() {
        return actorId;
    }

    public void setActorId(String actorId) {
        this.actorId = actorId;
    }

    public String getActorName() {
        return actorName;
    }

    public void setActorName(String actorName) {
        this.actorName = actorName;
    }

    public TaskStatus getPreviousStatus() {
        return previousStatus;
    }

    public void setPreviousStatus(TaskStatus previousStatus) {
        this.previousStatus = previousStatus;
    }

    public TaskStatus getNewStatus() {
        return newStatus;
    }

    public void setNewStatus(TaskStatus newStatus) {
        this.newStatus = newStatus;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
