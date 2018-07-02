package com.example.gamor.rhema.remote;

import com.example.gamor.rhema.data.model.PostPayBillMobileMoney;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface APIServicePayBillMobileMoney {
    @POST("Hubtel")
    @FormUrlEncoded
    Call<PostPayBillMobileMoney> savePostPayBillMobileMoney(@Field("CustomerName") String CustomerName,
                                                            @Field("CustomerMsisdn") String CustomerMsisdn,
                                                            @Field("CustomerEmail") String CustomerEmail,
                                                            @Field("Channel") String Channel,
                                                            @Field("Amount") Float Amount,
                                                            @Field("PrimaryCallbackUrl") String PrimaryCallbackUrl,
                                                            @Field("SecondaryCallbackUrl") String SecondaryCallbackUrl,
                                                            @Field("Description") String Description,
                                                            @Field("ClientReference") String ClientReference,
                                                            @Field("Token") String Token,
                                                            @Field("AccountNumber") String AccountNumber,
                                                            @Field("BillNumber") String BillNumber
    );
}
