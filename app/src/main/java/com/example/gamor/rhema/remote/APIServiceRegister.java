package com.example.gamor.rhema.remote;

import com.example.gamor.rhema.data.model.PostRegistration;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;


public interface APIServiceRegister {
    @POST("SaveCustomer")
    @FormUrlEncoded
    Call<PostRegistration> savePostRegister(@Field("Surname") String Surname,
                                    @Field("Firstname") String Firstname,
                                    @Field("OtherName") String OtherName,
                                    @Field("Address") String Address,
                                    @Field("DateOfBirth") String DateOfBirth,
                                    @Field("LicenseNo") String LicenseNo,
                                    @Field("DateOfIssue") String DateOfIssue,
                                    @Field("DateOfExpiry") String DateOfExpiry,
                                    @Field("DateOfRenewal") String DateOfRenewal,
                                    @Field("LicenseClass") String LicenseClass,
                                    @Field("Email") String Email,
                                    @Field("RegistrationCenter") String RegistrationCenter,
                                    @Field("Nationality") String Nationality,
                                    @Field("Phone") String Phone,
                                    @Field("CustomerPicture") String CustomerPicture);
}
