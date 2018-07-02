package com.example.gamor.rhema.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class GetBills {

    @SerializedName("RespCode")
    @Expose
    private String respCode;
    @SerializedName("Cust_Account")
    @Expose
    private String custAccount;
    @SerializedName("Cust_Name")
    @Expose
    private String custName;
    @SerializedName("Location")
    @Expose
    private String location;
    @SerializedName("Phone")
    @Expose
    private String phone;
    @SerializedName("Bill_Number")
    @Expose
    private String billNumber;
    @SerializedName("Description")
    @Expose
    private String description;
    @SerializedName("Original_Amount")
    @Expose
    private String originalAmount;
    @SerializedName("Balance")
    @Expose
    private String balance;
    @SerializedName("Bill_Date")
    @Expose
    private String billDate;
    @SerializedName("Bill_Status")
    @Expose
    private String billStatus;

    public GetBills(String respCode, String custAccount, String custName, String location, String phone, String billNumber, String description, String originalAmount, String balance, String billDate, String billStatus) {
        this.respCode = respCode;
        this.custAccount = custAccount;
        this.custName = custName;
        this.location = location;
        this.phone = phone;
        this.billNumber = billNumber;
        this.description = description;
        this.originalAmount = originalAmount;
        this.balance = balance;
        this.billDate = billDate;
        this.billStatus = billStatus;
    }

    public String getRespCode() {
        return respCode;
    }

    public void setRespCode(String respCode) {
        this.respCode = respCode;
    }

    public String getCustAccount() {
        return custAccount;
    }

    public void setCustAccount(String custAccount) {
        this.custAccount = custAccount;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBillNumber() {
        return billNumber;
    }

    public void setBillNumber(String billNumber) {
        this.billNumber = billNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOriginalAmount() {
        return originalAmount;
    }

    public void setOriginalAmount(String originalAmount) {
        this.originalAmount = originalAmount;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getBillDate() {
        return billDate;
    }

    public void setBillDate(String billDate) {
        this.billDate = billDate;
    }

    public String getBillStatus() {
        return billStatus;
    }

    public void setBillStatus(String billStatus) {
        this.billStatus = billStatus;
    }

    @Override
    public String toString() {
        return "GetBill{" +
                "respCode='" + respCode + '\'' +
                ", custAccount='" + custAccount + '\'' +
                ", custName='" + custName + '\'' +
                ", location='" + location + '\'' +
                ", phone='" + phone + '\'' +
                ", billNumber='" + billNumber + '\'' +
                ", description='" + description + '\'' +
                ", originalAmount='" + originalAmount + '\'' +
                ", balance='" + balance + '\'' +
                ", billDate='" + billDate + '\'' +
                ", billStatus='" + billStatus + '\'' +
                '}';
    }
}
