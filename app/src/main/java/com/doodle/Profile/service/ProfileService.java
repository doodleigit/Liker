package com.doodle.Profile.service;

import com.doodle.Profile.model.AlbumPhoto;
import com.doodle.Profile.model.AllFriend;
import com.doodle.Profile.model.PhoneCountry;
import com.doodle.Profile.model.PhotoAlbum;
import com.doodle.Profile.model.RecentPhoto;
import com.doodle.Profile.model.Star;
import com.doodle.Profile.model.UserAllInfo;
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

    @POST(AppConstants.GET_USER_INFO_BY_USER_NAME)
    @FormUrlEncoded
    Call<UserAllInfo> getUserInfo(
            @Header("Device-Id") String deviceId,
            @Header("User-Id") String userId,
            @Header("Security-Token") String token,
            @Field("user_id") String userIds,
            @Field("user_name") String userName,
            @Field("get_followers") boolean getFollowers
    );

    @Multipart
    @POST(AppConstants.UPLOAD_PROFILE_IMAGE)
    Call<String> uploadProfileImage(
            @Header("Device-Id") String deviceId,
            @Header("User-Id") String userId,
            @Header("Security-Token") String token,
            @Part MultipartBody.Part file

    );

    @Multipart
    @POST(AppConstants.UPLOAD_COVER_IMAGE)
    Call<String> uploadCoverImage(
            @Header("Device-Id") String deviceId,
            @Header("User-Id") String userId,
            @Header("Security-Token") String token,
            @Part MultipartBody.Part file

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

    @POST(AppConstants.GET_PROFILE_INFO)
    @FormUrlEncoded
    Call<String> getProfileInfo(
            @Header("Device-Id") String deviceId,
            @Header("Security-Token") String token,
            @Header("User-Id") String userId,
            @Field("profile_user_id") String profileUserId,
            @Field("user_id") String userIds
    );

    @POST(AppConstants.GET_EDUCATION_SUGGESTION)
    @FormUrlEncoded
    Call<String> getSuggestion(
            @Header("Device-Id") String deviceId,
            @Header("Security-Token") String token,
            @Header("User-Id") String userId,
            @Field("suggesion_type") String suggestionType,
            @Field("search_string") String searchKey
    );

    @POST(AppConstants.GET_EXPERIENCE_SUGGESTION)
    @FormUrlEncoded
    Call<String> getExperienceSuggestion(
            @Header("Device-Id") String deviceId,
            @Header("Security-Token") String token,
            @Header("User-Id") String userId,
            @Field("suggesion_type") String suggestionType,
            @Field("search_string") String searchKey
    );

    @POST(AppConstants.GET_AWARDS_SUGGESTION)
    @FormUrlEncoded
    Call<String> getAwardsSuggestion(
            @Header("Device-Id") String deviceId,
            @Header("Security-Token") String token,
            @Header("User-Id") String userId,
            @Field("suggesion_type") String suggestionType,
            @Field("search_string") String searchKey
    );

    @POST(AppConstants.GET_CERTIFICATE_SUGGESTION)
    @FormUrlEncoded
    Call<String> getCertificateSuggestion(
            @Header("Device-Id") String deviceId,
            @Header("Security-Token") String token,
            @Header("User-Id") String userId,
            @Field("suggesion_type") String suggestionType,
            @Field("search_string") String searchKey
    );

    @POST(AppConstants.INSERT_EDUCATION)
    @FormUrlEncoded
    Call<String> addEducation(
            @Header("Device-Id") String deviceId,
            @Header("Security-Token") String token,
            @Header("User-Id") String userId,
            @Field("user_id") String user_id,
            @Field("institute_name") String institute_name,
            @Field("institute_type") String institute_type,
            @Field("degree_name") String degree_name,
            @Field("field_study_name") String field_study_name,
            @Field("website_url") String website_url,
            @Field("location_name") String location_name,
            @Field("permission_type") String permission_type,
            @Field("grade") String grade,
            @Field("start_year") String start_year,
            @Field("end_year") String end_year,
            @Field("description") String description,
            @Field("location_actual_name") String location_actual_name,
            @Field("location_country_name") String location_country_name,
            @Field("location_latitude") String location_latitude,
            @Field("location_longitude") String location_longitude
    );

    @POST(AppConstants.INSERT_EXPERIENCE)
    @FormUrlEncoded
    Call<String> addExperience(
            @Header("Device-Id") String deviceId,
            @Header("Security-Token") String token,
            @Header("User-Id") String userId,
            @Field("user_id") String user_id,
            @Field("designation_name") String designation_name,
            @Field("company_name") String company_name,
            @Field("website_url") String website_url,
            @Field("from_date") String from_date,
            @Field("to_date") String to_date,
            @Field("currently_worked") boolean currently_worked,
            @Field("permission_type") String permission_type,
            @Field("description") String description,
            @Field("location_name") String location_name,
            @Field("location_actual_name") String location_actual_name,
            @Field("location_country_name") String location_country_name,
            @Field("location_latitude") String location_latitude,
            @Field("location_longitude") String location_longitude
    );

    @POST(AppConstants.INSERT_AWARDS)
    @FormUrlEncoded
    Call<String> addAwards(
            @Header("Device-Id") String deviceId,
            @Header("Security-Token") String token,
            @Header("User-Id") String userId,
            @Field("user_id") String user_id,
            @Field("institute_name") String institute_name,
            @Field("institute_type") String institute_type,
            @Field("website_url") String website_url,
            @Field("awards_name") String awards_name,
            @Field("date") String date,
            @Field("permission_type") String permission_type,
            @Field("description") String description,
            @Field("location_name") String location_name,
            @Field("location_actual_name") String location_actual_name,
            @Field("location_country_name") String location_country_name,
            @Field("location_latitude") String location_latitude,
            @Field("location_longitude") String location_longitude
    );

    @POST(AppConstants.INSERT_CERTIFICATE)
    @FormUrlEncoded
    Call<String> addCertificate(
            @Header("Device-Id") String deviceId,
            @Header("Security-Token") String token,
            @Header("User-Id") String userId,
            @Field("user_id") String user_id,
            @Field("institute_name") String institute_name,
            @Field("institute_type") String institute_type,
            @Field("website_url") String website_url,
            @Field("certification_name") String certification_name,
            @Field("license_number") String license_number,
            @Field("certification_url") String certification_url,
            @Field("from_date") String from_date,
            @Field("is_expired") boolean is_expired,
            @Field("to_date") String to_date,
            @Field("permission_type") String permission_type,
            @Field("media") String media,
            @Field("location_name") String location_name,
            @Field("location_actual_name") String location_actual_name,
            @Field("location_country_name") String location_country_name,
            @Field("location_latitude") String location_latitude,
            @Field("location_longitude") String location_longitude
    );

    @POST(AppConstants.INSERT_LINK)
    @FormUrlEncoded
    Call<String> addLink(
            @Header("Device-Id") String deviceId,
            @Header("Security-Token") String token,
            @Header("User-Id") String userId,
            @Field("user_id") String user_id,
            @Field("link") String link,
            @Field("type") String type,
            @Field("permission_type") String permission_type
    );

    @POST(AppConstants.UPDATE_EDUCATION)
    @FormUrlEncoded
    Call<String> updateEducation(
            @Header("Device-Id") String deviceId,
            @Header("Security-Token") String token,
            @Header("User-Id") String userId,
            @Field("user_id") String user_id,
            @Field("education_id") String education_id,
            @Field("institute_name") String institute_name,
            @Field("institute_type") String institute_type,
            @Field("degree_name") String degree_name,
            @Field("field_study_name") String field_study_name,
            @Field("website_url") String website_url,
            @Field("location_name") String location_name,
            @Field("permission_type") String permission_type,
            @Field("grade") String grade,
            @Field("start_year") String start_year,
            @Field("end_year") String end_year,
            @Field("description") String description,
            @Field("location_actual_name") String location_actual_name,
            @Field("location_country_name") String location_country_name,
            @Field("location_latitude") String location_latitude,
            @Field("location_longitude") String location_longitude
    );

    @POST(AppConstants.UPDATE_EXPERIENCE)
    @FormUrlEncoded
    Call<String> updateExperience(
            @Header("Device-Id") String deviceId,
            @Header("Security-Token") String token,
            @Header("User-Id") String userId,
            @Field("user_id") String user_id,
            @Field("experience_id") String experience_id,
            @Field("designation_name") String designation_name,
            @Field("company_name") String company_name,
            @Field("website_url") String website_url,
            @Field("from_date") String from_date,
            @Field("to_date") String to_date,
            @Field("currently_worked") boolean currently_worked,
            @Field("permission_type") String permission_type,
            @Field("description") String description,
            @Field("location_name") String location_name,
            @Field("location_actual_name") String location_actual_name,
            @Field("location_country_name") String location_country_name,
            @Field("location_latitude") String location_latitude,
            @Field("location_longitude") String location_longitude
    );

    @POST(AppConstants.UPDATE_AWARDS)
    @FormUrlEncoded
    Call<String> updateAwards(
            @Header("Device-Id") String deviceId,
            @Header("Security-Token") String token,
            @Header("User-Id") String userId,
            @Field("user_id") String user_id,
            @Field("awards_id") String awards_id,
            @Field("institute_name") String institute_name,
            @Field("institute_type") String institute_type,
            @Field("website_url") String website_url,
            @Field("awards_name") String awards_name,
            @Field("date") String date,
            @Field("permission_type") String permission_type,
            @Field("description") String description,
            @Field("location_name") String location_name,
            @Field("location_actual_name") String location_actual_name,
            @Field("location_country_name") String location_country_name,
            @Field("location_latitude") String location_latitude,
            @Field("location_longitude") String location_longitude
    );

    @POST(AppConstants.UPDATE_CERTIFICATE)
    @FormUrlEncoded
    Call<String> updateCertificate(
            @Header("Device-Id") String deviceId,
            @Header("Security-Token") String token,
            @Header("User-Id") String userId,
            @Field("user_id") String user_id,
            @Field("users_certification_id") String users_certification_id,
            @Field("institute_name") String institute_name,
            @Field("institute_type") String institute_type,
            @Field("website_url") String website_url,
            @Field("certification_name") String certification_name,
            @Field("license_number") String license_number,
            @Field("certification_url") String certification_url,
            @Field("from_date") String from_date,
            @Field("is_expired") boolean is_expired,
            @Field("to_date") String to_date,
            @Field("permission_type") String permission_type,
            @Field("media") String media,
            @Field("location_name") String location_name,
            @Field("location_actual_name") String location_actual_name,
            @Field("location_country_name") String location_country_name,
            @Field("location_latitude") String location_latitude,
            @Field("location_longitude") String location_longitude
    );

    @POST(AppConstants.UPDATE_LINK)
    @FormUrlEncoded
    Call<String> updateLink(
            @Header("Device-Id") String deviceId,
            @Header("Security-Token") String token,
            @Header("User-Id") String userId,
            @Field("user_id") String user_id,
            @Field("link_old") String link_old,
            @Field("link_new") String link_new,
            @Field("type") String type,
            @Field("permission_type") String permission_type
    );

    @POST(AppConstants.REMOVE_EDUCATION)
    @FormUrlEncoded
    Call<String> removeEducation(
            @Header("Device-Id") String deviceId,
            @Header("Security-Token") String token,
            @Header("User-Id") String userId,
            @Field("user_id") String user_id,
            @Field("education_id") String education_id
    );

    @POST(AppConstants.REMOVE_EXPERIENCE)
    @FormUrlEncoded
    Call<String> removeExperience(
            @Header("Device-Id") String deviceId,
            @Header("Security-Token") String token,
            @Header("User-Id") String userId,
            @Field("user_id") String user_id,
            @Field("experience_id") String experience_id
    );

    @POST(AppConstants.REMOVE_AWARDS)
    @FormUrlEncoded
    Call<String> removeAwards(
            @Header("Device-Id") String deviceId,
            @Header("Security-Token") String token,
            @Header("User-Id") String userId,
            @Field("user_id") String user_id,
            @Field("awards_id") String awards_id
    );

    @POST(AppConstants.REMOVE_CERTIFICATION)
    @FormUrlEncoded
    Call<String> removeCertification(
            @Header("Device-Id") String deviceId,
            @Header("Security-Token") String token,
            @Header("User-Id") String userId,
            @Field("user_id") String user_id,
            @Field("certification_id") String certification_id
    );

    @POST(AppConstants.REMOVE_LINKS)
    @FormUrlEncoded
    Call<String> removeSocialLinks(
            @Header("Device-Id") String deviceId,
            @Header("Security-Token") String token,
            @Header("User-Id") String userId,
            @Field("user_id") String user_id,
            @Field("link") String link
    );

    @POST(AppConstants.STAR_LIST)
    @FormUrlEncoded
    Call<ArrayList<Star>> getStarList(
            @Header("Device-Id") String deviceId,
            @Header("Security-Token") String token,
            @Header("User-Id") String userId,
            @Field("profile_username") String profile_username
    );

    @POST(AppConstants.EMAIL_ADD)
    @FormUrlEncoded
    Call<String> addEmail(
            @Header("Device-Id") String deviceId,
            @Header("Security-Token") String token,
            @Header("User-Id") String userId,
            @Field("user_id") String id,
            @Field("email") String email,
            @Field("permission_type") String permissionType
    );

    @POST(AppConstants.PHONE_ADD)
    @FormUrlEncoded
    Call<String> addPhone(
            @Header("Device-Id") String deviceId,
            @Header("Security-Token") String token,
            @Header("User-Id") String userId,
            @Field("user_id") String id,
            @Field("phone_number") String phoneNumber,
            @Field("type") String type,
            @Field("permission_type") String permissionType,
            @Field("country_id") String countryId
    );

    @POST(AppConstants.SET_STORY)
    @FormUrlEncoded
    Call<String> setStory(
            @Header("Device-Id") String deviceId,
            @Header("Security-Token") String token,
            @Header("User-Id") String userId,
            @Field("user_id") String id,
            @Field("type") String type,
            @Field("permission_type") String permissionType,
            @Field("description") String description
    );

    @POST(AppConstants.SET_LIVE_PLACE)
    @FormUrlEncoded
    Call<String> setLivePlace(
            @Header("Device-Id") String deviceId,
            @Header("Security-Token") String token,
            @Header("User-Id") String userId,
            @Field("user_id") String id,
            @Field("country_id") String countryId,
            @Field("city_id") String cityId,
            @Field("type") String type,
            @Field("permission_type") String permissionType
    );

    @POST(AppConstants.GET_CITY_LIST)
    @FormUrlEncoded
    Call<ArrayList<Star>> getCityList(
            @Header("Device-Id") String deviceId,
            @Header("Security-Token") String token,
            @Header("User-Id") String userId,
            @Field("user_id") String id,
            @Field("country_id") String countryId
    );

    @POST(AppConstants.GET_COUNTRY_PHONE_CODES)
    @FormUrlEncoded
    Call<ArrayList<PhoneCountry>> getCountryPhoneCodes(
            @Header("Device-Id") String deviceId,
            @Header("Security-Token") String token,
            @Header("User-Id") String userId,
            @Field("user_id") String id
    );

}
