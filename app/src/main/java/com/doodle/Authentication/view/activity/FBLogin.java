package com.doodle.Authentication.view.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.request.RequestOptions;
import com.doodle.App;
import com.doodle.Authentication.model.LoginUser;
import com.doodle.Authentication.model.UserInfo;
import com.doodle.Authentication.service.AuthService;
import com.doodle.Home.view.activity.Home;
import com.doodle.R;
import com.doodle.Tool.AppConstants;
import com.doodle.Tool.NetworkHelper;
import com.doodle.Tool.PrefManager;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.doodle.Tool.AppConstants.POST_IMAGES;

public class FBLogin extends AppCompatActivity {


    CircleImageView profileImage;
    TextView profileName, profileEmail;
    LoginButton loginButton;
    private PrefManager manager;
    private CallbackManager callbackManager;
    private boolean networkOk;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fblogin);

        toolbar = (Toolbar) findViewById(R.id.main_activiy_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Liker");
        networkOk = NetworkHelper.hasNetworkAccess(this);

        profileEmail = findViewById(R.id.profile_email);
        profileName = findViewById(R.id.profile_name);
        profileImage = findViewById(R.id.profile_image);
        loginButton = findViewById(R.id.login_button);
        manager = new PrefManager(this);
        callbackManager = CallbackManager.Factory.create();

//        String name=manager.getFbFirstName();
//        if (name != null) {
//            startActivity(new Intent(FBLogin.this, Liker.class));
//            finish();
//        }
        checkLoginStatus();

        loginButton.setReadPermissions(Arrays.asList("email", "public_profile"));
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                if (App.isIsFBSignup()) {
                    startActivity(new Intent(FBLogin.this, Signup.class));
                    finish();

                } else {

                    if (networkOk) {

                        String appSocialAccessCode = AppConstants.APP_SOCIAL_ACCESS_CODE;
                        String oauthProvider = AppConstants.OAUTH_PROVIDER_FB;
                        String deviceId = manager.getDeviceId();
                        String oauthId = manager.getOauthId();
                        socialLogin(appSocialAccessCode, oauthProvider, oauthId, deviceId);

                    } else {
                        Toast.makeText(FBLogin.this, "no internet!", Toast.LENGTH_SHORT).show();

                    }

                }
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    AccessTokenTracker tokenTracker = new AccessTokenTracker() {
        @Override
        protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {

            if (currentAccessToken == null) {
                profileEmail.setText("");
                profileName.setText("");
                TextView tvDetails = (TextView) findViewById(R.id.tvDetails);
                tvDetails.setText(" ");
                profileImage.setImageResource(0);
//                Toast.makeText(FBLogin.this, "User logged out", Toast.LENGTH_SHORT).show();
//                if(App.isIsFBSignup()){
//                    startActivity(new Intent(FBLogin.this, Signup.class));
//
//                }else {
//
//                    startActivity(new Intent(FBLogin.this, ForgotPasswords.class));
//                }
            } else {
                loadUserProfile(currentAccessToken);

            }
        }
    };


    private void loadUserProfile(AccessToken newAccessToken) {

        GraphRequest request = GraphRequest.newMeRequest(newAccessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {


                try {

                    Log.d("DATA", object.toString());

                    String first_name = object.getString("first_name");
                    manager.setFbFirstName(first_name);
                    String last_name = object.getString("last_name");
                    manager.setFbLastName(last_name);
                    String email = object.getString("email");
                    manager.setFbEmail(email);
                    String oauthId = object.getString("id");
                    manager.setOauthId(oauthId);
                    manager.setProfileId(oauthId);
                    String image_url = "https://graph.facebook.com/" + oauthId + "/picture?type=normal";
                    manager.setFbImageUrl(image_url);
                    manager.setProfileImage(image_url);
                    String name = object.getString("name");
                    manager.setFbName(name);
                    Log.d("name", name);
                    // String gender=object.getString("gender");
                    String birthDay = object.getString("birthday");
                    manager.setFbBirthDay(birthDay);

                    TextView tvDetails = (TextView) findViewById(R.id.tvDetails);
                    // tvDetails.setText("SocalName: " + name + "Birthday: " + birthDay);
                    String mProfileName = first_name + " " + last_name;
                    manager.setProfileName(mProfileName);
                    profileName.setText(mProfileName);
                    profileEmail.setText(email);

                    RequestOptions requestOptions = new RequestOptions();
                    requestOptions.dontAnimate();

                    Picasso.with(FBLogin.this)
                            .load(image_url)
                            .placeholder(R.drawable.ic_facebook)
                            .into(profileImage);

//                    if (networkOk) {
//
//                        String appSocialAccessCode = AppConstants.APP_SOCIAL_ACCESS_CODE;
//                        String oauthProvider = AppConstants.OAUTH_PROVIDER_FB;
//                        String deviceId = manager.getDeviceId();
//
//                        socialLogin(appSocialAccessCode, oauthProvider, oauthId, deviceId);
//
//                    } else {
//                        Toast.makeText(FBLogin.this, "no internet!", Toast.LENGTH_SHORT).show();
//
//                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });


        Bundle paramBundle = new Bundle();
//        paramBundle.putString("fields", "first_name,last_name,email,id");
        paramBundle.putString("fields", "first_name,last_name,email,id,name,gender,birthday");
        request.setParameters(paramBundle);
        request.executeAsync();
    }

    private void socialLogin(String appSocialAccessCode, String oauthProvider, String oauthId, String deviceId) {
        AuthService webService =
                AuthService.retrofitBase.create(AuthService.class);

        Call<LoginUser> call = webService.socialLogin(appSocialAccessCode, oauthProvider, oauthId, deviceId);
        sendRequest(call);
    }

    private void sendRequest(Call<LoginUser> call) {
        call.enqueue(new Callback<LoginUser>() {
            @Override
            public void onResponse(Call<LoginUser> call, Response<LoginUser> response) {
                LoginUser loginUser = response.body();
                boolean status = loginUser.isStatus();
                if (status) {
                    String mToken = loginUser.getToken();
                    manager.setToken(mToken);
                    UserInfo userInfo = loginUser.getUserInfo();
                    Gson gson = new Gson();
                    String json = gson.toJson(userInfo);
                    manager.setUserInfo(json);
                    String profileName = userInfo.getFirstName() + " " + userInfo.getLastName();
                    String userName = userInfo.getUserName();
                    String photo = userInfo.getPhoto();
                    App.setProfilePhoto(photo);
                    String profileId = userInfo.getUserId();
                    manager.setProfileName(profileName);
                    manager.setProfileImage(POST_IMAGES + photo);
                    manager.setProfileId(profileId);
                    manager.setUserName(userName);
                    Intent intent = new Intent(FBLogin.this, Home.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                } else {
                    App.setIsFBLogin(status);
                    App.setIsTwitterLogin(true);
                    startActivity(new Intent(FBLogin.this, Signup.class));
                    finish();

                }
            }

            @Override
            public void onFailure(Call<LoginUser> call, Throwable t) {

            }
        });
    }

    private void checkLoginStatus() {
        if (AccessToken.getCurrentAccessToken() != null) {
            loadUserProfile(AccessToken.getCurrentAccessToken());
        }
    }
}