package org.dcoffice.cachar.dto;

public class AdminWorkerSummaryDto {
    private WorkerUserDto user;
    private AttendanceRecordDto todayAttendance;

    public WorkerUserDto getUser() { return user; }
    public void setUser(WorkerUserDto user) { this.user = user; }

    public AttendanceRecordDto getTodayAttendance() { return todayAttendance; }
    public void setTodayAttendance(AttendanceRecordDto todayAttendance) { this.todayAttendance = todayAttendance; }
}
