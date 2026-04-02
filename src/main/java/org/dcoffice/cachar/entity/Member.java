package org.dcoffice.cachar.entity;

public class Member {

    // Roles: PRESIDING_OFFICER, POLLING_OFFICER_1, POLLING_OFFICER_2, POLLING_OFFICER_3, RESERVE_OFFICER
    private String role;

    private String name;

    private String mobile;

    // ✅ New field: grouping from Excel (not a DB key)
    private String groupCode;

    // Existing field (unchanged)
    private BankDetails bankDetails;

    // ✅ Default constructor (required for Mongo/Jackson)
    public Member() {}

    // ✅ Backward-compatible constructor (old usage)
    public Member(String role, String name, String mobile) {
        this.role = role;
        this.name = name;
        this.mobile = mobile;
    }

    // ✅ New constructor (for Excel ingestion)
    public Member(String role, String name, String mobile, String groupCode) {
        this.role = role;
        this.name = name;
        this.mobile = mobile;
        this.groupCode = groupCode;
    }

    // ================= GETTERS & SETTERS =================

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }

    public BankDetails getBankDetails() {
        return bankDetails;
    }

    public void setBankDetails(BankDetails bankDetails) {
        this.bankDetails = bankDetails;
    }

    // ================= OPTIONAL: toString (debugging) =================

    @Override
    public String toString() {
        return "Member{" +
                "role='" + role + '\'' +
                ", name='" + name + '\'' +
                ", mobile='" + mobile + '\'' +
                ", groupCode='" + groupCode + '\'' +
                '}';
    }
}