package org.dcoffice.cachar.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class OTPRequest {
    @NotBlank(message = "Mobile number is required")
    @Pattern(regexp = "^[6-9]\\d{9}$", message = "Invalid mobile number format")
    private String mobileNumber;

    private String otp;

    public OTPRequest() {}

    public OTPRequest(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public OTPRequest(String mobileNumber, String otp) {
        this.mobileNumber = mobileNumber;
        this.otp = otp;
    }

    public String getMobileNumber() { return mobileNumber; }
    public void setMobileNumber(String mobileNumber) { this.mobileNumber = mobileNumber; }

    public String getOtp() { return otp; }
    public void setOtp(String otp) { this.otp = otp; }
}
