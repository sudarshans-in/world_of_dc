package org.dcoffice.cachar.dto;

public class AttendanceRecordDto {
    private String id;
    private String userId;
    private String date;
    private String loginTime;
    private String logoutTime;
    private LocationCoordsDto loginLocation;
    private LocationCoordsDto logoutLocation;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getLoginTime() { return loginTime; }
    public void setLoginTime(String loginTime) { this.loginTime = loginTime; }

    public String getLogoutTime() { return logoutTime; }
    public void setLogoutTime(String logoutTime) { this.logoutTime = logoutTime; }

    public LocationCoordsDto getLoginLocation() { return loginLocation; }
    public void setLoginLocation(LocationCoordsDto loginLocation) { this.loginLocation = loginLocation; }

    public LocationCoordsDto getLogoutLocation() { return logoutLocation; }
    public void setLogoutLocation(LocationCoordsDto logoutLocation) { this.logoutLocation = logoutLocation; }
}
