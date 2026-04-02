package org.dcoffice.cachar.entity;

import org.springframework.data.mongodb.core.mapping.Field;

public class BankDetails {

    @Field("account_holder_name")
    private String accountHolderName;

    @Field("account_number")
    private String accountNumber;

    @Field("ifsc_code")
    private String ifscCode;

    @Field("bank_name")
    private String bankName;

    @Field("branch_name")
    private String branchName;

    // Optional (UPI support)
    @Field("upi_id")
    private String upiId;

    @Field("mobile_number")
    private String mobileNumber;

    public String getAccountHolderName() {
        return accountHolderName;
    }

    public void setAccountHolderName(String accountHolderName) {
        this.accountHolderName = accountHolderName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getIfscCode() {
        return ifscCode;
    }

    public void setIfscCode(String ifscCode) {
        this.ifscCode = ifscCode;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getUpiId() {
        return upiId;
    }

    public void setUpiId(String upiId) {
        this.upiId = upiId;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }
}