package com.example.gamor.rhema.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PostPayBillMobileMoney {
    @SerializedName("RespCode")
    @Expose
    private String respCode;

    @SerializedName("AmountAfterCharges")
    @Expose
    private String amountAfterCharges;

    @SerializedName("TransactionId")
    @Expose
    private String transactionId;

    @SerializedName("ClientReference")
    @Expose
    private String clientReference;

    @SerializedName("Description")
    @Expose
    private String description;

    @SerializedName("ExternalTransactionId")
    @Expose
    private String externalTransactionId;

    @SerializedName("Amount")
    @Expose
    private String amount;

    @SerializedName("Charges")
    @Expose
    private String charges;

    public String getRespCode() {
        return respCode;
    }

    public void setRespCode(String respCode) {
        this.respCode = respCode;
    }

    public String getAmountAfterCharges() {
        return amountAfterCharges;
    }

    public void setAmountAfterCharges(String amountAfterCharges) {
        this.amountAfterCharges = amountAfterCharges;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getClientReference() {
        return clientReference;
    }

    public void setClientReference(String clientReference) {
        this.clientReference = clientReference;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getExternalTransactionId() {
        return externalTransactionId;
    }

    public void setExternalTransactionId(String externalTransactionId) {
        this.externalTransactionId = externalTransactionId;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCharges() {
        return charges;
    }

    public void setCharges(String charges) {
        this.charges = charges;
    }

    @Override
    public String toString() {
        return "PostPayBillMobileMoney{" +
                "respCode='" + respCode + '\'' +
                ", amountAfterCharges='" + amountAfterCharges + '\'' +
                ", transactionId='" + transactionId + '\'' +
                ", clientReference='" + clientReference + '\'' +
                ", description='" + description + '\'' +
                ", externalTransactionId='" + externalTransactionId + '\'' +
                ", amount='" + amount + '\'' +
                ", charges='" + charges + '\'' +
                '}';
    }
}
