package com.example.gamor.rhema.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PostRegistration {

    @SerializedName("RespCode")
    @Expose
    private String respCode;
    @SerializedName("CustomerCode")
    @Expose
    private String customerCode;
    @SerializedName("RegistrationDate")
    @Expose
    private String registrationDate;
    @SerializedName("RespMessage")
    @Expose
    private String respMessage;

    public String getRespCode() {
        return respCode;
    }

    public void setRespCode(String respCode) {
        this.respCode = respCode;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public String getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(String registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getRespMessage() {
        return respMessage;
    }

    public void setRespMessage(String respMessage) {
        this.respMessage = respMessage;
    }

    @Override
    public String toString() {
        return "PostRegistration{" +
                "respCode='" + respCode + '\'' +
                ", customerCode='" + customerCode + '\'' +
                ", registrationDate='" + registrationDate + '\'' +
                ", respMessage='" + respMessage + '\'' +
                '}';
    }
}