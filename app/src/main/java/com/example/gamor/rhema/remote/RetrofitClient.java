package com.example.gamor.rhema.remote;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static Retrofit retrofit = null;

    public static Retrofit getClient(String url){
//        OkHttpClient client = new Builder()
//                .connectTimeout(20, TimeUnit.SECONDS)
//                .readTimeout(40,TimeUnit.SECONDS)
//                .build();


//        if (retrofit == null){
//            retrofit = new Retrofit.Builder()
//                    .baseUrl(url).client(client)
//                    .addConverterFactory(GsonConverterFactory.create())
//                    .build();
//        }

        if (retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

}
