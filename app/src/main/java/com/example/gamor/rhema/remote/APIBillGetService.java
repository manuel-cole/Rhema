package com.example.gamor.rhema.remote;

import com.example.gamor.rhema.data.model.GetBills;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface APIBillGetService {

    @GET("GetBills?CustomerCode=1234")
    Call<List<GetBills>> gettingBiils1();

      @GET("GetBills")
      Call<List<GetBills>> gettingBiils(@Query("CustomerCode") String Id);
}
