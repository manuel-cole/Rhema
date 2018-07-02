package com.example.gamor.rhema.remote;

import com.example.gamor.rhema.data.model.PostPayBill;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface APIServicePayBill {

//    @Headers("Content-Type: application/xml")
    @POST("PayBills")
    @FormUrlEncoded
    Call<PostPayBill> savePostPayBill(@Field("AccountNumber") String AccountNumber,
                                      @Field("Amount") Float Amount,
                                      @Field("PaymentType") String PaymentType,
                                      @Field("BillNumber") String BillNumber);
}
