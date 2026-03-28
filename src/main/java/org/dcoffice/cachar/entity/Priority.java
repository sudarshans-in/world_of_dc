package org.dcoffice.cachar.entity;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;

public enum Priority {
    LOW("Low"),
    NORMAL("Normal"),
    @JsonEnumDefaultValue
    MEDIUM("Medium"),
    HIGH("High"),
    URGENT("Urgent"),
    CRITICAL("Critical");

    private final String displayName;

    Priority(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
