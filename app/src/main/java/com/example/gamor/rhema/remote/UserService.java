package com.example.gamor.rhema.remote;

import com.example.gamor.rhema.model.ResObj;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface UserService {

    @GET("GetUser")
    Call<ResObj> login(@Query("username") String username, @Query("pwd") String password);

//    @GET("GetBills")
//    Call<ResGetBiils> gettingBiils(@Query("CustomerCode") String Id);

//    @GET("GetBills")
//    Call<List<GetBills>> gettingBiils(@Query("CustomerCode") String Id);



}
