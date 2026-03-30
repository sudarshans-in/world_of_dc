package org.dcoffice.cachar.dto;

public class WorkerAttendanceRequest {
    private String userId;
    private LocationCoordsDto location;

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public LocationCoordsDto getLocation() { return location; }
    public void setLocation(LocationCoordsDto location) { this.location = location; }
}
