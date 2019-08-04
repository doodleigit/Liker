package com.doodle.Profile.service;

import com.doodle.Profile.model.AlbumPhoto;
import com.doodle.Profile.model.AllFriend;
import com.doodle.Profile.model.PhotoAlbum;
import com.doodle.Profile.model.RecentPhoto;
import com.doodle.utils.AppConstants;

import java.util.ArrayList;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ProfileService {

    Retrofit mRetrofit = new Retrofit.Builder()
            .baseUrl(AppConstants.BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    @POST(AppConstants.CHAT_USERS)
    @FormUrlEncoded
    Call<String> getProfileData(
            @Header("Device-Id") String deviceId,
            @Header("User-Id") String userId,
            @Header("Security-Token") String token,
            @Field("user_id") String userIds
    );

    @POST(AppConstants.FRIEND_LIST)
    @FormUrlEncoded
    Call<AllFriend> getAllFriends(
            @Header("Device-Id") String deviceId,
            @Header("Security-Token") String token,
            @Header("User-Id") String userId,
            @Field("profile_user_id") String profileUserId,
            @Field("user_id") String userIds,
            @Field("limit") int limit,
            @Field("offset") int offset
    );

    @POST(AppConstants.VIEW_ALBUMS)
    @FormUrlEncoded
    Call<ArrayList<PhotoAlbum>> getAlbums(
            @Header("Device-Id") String deviceId,
            @Header("Security-Token") String token,
            @Header("User-Id") String userId,
            @Field("profile_user_id") String profileUserId,
            @Field("user_id") String userIds
    );

    @POST(AppConstants.GET_ALBUM_PHOTOS)
    @FormUrlEncoded
    Call<ArrayList<AlbumPhoto>> getAlbumPhotos(
            @Header("Device-Id") String deviceId,
            @Header("Security-Token") String token,
            @Header("User-Id") String userId,
            @Field("profile_user_id") String profileUserId,
            @Field("album_type") String albumType,
            @Field("user_id") String userIds,
            @Field("limit") int limit,
            @Field("offset") int offset
    );

    @POST(AppConstants.GET_RECENT_PHOTOS)
    @FormUrlEncoded
    Call<ArrayList<RecentPhoto>> getRecentPhotos(
            @Header("Device-Id") String deviceId,
            @Header("Security-Token") String token,
            @Header("User-Id") String userId,
            @Field("profile_user_id") String profileUserId,
            @Field("user_id") String userIds,
            @Field("limit") int limit,
            @Field("offset") int offset
    );

    @Multipart
    @POST(AppConstants.ADD_PHOTO)
    Call<String> updateProfileImage(
            @Header("Device-Id") String deviceId,
            @Header("User-Id") String userId,
            @Header("Security-Token") String token,
            @Part MultipartBody.Part file

    );

}
