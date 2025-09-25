package org.dcoffice.cachar.dto;

import javax.validation.constraints.NotNull;

public class AssignComplaintRequest {
    @NotNull(message = "Complaint ID is required")
    private Long complaintId;

    @NotNull(message = "Officer ID is required")
    private Long officerId;

    private String remarks;

    public AssignComplaintRequest() {}

    public AssignComplaintRequest(Long complaintId, Long officerId, String remarks) {
        this.complaintId = complaintId;
        this.officerId = officerId;
        this.remarks = remarks;
    }

    public Long getComplaintId() { return complaintId; }
    public void setComplaintId(Long complaintId) { this.complaintId = complaintId; }

    public Long getOfficerId() { return officerId; }
    public void setOfficerId(Long officerId) { this.officerId = officerId; }

    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }
}
