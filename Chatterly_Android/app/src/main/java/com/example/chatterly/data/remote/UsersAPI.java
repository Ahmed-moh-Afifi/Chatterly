package com.example.chatterly.data.remote;

import com.example.chatterly.model.data.User;

import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface UsersAPI {
    @GET("Users/{userId}")
    Call<User> getUser(@Path("userId") String userId);

    @PUT("Users/{userId}")
    Call updateUser(@Path("userId") String userId, @Body User user);

    @GET("Users/")
    Call<List<User>> searchUsers(@Query("query") String query);

    @GET("Users/Username/{username}/Available")
    Call<Boolean> isUsernameAvailable(@Path("username") String username);
}