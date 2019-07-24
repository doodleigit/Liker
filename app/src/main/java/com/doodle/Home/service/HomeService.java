package com.doodle.Home.service;

import com.doodle.Home.model.PostItem;
import com.doodle.Home.model.postshare.PostShareItem;
import com.doodle.utils.AppConstants;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface HomeService {


    Retrofit mRetrofit = new Retrofit.Builder()
            .baseUrl(AppConstants.BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build();


    @POST(AppConstants.FEED)
    @FormUrlEncoded
    Call<List<PostItem>> feed(
            @Header("Device-Id") String deviceId,
            @Header("User-Id") String userId,
            @Header("Security-Token") String token,
            @Field("user_id") String userIds,
            @Field("limit") int limit,
            @Field("offset") int offset,
            @Field("feed") String feed,
            @Field("cat_id") String cat_id,
            @Field("filter") int filter,
            @Field("is_public") boolean isPublic

    );

    @POST(AppConstants.GET_POST_DETAILS)
    @FormUrlEncoded
    Call<PostShareItem> getPostDetails(
            @Header("Device-Id") String deviceId,
            @Header("User-Id") String userId,
            @Header("Security-Token") String token,
            @Field("user_id") String userIds,
            @Field("post_id") String postId
    );

}
