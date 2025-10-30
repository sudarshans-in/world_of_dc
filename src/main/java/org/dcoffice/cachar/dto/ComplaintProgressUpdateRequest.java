package org.dcoffice.cachar.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * DTO for updating complaint progress
 * Only DC or complaint creator can update progress
 */
public class ComplaintProgressUpdateRequest {

    @NotNull(message = "Complaint ID is required")
    private Long complaintId;

    @Min(value = 0, message = "Progress percentage must be between 0 and 100")
    @Max(value = 100, message = "Progress percentage must be between 0 and 100")
    private Integer progressPercentage;

    @NotBlank(message = "Progress notes are required")
    private String progressNotes;

    @NotBlank(message = "Update remarks are required")
    private String updateRemarks;

    // Getters and Setters
    public Long getComplaintId() { return complaintId; }
    public void setComplaintId(Long complaintId) { this.complaintId = complaintId; }

    public Integer getProgressPercentage() { return progressPercentage; }
    public void setProgressPercentage(Integer progressPercentage) { this.progressPercentage = progressPercentage; }

    public String getProgressNotes() { return progressNotes; }
    public void setProgressNotes(String progressNotes) { this.progressNotes = progressNotes; }

    public String getUpdateRemarks() { return updateRemarks; }
    public void setUpdateRemarks(String updateRemarks) { this.updateRemarks = updateRemarks; }
}

