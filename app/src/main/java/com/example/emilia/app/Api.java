package com.example.emilia.app;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface Api {

    @POST("/androidrest")
    Call<ResponseBody> postUser(@Body RequestBody requestBody);


    @POST("/androidrest")
    Call<User> postCrear(@Body User user);




}
