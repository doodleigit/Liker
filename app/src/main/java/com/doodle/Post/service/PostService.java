package com.doodle.Post.service;

import com.doodle.Post.model.CategoryItem;
import com.doodle.Post.model.MentionUser;
import com.doodle.Post.model.MultipleMediaFile;
import com.doodle.utils.AppConstants;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

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

import static com.doodle.utils.RestAdapter.getUnsafeOkHttpClient;

public interface PostService {


    Retrofit mRetrofit = new Retrofit.Builder()
            .baseUrl(AppConstants.BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build();


//    URL url = new URL("https://wikipedia.org");
//    URLConnection urlConnection = url.openConnection();
//    InputStream in = urlConnection.getInputStream();
//    copyInputStreamToOutputStream(in);

    //    Retrofit videoRetrofit = new Retrofit.Builder()
//            .baseUrl(AppConstants.SOCKET_VIDEO)
//            .addConverterFactory(ScalarsConverterFactory.create())
//            .addConverterFactory(GsonConverterFactory.create())
//            .build();
    Retrofit videoRetrofit = new Retrofit.Builder()
            .baseUrl(AppConstants.SOCKET_VIDEO)
            .client(getUnsafeOkHttpClient().build())
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    Retrofit postRetrofit = new Retrofit.Builder()
            .baseUrl(AppConstants.BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    @POST(AppConstants.GET_CATEGORIES)
        // @FormUrlEncoded
    Call<CategoryItem> getCategories(
            @Header("Device-Id") String deviceId,
            @Header("User-Id") String userId,
            @Header("Security-Token") String token
    );

    @POST(AppConstants.POST_ADDED)
    @FormUrlEncoded
    Call<String> postAdded(
            @Header("Device-Id") String deviceId,
            @Header("User-Id") String userId,
            @Header("Security-Token") String token,
            @Field("user_id") int userIds,
            @Field("to_user_id") String toUserId,
            @Field("post_type") String postType,
            @Field("status") int status,
            @Field("image") String image,
            @Field("file_encode") String fileEncoded,
            @Field("post_permission") int postPermission,
            @Field("category_id") int categoryId,
            @Field("sub_category_id") int subCategoryId,
            @Field("content_type") int contentType,
            @Field("content_title") String contentTitle,
            @Field("content_link_url") String contentLinkUrl,
            @Field("content_link_host") String contentLinkHost,
            @Field("content_link_title") String contentLinkTitle,
            @Field("content_link_desc") String contentLinkDesc,
            @Field("content_link_image") String contentLinkImage,
            @Field("video_tmp_link") String videoTmpLink,
            @Field("user_name") String userName,
            @Field("friends") String friends,
            @Field("schedule_time") int scheduleTime,
            @Field("has_meme") int hasMeme,
            @Field("files") String mediaFiles
    );



    @POST(AppConstants.POST_EDITED)
    @FormUrlEncoded
    Call<String> postEdited(
            @Header("Device-Id") String deviceId,
            @Header("User-Id") String userId,
            @Header("Security-Token") String token,
            @Field("user_id") int userIds,
            @Field("to_user_id") String toUserId,
            @Field("post_type") String postType,
            @Field("status") int status,
            @Field("image") String image,
            @Field("file_encode") String fileEncoded,
            @Field("post_permission") int postPermission,
            @Field("category_id") int categoryId,
            @Field("sub_category_id") int subCategoryId,
            @Field("content_type") int contentType,
            @Field("content_title") String contentTitle,
            @Field("content_link_url") String contentLinkUrl,
            @Field("content_link_host") String contentLinkHost,
            @Field("content_link_title") String contentLinkTitle,
            @Field("content_link_desc") String contentLinkDesc,
            @Field("content_link_image") String contentLinkImage,
            @Field("video_tmp_link") String videoTmpLink,
            @Field("user_name") String userName,
            @Field("friends") String friends,
            @Field("schedule_time") int scheduleTime,
            @Field("has_meme") int hasMeme,
            @Field("content_new_scrap") int contentNewScrap,
            @Field("post_id") String postId
    );

/*


content_new_scrap	0
post_id	13656



* */


    @Multipart
    @POST(AppConstants.ADD_PHOTO)
    Call<String> postImage(
            @Header("Device-Id") String deviceId,
            @Header("User-Id") String userId,
            @Header("Security-Token") String token,
            @Part MultipartBody.Part file

    );

    @Multipart
    @POST(AppConstants.UPLOAD_VIDEO)
    Call<String> uploadVideo(
            @Header("Device-Id") String deviceId,
            @Header("User-Id") String userId,
            @Header("Security-Token") String token,
            @Part MultipartBody.Part file,
            @Part("post_id") int postId,
            @Part("is_apps") boolean isApps


    );

    /*http://192.168.0.77:4004/upload
    is_apps: 'true'
    video: binary file*/
    @POST(AppConstants.SEARCH_MENTION_USER)
    @FormUrlEncoded
    Call<List<MentionUser>> searchMentionUser(
            @Header("Device-Id") String deviceId,
            @Header("User-Id") String userId,
            @Header("Security-Token") String token,
            @Field("name") String name
    );

    //http://192.168.0.77:8040/sites/likeropt/addedPostContributor
    @POST(AppConstants.ADDED_POST_CONTRIBUTOR)
    @FormUrlEncoded
    Call<String> addedPostContributor(
            @Header("Device-Id") String deviceId,
            @Header("User-Id") String userId,
            @Header("Security-Token") String token,
            @Field("catg") int category,
            @Field("subcatg") int subCategory,
            @Field("feed") int feed,
            @Field("user_id") String userIds
    );

    @POST(AppConstants.IS_DUPLICATE_FILE)
    @FormUrlEncoded
    Call<String> isDuplicateFile(
            @Header("Device-Id") String deviceId,
            @Header("User-Id") String userId,
            @Header("Security-Token") String token,
            @Field("user_id") String userIds,
            @Field("content_type") String contentType,
            @Field("file_encode") String fileEncode
    );



 /*   @POST(AppConstants.UPLOAD_VIDEO)
    @FormUrlEncoded
    Call<String> uploadVideo(
            @Header("Device-Id") String deviceId,
            @Header("User-Id") String userId,
            @Header("Security-Token") String token,
            @Field("file_content") String fileContent,
            @Field("post_id") int postId,
            @Field("extension") String extension
    );*/


}
