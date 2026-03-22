package org.dcoffice.cachar.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class ElectionComplaintCreateRequest {

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Mobile number is required")
    @Pattern(regexp = "^[6-9]\\d{9}$", message = "Invalid mobile number")
    private String mobileNo;

    @NotBlank(message = "Polling station name is required")
    private String psName;

    @NotBlank(message = "Severity is required")
    private String severity;

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Description is required")
    private String description;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getMobileNo() { return mobileNo; }
    public void setMobileNo(String mobileNo) { this.mobileNo = mobileNo; }

    public String getPsName() { return psName; }
    public void setPsName(String psName) { this.psName = psName; }

    public String getSeverity() { return severity; }
    public void setSeverity(String severity) { this.severity = severity; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
