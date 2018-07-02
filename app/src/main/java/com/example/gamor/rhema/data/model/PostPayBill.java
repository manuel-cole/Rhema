package com.example.gamor.rhema.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PostPayBill {

    @SerializedName("RespCode")
    @Expose
    private String respCode;
    @SerializedName("PaymentNumber")
    @Expose
    private String paymentNumber;
    @SerializedName("PaymentAmount")
    @Expose
    private Float paymentAmount;
    @SerializedName("PaymentDate")
    @Expose
    private String paymentDate;
    @SerializedName("Respmessage")
    @Expose
    private String respmessage;

    public String getRespCode() {
        return respCode;
    }

    public void setRespCode(String respCode) {
        this.respCode = respCode;
    }

    public String getPaymentNumber() {
        return paymentNumber;
    }

    public void setPaymentNumber(String paymentNumber) {
        this.paymentNumber = paymentNumber;
    }

    public Float getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(Float paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getRespmessage() {
        return respmessage;
    }

    public void setRespmessage(String respmessage) {
        this.respmessage = respmessage;
    }

    @Override
    public String toString() {
        return "PostPayBill{" +
                "respCode='" + respCode + '\'' +
                ", paymentNumber='" + paymentNumber + '\'' +
                ", paymentAmount=" + paymentAmount +
                ", paymentDate='" + paymentDate + '\'' +
                ", respmessage='" + respmessage + '\'' +
                '}';
    }
}
