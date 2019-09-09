package com.doodle.Authentication.service;


import com.doodle.Authentication.model.City;
import com.doodle.Authentication.model.Country;
import com.doodle.Authentication.model.ForgotPassword;
import com.doodle.Authentication.model.LoginUser;
import com.doodle.Authentication.model.ResendStatus;
import com.doodle.Tool.AppConstants;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface AuthService {

/*    OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .connectTimeout(1, TimeUnit.MINUTES)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .build();*/

    Retrofit retrofitForCity = new Retrofit.Builder()
            .baseUrl(AppConstants.BASE_URL_LOCATION)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    //    Retrofit retrofitForSignup = new Retrofit.Builder()
//            .baseUrl(AppConstants.BASE_URL)
//            .addConverterFactory(ScalarsConverterFactory.create())
//            .build();
    Retrofit retrofitForCountry = new Retrofit.Builder()
            .baseUrl(AppConstants.BASE_URL_LOCATION)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    Retrofit retrofitBase = new Retrofit.Builder()
            .baseUrl(AppConstants.BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build();
//    Retrofit retrofitForResend = new Retrofit.Builder()
//            .baseUrl(AppConstants.BASE_URL)
//            .addConverterFactory(GsonConverterFactory.create())
//            .build();


    @GET(AppConstants.COUNTRY_LIST)
    Call<Country> getCountry();
//
//    @POST(AppConstants.SIGN_UP)
//    Call<String> math(@Path("number") String number);

    @POST(AppConstants.SIGN_UP)
    @FormUrlEncoded
    Call<String> registerUser(@Field("first_name") String first_name,
                              @Field("last_name") String last_name,
                              @Field("email") String email,
                              @Field("password") String password,
                              @Field("retype_password") String retype_password,
                              @Field("gender") String gender,
                              @Field("country") String country,
                              @Field("day") String day,
                              @Field("month") String month,
                              @Field("year") String year,
                              @Field("city") String city,
                              @Field("provider") String provider,
                              @Field("oauth_id") String oauth_id,
                              @Field("token") String token,
                              @Field("secret") String secret,
                              @Field("social_name") String social_name,
                              @Field("is_apps") String is_apps,
                              @Field("img_url") String img_url
    );

    @POST(AppConstants.LOGIN_NEW)
    @FormUrlEncoded
    Call<LoginUser> loginUser(
            @Field("email") String email,
            @Field("password") String password,
            @Field("device_id") String device_id
    );

    @POST(AppConstants.FORGOT_PASSWORD_NEW)
    @FormUrlEncoded
    Call<ForgotPassword> forgotPassword(
            @Field("email") String email
    );


    @POST(AppConstants.OTP)
    @FormUrlEncoded
    Call<String> setOTP(
            @Field("user_id") String userId,
            @Field("code") String code
    );

    @POST(AppConstants.RESET_PASSWORD)
    @FormUrlEncoded
    Call<String> setNewPassword(
            @Field("user_id") String userId,
            @Field("code") String code,
            @Field("password") String password,
            @Field("confirm_password") String confirm_password
    );

    @POST(AppConstants.LOGIN_WITH_OTP_APPS)
    @FormUrlEncoded
    Call<String> setOTPLogin(
            @Field("user_id") String userId,
            @Field("device_id") String deviceId,
            @Field("verify_otp") String verifyOTP
    );

    //resend_signup_otp
    @POST(AppConstants.RESEND_SIGNUP_OTP)
    @FormUrlEncoded
    Call<String> resendSignUpOTP(
            @Field("user_id") String userId

    );

    @POST(AppConstants.SOCIAL_LOGIN_APPS)
    @FormUrlEncoded
    Call<LoginUser> socialLogin(
            @Field("app_social_access_code") String appSocialAccessCode,
            @Field("oauth_provider") String oauthProvider,
            @Field("oauth_id") String oauthId,
            @Field("device_id") String deviceId
    );


    @POST(AppConstants.RESEND + "/{userId}")
    Call<ResendStatus> resendEmail(@Path(value = "userId", encoded = true) String userId);


    @POST(AppConstants.CITY_LIST)
    @FormUrlEncoded
    Call<City> cities(@Field("country_id") int country_id);

    @GET("{number}")
    Call<String> trivia(@Path("number") String number);


}
