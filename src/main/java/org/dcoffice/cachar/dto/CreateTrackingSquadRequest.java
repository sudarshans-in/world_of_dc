package org.dcoffice.cachar.dto;

import javax.validation.constraints.NotBlank;

public class CreateTrackingSquadRequest {

    private String id;

    @NotBlank(message = "Squad name is required")
    private String name;

    @NotBlank(message = "Zone is required")
    private String zone;

    private String leadId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public String getLeadId() {
        return leadId;
    }

    public void setLeadId(String leadId) {
        this.leadId = leadId;
    }
}
