package com.doodle.Search.service;

import com.doodle.Search.model.AdvanceSearches;
import com.doodle.Search.model.Message;
import com.doodle.Search.model.SearchHistory;
import com.doodle.Search.model.SearchUser;
import com.doodle.Tool.AppConstants;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface SearchService {


    Retrofit mRetrofit = new Retrofit.Builder()
            .baseUrl(AppConstants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();


    @POST(AppConstants.SEARCH_USER)
    @FormUrlEncoded
    Call<List<SearchUser>> searchUser(
            @Header("Device-Id") String deviceId,
            @Header("User-Id") String userId,
            @Header("Security-Token") String token,
            @Field("name") String searchQuery
    );

    @POST(AppConstants.GET_SEARCH_HISTORY)
    @FormUrlEncoded
    Call<List<SearchHistory>> searchHistory(
            @Header("Device-Id") String deviceId,
            @Header("User-Id") String userId,
            @Header("Security-Token") String token,
            @Field("user_id") String userIds
    );

    @POST(AppConstants.REMOVE_SEARCH_HISTORY)
    @FormUrlEncoded
    Call<Message> clearHistory(
            @Header("Device-Id") String deviceId,
            @Header("User-Id") String userId,
            @Header("Security-Token") String token,
            @Field("user_id") String userIds
    );

    @POST(AppConstants.ADVANCE_SEARCH)
    @FormUrlEncoded
    Call<AdvanceSearches> advanceSearch(
            @Header("Device-Id") String deviceId,
            @Header("User-Id") String userId,
            @Header("Security-Token") String token,
            @Field("user_id") String userIds,
            @Field("search_term") String searchTerm,
            @Field("limit") int limit,
            @Field("offset") int offset,
            @Field("post_only") int postOnly,
            @Field("image_only") int imageOnly
    );

    @POST(AppConstants.ADVANCE_SEARCH)
    @FormUrlEncoded
    Call<AdvanceSearches> advanceSearchPaging(
            @Header("Device-Id") String deviceId,
            @Header("User-Id") String userId,
            @Header("Security-Token") String token,
            @Field("user_id") String userIds,
            @Field("search_term") String searchTerm,
            @Field("limit") int limit,
            @Field("offset") int offset,
            @Field("post_only") int postOnly
    );
}
