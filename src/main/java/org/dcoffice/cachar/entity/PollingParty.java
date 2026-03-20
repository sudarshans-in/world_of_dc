package org.dcoffice.cachar.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "polling_party")
public class PollingParty {

    @Id
    private String id;

    private String acNo;
    private String psNo;
    private String psName;
    private String partyNo;
    private String presidingOfficer;
    private String pollingOfficer1;
    private String pollingOfficer2;
    private String pollingOfficer3;
    private String reserveOfficer;
    private String mobile;
    private Long uploadTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAcNo() {
        return acNo;
    }

    public void setAcNo(String acNo) {
        this.acNo = acNo;
    }

    public String getPsNo() {
        return psNo;
    }

    public void setPsNo(String psNo) {
        this.psNo = psNo;
    }

    public String getPsName() {
        return psName;
    }

    public void setPsName(String psName) {
        this.psName = psName;
    }

    public String getPartyNo() {
        return partyNo;
    }

    public void setPartyNo(String partyNo) {
        this.partyNo = partyNo;
    }

    public String getPresidingOfficer() {
        return presidingOfficer;
    }

    public void setPresidingOfficer(String presidingOfficer) {
        this.presidingOfficer = presidingOfficer;
    }

    public String getPollingOfficer1() {
        return pollingOfficer1;
    }

    public void setPollingOfficer1(String pollingOfficer1) {
        this.pollingOfficer1 = pollingOfficer1;
    }

    public String getPollingOfficer2() {
        return pollingOfficer2;
    }

    public void setPollingOfficer2(String pollingOfficer2) {
        this.pollingOfficer2 = pollingOfficer2;
    }

    public String getPollingOfficer3() {
        return pollingOfficer3;
    }

    public void setPollingOfficer3(String pollingOfficer3) {
        this.pollingOfficer3 = pollingOfficer3;
    }

    public String getReserveOfficer() {
        return reserveOfficer;
    }

    public void setReserveOfficer(String reserveOfficer) {
        this.reserveOfficer = reserveOfficer;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Long getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(Long uploadTime) {
        this.uploadTime = uploadTime;
    }
}
