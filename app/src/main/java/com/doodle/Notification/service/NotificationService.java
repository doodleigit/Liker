package com.doodle.Notification.service;

import com.doodle.Notification.model.NotificationItem;
import com.doodle.utils.AppConstants;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface NotificationService {


    Retrofit mRetrofit = new Retrofit.Builder()
            .baseUrl(AppConstants.BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build();


    @POST(AppConstants.NOTIFICATION)
    @FormUrlEncoded
    Call<ArrayList<NotificationItem>> getNotificationList(
            @Header("Device-Id") String deviceId,
            @Header("User-Id") String userId,
            @Header("Security-Token") String token,
            @Header("is_apps") boolean isApp,
            @Field("user_id") String userIds,
            @Field("limit") int limit,
            @Field("offset") int offset
    );

}
