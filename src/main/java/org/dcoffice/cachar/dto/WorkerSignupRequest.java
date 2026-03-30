package org.dcoffice.cachar.dto;

public class WorkerSignupRequest {
    private String mobile;
    private String name;
    private String address;

    public String getMobile() { return mobile; }
    public void setMobile(String mobile) { this.mobile = mobile; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
}
