package org.dcoffice.cachar.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "vehicle_details")
public class VehicleDetails {

    @Id
    private String id;

    private String acNo;
    private String psNo;
    private String psName;

    private String vehicleNo;
    private String driverName;
    private String driverMobile;
    private String vehicleType;
    private Integer capacity;

    private String route;
    private String remarks;

    private Long uploadTime;
}