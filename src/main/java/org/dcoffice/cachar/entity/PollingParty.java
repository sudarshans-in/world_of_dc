package org.dcoffice.cachar.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

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

    private long uploadTime;

    // getters & setters
}