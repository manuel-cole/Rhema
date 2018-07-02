package com.example.gamor.rhema.remote;

public class ApiUtils {

    private ApiUtils() {}

    public static final String BASE_URL = "http://102.176.96.33:8080/api/";

    public static UserService getUserService(){
        return RetrofitClient.getClient(BASE_URL).create(UserService.class);
    }

    public static APIServiceRegister getUserServiceRegister(){
        return RetrofitClient.getClient(BASE_URL).create(APIServiceRegister.class);
    }

    public static APIBillGetService getUserServiceBills(){
        return RetrofitClient.getClient(BASE_URL).create(APIBillGetService.class);
    }

    public static APIServicePayBill getUserServicePayBill(){
        return RetrofitClient.getClient(BASE_URL).create(APIServicePayBill.class);
    }

    public static APIServicePayBillMobileMoney getUserServicePayBillMobileMoney(){
        return RetrofitClient.getClient(BASE_URL).create(APIServicePayBillMobileMoney.class);
    }

    public static APIServicePayBillMobileMoneyFinalised getUserServicePayBillMobileMoneyFinalised(){
        return RetrofitClient.getClient(BASE_URL).create(APIServicePayBillMobileMoneyFinalised.class);
    }

}
