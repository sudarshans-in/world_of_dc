package org.dcoffice.cachar.dto;

import javax.validation.constraints.NotBlank;

public class TaskAssignRequest {

    @NotBlank(message = "Assigned officer ID is required")
    private String assignedToId;

    private String assignedToName;
    private String remarks;

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

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
