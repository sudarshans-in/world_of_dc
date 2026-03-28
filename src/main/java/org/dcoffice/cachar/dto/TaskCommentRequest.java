package org.dcoffice.cachar.dto;

import javax.validation.constraints.NotBlank;

public class TaskCommentRequest {

    @NotBlank(message = "Comment message is required")
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
