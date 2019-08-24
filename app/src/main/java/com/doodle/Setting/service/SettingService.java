package com.doodle.Setting.service;

import com.doodle.Setting.model.AccountSetting;
import com.doodle.Setting.model.AllEmail;
import com.doodle.Setting.model.Contribution;
import com.doodle.Setting.model.ContributionItem;
import com.doodle.Setting.model.Friend;
import com.doodle.Setting.model.PeopleMayKnow;
import com.doodle.Setting.model.PrivacyInfo;
import com.doodle.Setting.model.PrivacyOnOff;
import com.doodle.Setting.model.Social;
import com.doodle.Setting.model.SocialFriend;
import com.doodle.Setting.model.SocialLink;
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

public interface SettingService {

    Retrofit mRetrofit = new Retrofit.Builder()
            .baseUrl(AppConstants.BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    @POST(AppConstants.PRIVACY_VIEW)
    @FormUrlEncoded
    Call<PrivacyInfo> getPrivacyAndSecuritySetting(
            @Header("Device-Id") String deviceId,
            @Header("User-Id") String userId,
            @Header("Security-Token") String token,
            @Field("user_id") String userIds
    );

    @POST(AppConstants.PRIVACY_UPDATE_PERMISSION)
    @FormUrlEncoded
    Call<String> setPrivacyUpdate(
            @Header("Device-Id") String deviceId,
            @Header("User-Id") String userId,
            @Header("Security-Token") String token,
            @Field("user_id") String userIds,
            @Field("type") String type,
            @Field("value") String value
    );

    @POST(AppConstants.SEARCH_USER)
    @FormUrlEncoded
    Call<ArrayList<Friend>> getSearchUser(
            @Header("Device-Id") String deviceId,
            @Header("User-Id") String userId,
            @Header("Security-Token") String token,
            @Field("name") String searchQuery
    );

    @POST(AppConstants.BLOCKED_USER)
    @FormUrlEncoded
    Call<String> setBlockedUser(
            @Header("Device-Id") String deviceId,
            @Header("User-Id") String userId,
            @Header("Security-Token") String token,
            @Field("user_id") String id,
            @Field("block_user_id") String blockUserId
    );

    @POST(AppConstants.UNBLOCKED_USER)
    @FormUrlEncoded
    Call<String> setUnBlockedUser(
            @Header("Device-Id") String deviceId,
            @Header("User-Id") String userId,
            @Header("Security-Token") String token,
            @Field("user_id") String id,
            @Field("blocked_id") String blockedId
    );

    @POST(AppConstants.GET_ON_OFFS_BY_USER_ID)
    @FormUrlEncoded
    Call<PrivacyOnOff> getPrivacyOnOff(
            @Header("Device-Id") String deviceId,
            @Header("User-Id") String userId,
            @Header("Security-Token") String token,
            @Field("user_id") String id
    );

    @POST(AppConstants.NOTIFICATION_ON_OFF)
    @FormUrlEncoded
    Call<String> setNotificationOnOff(
            @Header("Device-Id") String deviceId,
            @Header("User-Id") String userId,
            @Header("Security-Token") String token,
            @Field("user_id") String id,
            @Field("single_status") String singleStatus,
            @Field("fileds[]") ArrayList<String> fileds
    );

    @POST(AppConstants.SET_CONTRIBUTOR_CATEGORY)
    @FormUrlEncoded
    Call<String> setContributorCategory(
            @Header("Device-Id") String deviceId,
            @Header("User-Id") String userId,
            @Header("Security-Token") String token,
            @Field("user_id") String id,
            @Field("category_id") String category_id,
            @Field("action_type") String action_type,
            @Field("sub_category_id") String sub_category_id
    );

    @POST(AppConstants.GET_CONTRIBUTOR_CATEGORY)
    @FormUrlEncoded
    Call<Contribution> getContributorCategory(
            @Header("Device-Id") String deviceId,
            @Header("User-Id") String userId,
            @Header("Security-Token") String token,
            @Field("user_id") String id
    );

    @POST(AppConstants.CONTRIBUTOR_VIEW)
    @FormUrlEncoded
    Call<ContributionItem> getContributorView(
            @Header("Device-Id") String deviceId,
            @Header("User-Id") String userId,
            @Header("Security-Token") String token,
            @Field("user_id") String id
    );

    @POST(AppConstants.ACCOUNT_VIEW)
    @FormUrlEncoded
    Call<AccountSetting> getAccountView(
            @Header("Device-Id") String deviceId,
            @Header("User-Id") String userId,
            @Header("Security-Token") String token,
            @Field("user_id") String id
    );

    @POST(AppConstants.GET_EMAILS)
    @FormUrlEncoded
    Call<AllEmail> getEmails(
            @Header("Device-Id") String deviceId,
            @Header("User-Id") String userId,
            @Header("Security-Token") String token,
            @Field("user_id") String id
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

    @POST(AppConstants.EMAIL_REMOVE)
    @FormUrlEncoded
    Call<String> removeEmail(
            @Header("Device-Id") String deviceId,
            @Header("Security-Token") String token,
            @Header("User-Id") String userId,
            @Field("user_id") String id,
            @Field("email") String email
    );

    @POST(AppConstants.UPDATE_PASSWORD)
    @FormUrlEncoded
    Call<String> updatePassword(
            @Header("Device-Id") String deviceId,
            @Header("Security-Token") String token,
            @Header("User-Id") String userId,
            @Field("user_id") String id,
            @Field("old_password") String oldPassword,
            @Field("new_password") String newPassword,
            @Field("new_password_confirm") String newPasswordConfirm
    );

    @POST(AppConstants.DEACTIVATED_ACCOUNT)
    @FormUrlEncoded
    Call<String> deactivatedAccount(
            @Header("Device-Id") String deviceId,
            @Header("Security-Token") String token,
            @Header("User-Id") String userId,
            @Field("user_id") String id
    );

    @POST(AppConstants.EMAIL_VERIFICATION_SEND)
    @FormUrlEncoded
    Call<String> emailVerificationSend(
            @Header("Device-Id") String deviceId,
            @Header("Security-Token") String token,
            @Header("User-Id") String userId,
            @Field("user_id") String id,
            @Field("email") String email
    );

    @POST(AppConstants.UPDATE_SECURITY_QUESTION)
    @FormUrlEncoded
    Call<String> updateSecurityQuestion(
            @Header("Device-Id") String deviceId,
            @Header("Security-Token") String token,
            @Header("User-Id") String userId,
            @Field("user_id") String id,
            @Field("question_id") String questionId,
            @Field("answer") String answer
    );

    @POST(AppConstants.PEOPLE_YOU_MAY_KNOW)
    @FormUrlEncoded
    Call<ArrayList<PeopleMayKnow>> peopleYouMayKnow(
            @Header("Device-Id") String deviceId,
            @Header("Security-Token") String token,
            @Header("User-Id") String userId,
            @Field("user_id") String userIds,
            @Field("suggested_ids") String suggested_ids,
            @Field("limit") int limit,
            @Field("offset") int offset
    );

    @POST(AppConstants.GET_SOCIAL_FRIENDS)
    @FormUrlEncoded
    Call<Social> getSocialFriends(
            @Header("Device-Id") String deviceId,
            @Header("Security-Token") String token,
            @Header("User-Id") String userId,
            @Field("user_id") String userIds,
            @Field("social_type") String socialType,
            @Field("limit") int limit,
            @Field("offset") int offset
    );

    @POST(AppConstants.GET_SOCIAL_LINKS)
    @FormUrlEncoded
    Call<SocialLink> getSocialLinks(
            @Header("Device-Id") String deviceId,
            @Header("Security-Token") String token,
            @Header("User-Id") String userId,
            @Field("user_id") String userIds
    );

}
