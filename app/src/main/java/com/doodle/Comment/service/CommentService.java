package com.doodle.Comment.service;

import com.doodle.Comment.model.CommentItem;
import com.doodle.Comment.model.CommentTextIndex_;
import com.doodle.Comment.model.Comment_;
import com.doodle.Home.model.PostItem;
import com.doodle.Home.model.postshare.PostShareItem;
import com.doodle.utils.AppConstants;

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

public interface CommentService {


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


    //https://www.stg.liker.com/get_postscomments
    @POST(AppConstants.GET_POST_COMMENTS)
    @FormUrlEncoded
    Call<CommentItem> getAllPostComments(
            @Header("Device-Id") String deviceId,
            @Header("User-Id") String userId,
            @Header("Security-Token") String token,
            @Field("is_public") String isPublic,
            @Field("limit") int limit,
            @Field("offset") int offset,
            @Field("orderby") String orderBy,
            @Field("post_id") String postId,
            @Field("user_id") String userIds
    );

    //https://www.stg.liker.com/added_comment

    @POST(AppConstants.ADDED_COMMENTS)
    @FormUrlEncoded
    Call<Comment_> addedComment(
            @Header("Device-Id") String deviceId,
            @Header("User-Id") String userId,
            @Header("Security-Token") String token,
            @Field("comment_image") String commentImage,
            @Field("comment_text") String commentText,
            @Field("comment_type") int commentType,
            @Field("has_mention") int hasMention,
            @Field("link_url") String linkUrl,
            @Field("mention") int mention,
            @Field("post_id") String postId,
            @Field("user_id") String userIds
    );

    /*comment_image
comment_text	7
comment_type	1

has_mention	0
link_url
mention
post_id	32891
user_id	26445




/*is_public	false
limit	10
offset	3
orderby	DESC
post_id	32891
user_id	26445
*/





}
