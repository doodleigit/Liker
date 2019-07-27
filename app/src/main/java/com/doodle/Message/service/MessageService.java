package com.doodle.Message.service;

import com.doodle.Message.model.AllFriends;
import com.doodle.Message.model.Chats;
import com.doodle.Message.model.Friend;
import com.doodle.Message.model.Message;
import com.doodle.utils.AppConstants;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface MessageService {

    Retrofit mRetrofit = new Retrofit.Builder()
            .baseUrl(AppConstants.BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    @POST(AppConstants.CHAT_USERS)
    @FormUrlEncoded
    Call<Message> getMessageList(
            @Header("Device-Id") String deviceId,
            @Header("User-Id") String userId,
            @Header("Security-Token") String token,
            @Field("user_id") String userIds,
            @Field("limit") int limit,
            @Field("offset") int offset
    );

    @POST(AppConstants.CHAT_MESSAGES)
    @FormUrlEncoded
    Call<Chats> getAllMessage(
            @Header("Device-Id") String deviceId,
            @Header("User-Id") String userId,
            @Header("Security-Token") String token,
            @Field("user_id") String userIds,
            @Field("chat_username") String chat_user_name,
            @Field("limit") int limit,
            @Field("offset") int offset
    );

    @POST(AppConstants.FRIEND_LIST)
    @FormUrlEncoded
    Call<AllFriends> getAllFriends(
            @Header("Device-Id") String deviceId,
            @Header("Security-Token") String token,
            @Header("User-Id") String userId,
            @Field("profile_user_id") String profileUserId,
            @Field("user_id") String userIds,
            @Field("limit") int limit,
            @Field("offset") int offset
    );

    @POST(AppConstants.SEARCH_USER)
    @FormUrlEncoded
    Call<ArrayList<Friend>> getSearchUser(
            @Header("Device-Id") String deviceId,
            @Header("User-Id") String userId,
            @Header("Security-Token") String token,
            @Field("name") String searchQuery
    );

}
