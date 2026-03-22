package org.dcoffice.cachar.entity;

public class Member {

    private String role;  // PRESIDING_OFFICER, POLLING_OFFICER_1, POLLING_OFFICER_2, POLLING_OFFICER_3, RESERVE_OFFICER
    private String name;
    private String mobile;

    public Member() {}

    public Member(String role, String name, String mobile) {
        this.role = role;
        this.name = name;
        this.mobile = mobile;
    }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getMobile() { return mobile; }
    public void setMobile(String mobile) { this.mobile = mobile; }
}
