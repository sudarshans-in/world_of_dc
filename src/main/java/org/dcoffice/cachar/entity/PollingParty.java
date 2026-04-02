package org.dcoffice.cachar.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

@Document(collection = "polling_party")
public class PollingParty {

    @Id
    private String id;

    private String acNo;
    private String psNo;
    private String psName;
    private String partyNo;
    private List<Member> members;
    private List<Member> policeMembers;
    private String vehicleId;
    private Long uploadTime;

    private Materials materials;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getAcNo() { return acNo; }
    public void setAcNo(String acNo) { this.acNo = acNo; }

    public String getPsNo() { return psNo; }
    public void setPsNo(String psNo) { this.psNo = psNo; }

    public String getPsName() { return psName; }
    public void setPsName(String psName) { this.psName = psName; }

    public String getPartyNo() { return partyNo; }
    public void setPartyNo(String partyNo) { this.partyNo = partyNo; }

    public List<Member> getMembers() { return members; }
    public void setMembers(List<Member> members) { this.members = members; }

    public List<Member> getPoliceMembers() {
        return policeMembers;
    }

    public void setPoliceMembers(List<Member> policeMembers) {
        this.policeMembers = policeMembers;
    }

    public String getVehicleId() { return vehicleId; }
    public void setVehicleId(String vehicleId) { this.vehicleId = vehicleId; }

    public Long getUploadTime() { return uploadTime; }
    public void setUploadTime(Long uploadTime) { this.uploadTime = uploadTime; }

    public Materials getMaterials() { return materials; }
    public void setMaterials(Materials materials) { this.materials = materials; }

    // Backward compatibility getters for old field names
    @Deprecated
    @JsonIgnore
    public String getPresidingOfficer() {
        if (members != null) {
            return members.stream()
                    .filter(m -> "PRESIDING_OFFICER".equals(m.getRole()))
                    .map(Member::getName)
                    .findFirst()
                    .orElse(null);
        }
        return null;
    }

    @Deprecated
    @JsonIgnore
    public String getPollingOfficer1() {
        if (members != null) {
            return members.stream()
                    .filter(m -> "POLLING_OFFICER_1".equals(m.getRole()))
                    .map(Member::getName)
                    .findFirst()
                    .orElse(null);
        }
        return null;
    }

    @Deprecated
    @JsonIgnore
    public String getPollingOfficer2() {
        if (members != null) {
            return members.stream()
                    .filter(m -> "POLLING_OFFICER_2".equals(m.getRole()))
                    .map(Member::getName)
                    .findFirst()
                    .orElse(null);
        }
        return null;
    }

    @Deprecated
    @JsonIgnore
    public String getPollingOfficer3() {
        if (members != null) {
            return members.stream()
                    .filter(m -> "POLLING_OFFICER_3".equals(m.getRole()))
                    .map(Member::getName)
                    .findFirst()
                    .orElse(null);
        }
        return null;
    }

    @Deprecated
    @JsonIgnore
    public String getReserveOfficer() {
        if (members != null) {
            return members.stream()
                    .filter(m -> "RESERVE_OFFICER".equals(m.getRole()))
                    .map(Member::getName)
                    .findFirst()
                    .orElse(null);
        }
        return null;
    }

    @Deprecated
    @JsonIgnore
    public String getMobile() {
        if (members != null) {
            return members.stream()
                    .filter(m -> m.getMobile() != null && !m.getMobile().isBlank())
                    .map(Member::getMobile)
                    .findFirst()
                    .orElse(null);
        }
        return null;
    }
}
