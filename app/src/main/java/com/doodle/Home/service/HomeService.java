package com.doodle.Home.service;

import com.doodle.Comment.model.CommentItem;
import com.doodle.Home.model.PostItem;
import com.doodle.Home.model.StarContributor;
import com.doodle.Home.model.postshare.PostShareItem;
import com.doodle.utils.AppConstants;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface HomeService {


    OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .connectTimeout(1, TimeUnit.MINUTES)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .build();

    Retrofit mRetrofit = new Retrofit.Builder()
            .baseUrl(AppConstants.BASE_URL)
            .client(okHttpClient)
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
    //https://www.stg.liker.com/get_postscomments
    @POST(AppConstants.GET_POST_COMMENTS)
    @FormUrlEncoded
    Call<CommentItem> getPostComments(
            @Header("Device-Id") String deviceId,
            @Header("User-Id") String userId,
            @Header("Security-Token") String token,
            @Field("is_public") String isPublic,
            @Field("limit") int limit,
            @Field("offset") int offset,
            @Field("orderby") String orderBy,
            @Field("post_id") String feed,
            @Field("user_id") String userIds
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

//https://www.stg.liker.com/addSharedpost
    @POST(AppConstants.ADD_SHARED_POST)
    @FormUrlEncoded
    Call<String> addSharedPost(
            @Header("Device-Id") String deviceId,
            @Header("User-Id") String userId,
            @Header("Security-Token") String token,
            @Field("user_id") String userIds,
            @Field("post_id") String postId,
            @Field("post_content") String postContent,
            @Field("post_permission") int postPermission,
            @Field("category_id") String categoryId,
            @Field("sub_category_id") String subCategoryId,
            @Field("post_type") String postType,
            @Field("user_name") String userName,
            @Field("friends") String friends
    );


    //https://www.stg.liker.com/send_browser_notification
    @POST(AppConstants.SEND_BROWSER_NOTIFICATION)
    @FormUrlEncoded
    Call<String> sendBrowserNotification(
            @Header("Device-Id") String deviceId,
            @Header("User-Id") String userId,
            @Header("Security-Token") String token,
            @Field("to_user_id") String toUserId,//anotherId
            @Field("from_user_id") String fromUserId,// myId
            @Field("content_id") String contentId,
            @Field("type") String type
    );

    @POST(AppConstants.GET_FILTER_CATEGORIES)
    @FormUrlEncoded
    Call<String> sendFilterCategories(
            @Header("Device-Id") String deviceId,
            @Header("User-Id") String userId,
            @Header("Security-Token") String token,
            @Field("user_id") String id
    );

    @POST(AppConstants.GET_USER_RANKINGS)
    @FormUrlEncoded
    Call<ArrayList<StarContributor>> getUserRanking(
            @Header("Device-Id") String deviceId,
            @Header("User-Id") String userId,
            @Header("Security-Token") String token,
            @Field("cat_id") String catId,
            @Field("search_name") String searchName,
            @Field("offset") int offset,
            @Field("limit") int limit
    );

}
