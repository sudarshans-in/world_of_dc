package org.dcoffice.cachar.entity;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;

public enum TaskStatus {
    // Standard lifecycle
    OPEN,
    @JsonEnumDefaultValue
    CREATED,
    PENDING,
    ASSIGNED,
    IN_PROGRESS,
    BLOCKED,
    ON_HOLD,
    DEFERRED,
    REVIEW,
    IN_REVIEW,
    COMPLETED,
    DONE,
    CLOSED,
    CANCELLED,
    REJECTED,
    REOPENED,
    TODO;

    private static final java.util.Map<TaskStatus, java.util.Set<TaskStatus>> ALLOWED_TRANSITIONS;

    static {
        java.util.Map<TaskStatus, java.util.Set<TaskStatus>> map = new java.util.EnumMap<>(TaskStatus.class);

        map.put(OPEN,      java.util.EnumSet.of(CREATED, PENDING, ASSIGNED, IN_PROGRESS, CANCELLED));
        map.put(TODO,      java.util.EnumSet.of(OPEN, CREATED, ASSIGNED, IN_PROGRESS, CANCELLED));
        map.put(CREATED,   java.util.EnumSet.of(PENDING, ASSIGNED, IN_PROGRESS, CANCELLED));
        map.put(PENDING,   java.util.EnumSet.of(ASSIGNED, IN_PROGRESS, CANCELLED, DEFERRED));
        map.put(ASSIGNED,  java.util.EnumSet.of(IN_PROGRESS, BLOCKED, ON_HOLD, DEFERRED, CANCELLED));
        map.put(IN_PROGRESS, java.util.EnumSet.of(BLOCKED, ON_HOLD, REVIEW, IN_REVIEW, COMPLETED, DONE, CANCELLED));
        map.put(BLOCKED,   java.util.EnumSet.of(IN_PROGRESS, ON_HOLD, CANCELLED));
        map.put(ON_HOLD,   java.util.EnumSet.of(IN_PROGRESS, ASSIGNED, CANCELLED));
        map.put(DEFERRED,  java.util.EnumSet.of(ASSIGNED, IN_PROGRESS, CANCELLED));
        map.put(REVIEW,    java.util.EnumSet.of(IN_PROGRESS, COMPLETED, DONE, REJECTED));
        map.put(IN_REVIEW, java.util.EnumSet.of(IN_PROGRESS, COMPLETED, DONE, REJECTED));
        map.put(COMPLETED, java.util.EnumSet.of(CLOSED, REOPENED, IN_PROGRESS));
        map.put(DONE,      java.util.EnumSet.of(CLOSED, REOPENED, IN_PROGRESS));
        map.put(CLOSED,    java.util.EnumSet.of(REOPENED));
        map.put(REJECTED,  java.util.EnumSet.of(REOPENED));
        map.put(REOPENED,  java.util.EnumSet.of(ASSIGNED, IN_PROGRESS));
        map.put(CANCELLED, java.util.EnumSet.of(REOPENED));

        ALLOWED_TRANSITIONS = java.util.Collections.unmodifiableMap(map);
    }

    public java.util.Set<TaskStatus> allowedNextStatuses() {
        return ALLOWED_TRANSITIONS.getOrDefault(this, java.util.EnumSet.noneOf(TaskStatus.class));
    }

    public static boolean isValidTransition(TaskStatus from, TaskStatus to) {
        if (from == null || to == null) {
            return false;
        }
        if (from == to) {
            return true;
        }
        return ALLOWED_TRANSITIONS.getOrDefault(from, java.util.EnumSet.noneOf(TaskStatus.class)).contains(to);
    }
}
