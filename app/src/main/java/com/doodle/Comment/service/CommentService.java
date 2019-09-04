package com.doodle.Comment.service;

import com.doodle.Comment.model.CommentItem;
import com.doodle.Comment.model.Comment_;
import com.doodle.Comment.model.Reply;
import com.doodle.Comment.model.ReportReason;
import com.doodle.Tool.AppConstants;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
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

    //https://www.liker.com/get_post_comment_reply_list

    @POST(AppConstants.GET_POST_COMMENT_REPLY_LIST)
    @FormUrlEncoded
    Call<List<Reply>> getPostCommentReplyList(
            @Header("Device-Id") String deviceId,
            @Header("User-Id") String userId,
            @Header("Security-Token") String token,
            @Field("comment_id") String commentId,
            @Field("is_public") String isPublic,
            @Field("limit") int limit,
            @Field("offset") int offset,
            @Field("post_id") String postId,
            @Field("user_id") String userIds
    );


    @POST(AppConstants.GET_REPORT_REASON)
    @FormUrlEncoded
    Call<ReportReason> getReportReason(
            @Header("Device-Id") String deviceId,
            @Header("User-Id") String userId,
            @Header("Security-Token") String token,
            @Field("blocked_user_id") String blockedUserId,
            @Field("report_type") String reportType,
            @Field("user_id") String userIds
    );

    @POST(AppConstants.REPORT_USER)
    @FormUrlEncoded
    Call<String> reportUser(
            @Header("Device-Id") String deviceId,
            @Header("User-Id") String userId,
            @Header("Security-Token") String token,
            @Field("comment_id") String commentId,
            @Field("msg") String message,
            @Field("post_id") String postId,
            @Field("reasonid") String reasonid,
            @Field("report_type") String reportType,
            @Field("send_type") String sendType,
            @Field("to_id") String toId,
            @Field("user_id") String userIds
    );

    @POST(AppConstants.UN_FOLLOW)
    @FormUrlEncoded
    Call<String> unfollow(
            @Header("Device-Id") String deviceId,
            @Header("User-Id") String userId,
            @Header("Security-Token") String token,
            @Field("follow_id") String toId,
            @Field("user_id") String userIds
    );

//http://192.168.0.77:8040/sites/likerapp/blocked_user

    @POST(AppConstants.BLOCKED_USER)
    @FormUrlEncoded
    Call<String> blockedUser(
            @Header("Device-Id") String deviceId,
            @Header("User-Id") String userId,
            @Header("Security-Token") String token,
            @Field("block_user_id") String blockUserId,
            @Field("user_id") String userIds
            );

/*
comment_id	715
msg	xvv
post_id	13596
reasonid	1
report_type	4
send_type	1
to_id	28834
user_id	28827
 */


    @Multipart
    @POST(AppConstants.ADDED_COMMENTS)
    Call<Comment_> addedComment(
            @Header("Device-Id") String deviceId,
            @Header("User-Id") String userId,
            @Header("Security-Token") String token,
            @Part MultipartBody.Part file,
            @Part("comment_text") String commentText,
            @Part("comment_type") int commentType,
            @Part("has_mention") int has_mention,
            @Part("link_url") String linkUrl,
            @Part("mention") String mention,
            @Part("post_id") String postId,
            @Part("user_id") String userIds


    );

    //https://www.stg.liker.com/add_comment_reply

    @Multipart
    @POST(AppConstants.ADDED_COMMENT_REPLY)
    Call<Comment_> addedCommentReply(
            @Header("Device-Id") String deviceId,
            @Header("User-Id") String userId,
            @Header("Security-Token") String token,
            @Part("comment_id") String commentId,
            @Part MultipartBody.Part file,
            @Part("comment_text") String commentText,
            @Part("comment_type") int commentType,
            @Part("mention") String mention,
            @Part("link_url") String linkUrl,
            @Part("replies_user_id") String repliesUserId,
            @Part("post_id") String postId,
            @Part("user_id") String userIds
    );


    @Multipart
    @POST(AppConstants.EDIT_POST_COMMENT)
    Call<Comment_> editPostComment(
            @Header("Device-Id") String deviceId,
            @Header("User-Id") String userId,
            @Header("Security-Token") String token,
            @Part("change_comment_text") String changeCommentText,
            @Part("change_image") String changeImage,
            @Part("comment_id") String commentId,
            @Part MultipartBody.Part file,
            @Part("comment_text") String commentText,
            @Part("comment_type") int commentType,
            @Part("has_mention") int hasMention,
            @Part("is_change") boolean isChange,
            @Part("link_url") String linkUrl,
            @Part("mention") String mention,
            @Part("post_id") String postId,
            @Part("user_id") String userIds
    );

    @Multipart
    @POST(AppConstants.EDIT_COMMENT_REPLY)
    Call<Comment_> editCommentReply(
            @Header("Device-Id") String deviceId,
            @Header("User-Id") String userId,
            @Header("Security-Token") String token,
            @Part("change_comment_text") String changeCommentText,
            @Part("change_image") String changeImage,
            @Part("comment_id") String commentId,
            @Part MultipartBody.Part file,
            @Part("comment_reply_id") String commentReplyId,
            @Part("comment_text") String commentText,
            @Part("comment_type") int commentType,
            @Part("has_mention") int hasMention,
            @Part("is_change") boolean isChange,
            @Part("link_url") String linkUrl,
            @Part("mention") String mention,
            @Part("post_id") String postId,
            @Part("user_id") String userIds
    );

    @POST(AppConstants.DELETE_POST_COMMENT)
    @FormUrlEncoded
    Call<String> deletePostComment(
            @Header("Device-Id") String deviceId,
            @Header("User-Id") String userId,
            @Header("Security-Token") String token,
            @Field("commented_id") String commentId,
            @Field("post_id") String postId,
            @Field("user_id") String userIds
    );

    @POST(AppConstants.DELETE_COMMENT_REPLY)
    @FormUrlEncoded
    Call<String> deleteCommentReply(
            @Header("Device-Id") String deviceId,
            @Header("User-Id") String userId,
            @Header("Security-Token") String token,
            @Field("comment_id") String commentId,
            @Field("comment_reply_id") String commentReplyId,
            @Field("post_id") String postId,
            @Field("user_id") String userIds
    );

    @POST(AppConstants.COMMENT_LIKE)
    @FormUrlEncoded
    Call<String> commentLike(
            @Header("Device-Id") String deviceId,
            @Header("User-Id") String userId,
            @Header("Security-Token") String token,
            @Field("commented_id") String commentId,
            @Field("user_id") String userIds
    );

    @POST(AppConstants.COMMENT_UNLIKE)
    @FormUrlEncoded
    Call<String> commentUnLike(
            @Header("Device-Id") String deviceId,
            @Header("User-Id") String userId,
            @Header("Security-Token") String token,
            @Field("commented_id") String commentId,
            @Field("user_id") String userIds
    );
}
