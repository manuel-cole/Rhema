package com.example.gamor.rhema.remote;

import com.example.gamor.rhema.data.model.PostPayBillMobileMoneyFinalised;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface APIServicePayBillMobileMoneyFinalised {
    @POST("FinalizePayment")
    @FormUrlEncoded
    Call<PostPayBillMobileMoneyFinalised> savePostPayBillMobileMoneyFinalised(@Field("TransactionID") String TransactionID

    );
}
