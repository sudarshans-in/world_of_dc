package org.dcoffice.cachar.dto;

import java.time.Instant;
import java.util.List;

public class TrackingDashboardResponseDto {

    private Instant generatedAt;
    private List<TrackingDashboardItemDto> squads;

    public Instant getGeneratedAt() {
        return generatedAt;
    }

    public void setGeneratedAt(Instant generatedAt) {
        this.generatedAt = generatedAt;
    }

    public List<TrackingDashboardItemDto> getSquads() {
        return squads;
    }

    public void setSquads(List<TrackingDashboardItemDto> squads) {
        this.squads = squads;
    }
}
