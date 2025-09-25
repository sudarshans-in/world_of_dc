package org.dcoffice.cachar.entity;

public enum ComplaintStatus {
    SUBMITTED("Submitted"),
    UNDER_REVIEW("Under Review"),
    ASSIGNED("Assigned"),
    IN_PROGRESS("In Progress"),
    PENDING_INFORMATION("Pending Information"),
    RESOLVED("Resolved"),
    CLOSED("Closed"),
    REJECTED("Rejected");

    private final String displayName;

    ComplaintStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
