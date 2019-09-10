package com.doodle.Authentication.view.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.request.RequestOptions;
import com.doodle.App;
import com.doodle.Authentication.model.LoginUser;
import com.doodle.Authentication.model.SocialInfo;
import com.doodle.Authentication.model.UserInfo;
import com.doodle.Authentication.service.AuthService;
import com.doodle.Authentication.view.fragment.ResendEmail;
import com.doodle.Home.view.activity.Home;
import com.doodle.R;
import com.doodle.Tool.AppConstants;
import com.doodle.Tool.ClearableEditText;
import com.doodle.Tool.NetworkHelper;
import com.doodle.Tool.PrefManager;
import com.doodle.Tool.Tools;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.google.gson.Gson;
import com.onesignal.OneSignal;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

import static com.doodle.Tool.AppConstants.POST_IMAGES;

public class Login extends AppCompatActivity implements View.OnClickListener {

    public static final String SOCIAL_ITEM = "social_item";
    private ClearableEditText etEmail;
    private EditText etPassword;
    private String email, password;
    private TextView tvForgot;
    private TextView tvSignIn;
    private PrefManager manager;
    private String mDeviceId;
    private boolean networkOk;
    private ProgressBar progressBar;

    private CallbackManager callbackManager;
    private AccessTokenTracker tokenTracker;
    private Twitter mTwitter = null;
    private RequestToken mRequestToken = null;

    private String mConsumerKey = null, mConsumerSecret = null, mCallbackUrl = null, mTwitterVerifier = null, mAuthVerifier = null;
    public static final int WEBVIEW_REQUEST_CODE = 100;

   SocialInfo socialInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_login);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        socialInfo=new SocialInfo();
        manager = new PrefManager(this);
        networkOk = NetworkHelper.hasNetworkAccess(this);
        findViewById(R.id.fbLogin).setOnClickListener(this);
        findViewById(R.id.twitterLogin).setOnClickListener(this);
        etEmail = (ClearableEditText) findViewById(R.id.etEmail);
        findViewById(R.id.etEmail).setOnClickListener(this);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etEmail.setOnEditorActionListener(editorListener);
        etPassword.setOnEditorActionListener(editorListener);
        tvForgot = (TextView) findViewById(R.id.tvForgot);
        tvSignIn = (TextView) findViewById(R.id.tvSignIn);
        tvSignIn.setOnClickListener(this);
        progressBar = findViewById(R.id.progress_bar);
        findViewById(R.id.tvForgot).setOnClickListener(this);
        findViewById(R.id.imgAbout).setOnClickListener(this);
        findViewById(R.id.ivCancel).setOnClickListener(this);

        mConsumerKey = getString(R.string.com_twitter_sdk_android_CONSUMER_KEY);
        mConsumerSecret = getString(R.string.com_twitter_sdk_android_CONSUMER_SECRET);
        mAuthVerifier = "oauth_verifier";

        callbackManager = CallbackManager.Factory.create();

        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();

        OneSignal.idsAvailable(new OneSignal.IdsAvailableHandler() {
            @Override
            public void idsAvailable(String userId, String registrationId) {
                Log.d("debug", "User:" + userId);
                manager.setDeviceId(userId);
                if (registrationId != null)
                    Log.d("debug", "registrationId:" + registrationId);

            }
        });


        etEmail.setDrawableClickListener(new ClearableEditText.DrawableClickListener() {
            @Override
            public void onClick() {
                etEmail.setText(null);
            }
        });

     /*   tokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                if (currentAccessToken == null) {
                    Tools.toast(getApplicationContext(), getString(R.string.something_went_wrong), R.drawable.ic_info_outline_black_24dp);
                } else {
                    loadUserProfile(currentAccessToken);
                }
            }
        };*/

        etEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                email = etEmail.getText().toString();


                if (!TextUtils.isEmpty(password) & !TextUtils.isEmpty(email)) {
                    tvForgot.setTextColor(Color.parseColor("#1485CC"));
                    tvSignIn.setBackgroundResource(R.drawable.btn_round_outline);
                    tvSignIn.setEnabled(true);

                } else {
                    tvSignIn.setBackgroundResource(R.drawable.btn_round_outline_disable);
                    tvForgot.setTextColor(Color.parseColor("#89C3E7"));
                    tvSignIn.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                //  viewModel.validateEmailField(etEmail);
            }
        });


        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                password = etPassword.getText().toString();
                if (!TextUtils.isEmpty(password) & !TextUtils.isEmpty(email)) {
                    tvForgot.setTextColor(Color.parseColor("#1485CC"));
                    tvSignIn.setBackgroundResource(R.drawable.btn_round_outline);
                    tvSignIn.setEnabled(true);

                } else {
                    tvSignIn.setBackgroundResource(R.drawable.btn_round_outline_disable);
                    tvForgot.setTextColor(Color.parseColor("#89C3E7"));
                    tvSignIn.setEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                /// bitForMessageSave = 1;

                //    viewModel.validatePasswordField(etPassword);

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
//        tokenTracker.startTracking();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        tokenTracker.stopTracking();
    }

    private void loginDisable(boolean disable) {
        if (disable) {
            tvSignIn.setBackgroundResource(R.drawable.btn_round_outline_disable);
            tvSignIn.setEnabled(false);
        } else {
            tvSignIn.setBackgroundResource(R.drawable.btn_round_outline);
            tvSignIn.setEnabled(true);
        }
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();
        switch (id) {
            case R.id.fbLogin:
                facebookLogin();
                break;
            case R.id.twitterLogin:
                loginToTwitter();
                break;
            case R.id.etEmail:
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                break;
            case R.id.etPassword:
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                break;
            case R.id.tvSignIn:
                if (networkOk) {
                    progressBar.setVisibility(View.VISIBLE);
                    mDeviceId = manager.getDeviceId();
                    loginDisable(true);
                    requestData(email, password, mDeviceId);

                } else {
                    Tools.showNetworkDialog(getSupportFragmentManager());
                    progressBar.setVisibility(View.GONE);

                }
                break;

            case R.id.tvForgot:
            /*    if (flipperId == 0) {
                    flipperId++;
                    mViewFlipper.setInAnimation(slideLeftIn);
                    mViewFlipper.setOutAnimation(slideLeftOut);
                    mViewFlipper.showNext();

                }*/
                startActivity(new Intent(this, ForgotPassword.class));
                finish();
                break;

            case R.id.imgAbout:
                startActivity(new Intent(this, About.class));
                break;
            case R.id.ivCancel:
                finish();
                break;


        }

    }

    private void facebookLogin() {
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email", "public_profile"));
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                    if (networkOk) {
                        loadUserProfile(loginResult.getAccessToken());
                    } else {
                        Toast.makeText(getApplicationContext(), "no internet!", Toast.LENGTH_SHORT).show();
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

    private void loginToTwitter() {
        final ConfigurationBuilder builder = new ConfigurationBuilder();
        builder.setOAuthConsumerKey(mConsumerKey);
        builder.setOAuthConsumerSecret(mConsumerSecret);

        final Configuration configuration = builder.build();
        final TwitterFactory factory = new TwitterFactory(configuration);
        mTwitter = factory.getInstance();
        try {
            mRequestToken = mTwitter.getOAuthRequestToken(mCallbackUrl);
            startWebAuthentication();
        } catch (TwitterException e) {
            e.printStackTrace();
            Log.d("FA", "FA");
        }
    }

    protected void startWebAuthentication() {
        final Intent intent = new Intent(Login.this,
                TwitterAuthenticationActivity.class);
        intent.putExtra(TwitterAuthenticationActivity.EXTRA_URL,
                mRequestToken.getAuthenticationURL());
        startActivityForResult(intent, WEBVIEW_REQUEST_CODE);
    }

    private void socialLoginFacebook(String appSocialAccessCode, String oauthProvider, String oauthId, String deviceId) {
        AuthService webService =
                AuthService.retrofitBase.create(AuthService.class);

        Call<LoginUser> call = webService.socialLogin(appSocialAccessCode, oauthProvider, oauthId, deviceId);
        sendFacebookRequest(call);
    }

    private void socialLoginTwitter(String appSocialAccessCode, String oauthProvider, String oauthId, String deviceId) {
        AuthService webService =
                AuthService.retrofitBase.create(AuthService.class);

        Call<LoginUser> call = webService.socialLogin(appSocialAccessCode, oauthProvider, oauthId, deviceId);
        sendTwitterRequest(call);
    }

    private void loadUserProfile(AccessToken newAccessToken) {

        GraphRequest request = GraphRequest.newMeRequest(newAccessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                try {
                    Log.d("DATA", object.toString());

                    String first_name = object.getString("first_name");
                    socialInfo.setFirstName(first_name);
                    manager.setFbFirstName(first_name);
                    String last_name = object.getString("last_name");
                    socialInfo.setLastName(last_name);
                    manager.setFbLastName(last_name);
                    String email = object.getString("email");
                    socialInfo.setEmail(email);
                    manager.setFbEmail(email);
                    String oauthId = object.getString("id");
                    socialInfo.setAuthId(oauthId);
                    manager.setOauthId(oauthId);
                    manager.setProfileId(oauthId);
                    String image_url = "https://graph.facebook.com/" + oauthId + "/picture?type=normal";
                    socialInfo.setImage(image_url);
                    manager.setFbImageUrl(image_url);
                    manager.setProfileImage(image_url);
                    String name = object.getString("name");
                    manager.setFbName(name);
                    socialInfo.setSocialName(name);
                    String birthDay = object.getString("birthday");
                    manager.setFbBirthDay(birthDay);
                    String mProfileName = first_name + " " + last_name;
                    manager.setProfileName(mProfileName);
                    socialInfo.setProvider("facebook");

                    String appSocialAccessCode = AppConstants.APP_SOCIAL_ACCESS_CODE;
                    String oauthProvider = AppConstants.OAUTH_PROVIDER_FB;
                    String deviceId = manager.getDeviceId();
                    socialLoginFacebook(appSocialAccessCode, oauthProvider, oauthId, deviceId);


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

    private TextView.OnEditorActionListener editorListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            switch (actionId) {
                case EditorInfo.IME_ACTION_NEXT:
                    //  Toast.makeText(Login.this, "Next", Toast.LENGTH_SHORT).show();
                    break;
                case EditorInfo.IME_ACTION_SEND:
                    if (networkOk) {
                        progressBar.setVisibility(View.VISIBLE);
                        mDeviceId = manager.getDeviceId();
                        loginDisable(true);
                        requestData(email, password, mDeviceId);

                    } else {
                        Tools.showNetworkDialog(getSupportFragmentManager());
                        progressBar.setVisibility(View.GONE);

                    }
                    break;
            }
            return false;
        }
    };


    private void requestData(String email, String password, String mDeviceId) {
        AuthService webService =
                AuthService.retrofitBase.create(AuthService.class);

        Call<LoginUser> call = webService.loginUser(email, password, mDeviceId);
        sendRequest(call);

    }

    UserInfo userInfo;

    private void sendFacebookRequest(Call<LoginUser> call) {
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
                    Intent intent = new Intent(Login.this, Home.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(Login.this, Signup.class);
                    intent.putExtra(SOCIAL_ITEM, socialInfo);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onFailure(Call<LoginUser> call, Throwable t) {

            }
        });
    }

    private void sendTwitterRequest(Call<LoginUser> call) {
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
                    Intent intent = new Intent(Login.this, Home.class);
                    //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(Login.this, Signup.class);
                    intent.putExtra(SOCIAL_ITEM, socialInfo);
                    startActivity(intent);
                    finish();

                }

            }

            @Override
            public void onFailure(Call<LoginUser> call, Throwable t) {

            }
        });
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
                    showSnackbar("login success!");
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
                    //   startActivity(new Intent(ForgotPassword.this, Liker.class));
//                    Intent intent=new Intent(ForgotPassword.this,Home.class);
//                    intent.putExtra(USER_INFO_ITEM_KEY, (Parcelable) userInfo);
//                    startActivity(intent);

                    startActivity(new Intent(Login.this, Home.class));
                    finish();
                } else {
                    String msg = "Username and password miss match";
                    showStatus(msg);

                    if (loginUser.getIsVerified() != null && loginUser.getBounceData() != null) {
                        if (loginUser.getIsVerified().equalsIgnoreCase("0") && loginUser.getBounceData().equalsIgnoreCase("0")) {
                            userInfo = loginUser.getUserInfo();
                            if (userInfo != null) {
                                String message = "A verification email has been sent to your email address. Please confirm and complete your registration.";
                                ResendEmail resendEmail = ResendEmail.newInstance(message);
                                resendEmail.show(getSupportFragmentManager(), "ResendEmail");
                            }

                        } else if (loginUser.getBounceData().equalsIgnoreCase(String.valueOf(1)) || loginUser.getBounceData().equalsIgnoreCase(String.valueOf(2))) {
                            Toast.makeText(Login.this, "Email is invalid", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                progressBar.setVisibility(View.GONE);
                loginDisable(false);
            }

            @Override
            public void onFailure(Call<LoginUser> call, Throwable t) {
                Log.d("message", t.getMessage());
                progressBar.setVisibility(View.GONE);
                loginDisable(false);

            }
        });
    }


    public void showSnackbar(String message) {
        Snackbar snackbar = Snackbar
                .make(findViewById(R.id.signupContainer), message, Snackbar.LENGTH_LONG)
                .setAction(R.string.retry, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                    }
                });

// Changing message text color
        snackbar.setActionTextColor(Color.RED);

// Changing action button text color
        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.YELLOW);
        snackbar.show();
    }

    private void showStatus(String message) {
        Snackbar snackbar = Snackbar
                .make(findViewById(R.id.signupContainer), message, Snackbar.LENGTH_LONG);

        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.YELLOW);
        snackbar.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        if (requestCode == WEBVIEW_REQUEST_CODE) {
            if (data != null)
                mTwitterVerifier = data.getExtras().getString(mAuthVerifier);

            twitter4j.auth.AccessToken accessToken;
            try {
                accessToken = mTwitter.getOAuthAccessToken(mRequestToken,
                        mTwitterVerifier);


                long userID = accessToken.getUserId();
                socialInfo.setAuthId(String.valueOf(userID));

                manager.setTwitterOauthId(String.valueOf(userID));
                manager.setProfileId(String.valueOf(userID));
                final User user = mTwitter.showUser(userID);

                long ids = user.getId();
                Log.d("IDS", ids + "");
                String imageUrl = user.getProfileImageURL();
                socialInfo.setImage(imageUrl);
                manager.setTwitter_imageUrl(imageUrl);
                manager.setProfileImage(imageUrl);
                String username = user.getName();
                socialInfo.setFirstName(username);
                socialInfo.setLastName(username);
                socialInfo.setSocialName(username);
                manager.setTwitterName(username);
                manager.setProfileName(username);
                String jj = user.getDescription();
                Log.d("Description", jj);
//                saveTwitterInformation(accessToken);
                if (App.isIsTwitterSignup()) {
                    startActivity(new Intent(Login.this, Signup.class));
                    finish();
                } else {
                    if (networkOk) {
                        String appSocialAccessCode = AppConstants.APP_SOCIAL_ACCESS_CODE;
                        String oauthProvider = AppConstants.OAUTH_PROVIDER_TWITTER;
                        String deviceId = manager.getDeviceId();

                        //String oauthId = manager.getTwitterOauthId();
                      //  String oauthId = manager.getProfileId();
                        String oauthId =socialInfo.getAuthId();
                        socialLoginTwitter(appSocialAccessCode, oauthProvider, oauthId, deviceId);
                    } else {
                        Toast.makeText(getApplicationContext(), "no internet!", Toast.LENGTH_SHORT).show();
                    }
                }

            } catch (Exception e) {
            }
        }

    }

}
