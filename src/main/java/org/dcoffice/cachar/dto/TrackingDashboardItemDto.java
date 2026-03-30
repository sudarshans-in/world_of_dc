package org.dcoffice.cachar.dto;

import org.dcoffice.cachar.entity.TrackingActivity;
import org.dcoffice.cachar.entity.TrackingMember;
import org.dcoffice.cachar.entity.TrackingSquad;

import java.util.List;

public class TrackingDashboardItemDto {

    private TrackingSquad squad;
    private List<TrackingMember> members;
    private List<TrackingActivity> latestActivities;

    public TrackingSquad getSquad() {
        return squad;
    }

    public void setSquad(TrackingSquad squad) {
        this.squad = squad;
    }

    public List<TrackingMember> getMembers() {
        return members;
    }

    public void setMembers(List<TrackingMember> members) {
        this.members = members;
    }

    public List<TrackingActivity> getLatestActivities() {
        return latestActivities;
    }

    public void setLatestActivities(List<TrackingActivity> latestActivities) {
        this.latestActivities = latestActivities;
    }
}
