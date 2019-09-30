package com.doodle.Authentication.view.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.doodle.App;
import com.doodle.Authentication.model.LoginUser;
import com.doodle.Authentication.model.UserInfo;
import com.doodle.Authentication.service.AuthService;
import com.doodle.Home.view.activity.Home;
import com.doodle.R;
import com.doodle.Tool.AppConstants;
import com.doodle.Tool.NetworkHelper;
import com.doodle.Tool.PrefManager;
import com.doodle.Tool.Tools;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

import static com.doodle.Tool.AppConstants.POST_IMAGES;

public class MyTwitter extends AppCompatActivity implements View.OnClickListener {

    public static final String PREF_NAME = "sample_twitter_pref";
    public static final String PREF_USER_NAME = "twitter_user_name";
    public static final int WEBVIEW_REQUEST_CODE = 100;
    private static final String PREF_KEY_OAUTH_TOKEN = "oauth_token";
    private static final String PREF_KEY_OAUTH_SECRET = "oauth_token_secret";
    private static final String PREF_KEY_TWITTER_LOGIN = "is_twitter_loggedin";

    EditText mEditText = null;
    Button tweet_btn = null, twitter_login_btn = null, twitter_logout_btn = null;
    RelativeLayout mtweet_layout = null, mtwitter_login_layout = null;
    TextView username_tv = null;

    private ProgressDialog mPostProgress = null;

    private String mConsumerKey = null;
    private String mConsumerSecret = null;
    private String mCallbackUrl = null;
    private String mAuthVerifier = null;
    private String mTwitterVerifier = null;
    private Twitter mTwitter = null;
    private RequestToken mRequestToken = null;
    private SharedPreferences mSharedPreferences = null;
    private PrefManager manager;
    private boolean networkOk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        setContentView(R.layout.activity_my_twitter);
        manager = new PrefManager(this);
        networkOk = NetworkHelper.hasNetworkAccess(this);
        initViews();
        initSDK();
        tweet_btn.setOnClickListener(this);
        twitter_login_btn.setOnClickListener(this);
        twitter_logout_btn.setOnClickListener(this);
    }

    private void initViews() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //   toolbar = (Toolbar) findViewById(R.id.main_activiy_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(R.string.app_title);
//        setSupportActionBar(toolbar);
        //  getSupportActionBar().show();
        mEditText = (EditText) findViewById(R.id.et);
        tweet_btn = (Button) findViewById(R.id.tweet_btn);
        twitter_login_btn = (Button) findViewById(R.id.twitter_login_btn);
        twitter_logout_btn = (Button) findViewById(R.id.twitter_logout_btn);
        username_tv = (TextView) findViewById(R.id.username_tv);
        mtweet_layout = (RelativeLayout) findViewById(R.id.tweet_layout);
        mtwitter_login_layout = (RelativeLayout) findViewById(R.id.twitter_login_layout);
    }

    @Override
    public void onClick(View v) {
        Tools tools = new Tools();
        switch (v.getId()) {
            case R.id.tweet_btn:

                if (tools.isNetworkConnected(MyTwitter.this) == false) {
                    showAlertBox();
                } else {
                    String tweetText = mEditText.getText().toString();
                    new PostTweet().execute(tweetText);
                }
                break;
            case R.id.twitter_login_btn:
                if (tools.isNetworkConnected(MyTwitter.this) == false) {
                    showAlertBox();
                } else {
                    mSharedPreferences = getSharedPreferences(PREF_NAME, 0);
                    if (isAuthenticated()) {
                        Toast.makeText(getApplicationContext(), getString(R.string.success), Toast.LENGTH_SHORT).show();
                        mtweet_layout.setVisibility(View.GONE);
                        mtwitter_login_layout.setVisibility(View.VISIBLE);


                    } else {
                        loginToTwitter();
                    }
                }
                break;
            case R.id.twitter_logout_btn:
                logoutFromTwitter();
                break;
        }
    }

    private void logoutFromTwitter() {
        SharedPreferences.Editor e = mSharedPreferences.edit();
        e.remove(PREF_KEY_OAUTH_TOKEN);
        e.remove(PREF_KEY_OAUTH_SECRET);
        e.remove(PREF_KEY_TWITTER_LOGIN);
        e.remove(PREF_USER_NAME);
        e.commit();

        CookieSyncManager cookieSyncMngr = CookieSyncManager.createInstance(this);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookie();

        mtweet_layout.setVisibility(View.GONE);
        mtwitter_login_layout.setVisibility(View.VISIBLE);
        twitter_logout_btn.setVisibility(View.GONE);
    }


    private void closeProgress() {
        if (mPostProgress != null && mPostProgress.isShowing()) {
            mPostProgress.dismiss();
            mPostProgress = null;
        }
    }

    ///TwitterAuthentication
    public void initSDK() {
        // this.mTwitterAuth = auth;
        mConsumerKey = getResources().getString(R.string.com_twitter_sdk_android_CONSUMER_KEY);
        mConsumerSecret = getResources().getString(R.string.com_twitter_sdk_android_CONSUMER_SECRET);
        mAuthVerifier = getString(R.string.oauth_verifier);

        if (TextUtils.isEmpty(mConsumerKey)
                || TextUtils.isEmpty(mConsumerSecret)) {
            return;
        }

        mSharedPreferences = getSharedPreferences(PREF_NAME, 0);
        if (isAuthenticated()) {
            Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
            //hide login button here and show tweet
            mtweet_layout.setVisibility(View.VISIBLE);
            mtwitter_login_layout.setVisibility(View.GONE);
            mSharedPreferences.getString(PREF_USER_NAME, "");
            username_tv.setText("ForgotPasswords \n" + mSharedPreferences.getString(PREF_USER_NAME, ""));

        } else {
            mtweet_layout.setVisibility(View.GONE);
            mtwitter_login_layout.setVisibility(View.VISIBLE);
            Uri uri = getIntent().getData();

            if (uri != null && uri.toString().startsWith(mCallbackUrl)) {
                String verifier = uri.getQueryParameter(mAuthVerifier);
                try {
                    AccessToken accessToken = mTwitter.getOAuthAccessToken(
                            mRequestToken, verifier);
                    saveTwitterInformation(accessToken);
                    Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
                    Log.d("Failed to login ",
                            e.getMessage());
                }
            }
        }
    }

    protected boolean isAuthenticated() {
        return mSharedPreferences.getBoolean(PREF_KEY_TWITTER_LOGIN, false);
    }

    private void saveTwitterInformation(AccessToken accessToken) {
        long userID = accessToken.getUserId();
        User user;
        try {
            user = mTwitter.showUser(userID);
            String username = user.getName();
            SharedPreferences.Editor e = mSharedPreferences.edit();
            e.putString(PREF_KEY_OAUTH_TOKEN, accessToken.getToken());
            e.putString(PREF_KEY_OAUTH_SECRET, accessToken.getTokenSecret());
            e.putBoolean(PREF_KEY_TWITTER_LOGIN, true);
            e.putString(PREF_USER_NAME, username);
            e.commit();

        } catch (TwitterException e1) {
            Log.d("Failed to Save", e1.getMessage());
        }
    }

    private void loginToTwitter() {
        boolean isLoggedIn = mSharedPreferences.getBoolean(
                PREF_KEY_TWITTER_LOGIN, false);

        if (!isLoggedIn) {
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
    }

    protected void startWebAuthentication() {
        final Intent intent = new Intent(MyTwitter.this,
                TwitterAuthenticationActivity.class);
        intent.putExtra(TwitterAuthenticationActivity.EXTRA_URL,
                mRequestToken.getAuthenticationURL());
        startActivityForResult(intent, WEBVIEW_REQUEST_CODE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null)
            mTwitterVerifier = data.getExtras().getString(mAuthVerifier);

        AccessToken accessToken;
        try {
            accessToken = mTwitter.getOAuthAccessToken(mRequestToken,
                    mTwitterVerifier);

            long userID = accessToken.getUserId();
            manager.setTwitterOauthId(String.valueOf(userID));
            manager.setProfileId(String.valueOf(userID));
            final User user = mTwitter.showUser(userID);

            long ids = user.getId();
            Log.d("IDS", ids + "");
            String imageUrl = user.getProfileImageURL();
            manager.setTwitter_imageUrl(imageUrl);
            manager.setProfileImage(imageUrl);
            String username = user.getName();
            manager.setTwitterName(username);
            manager.setProfileName(username);
            String jj = user.getDescription();
            Log.d("Description", jj);
            username_tv.setText("ForgotPasswords\n " + username);

            mtweet_layout.setVisibility(View.VISIBLE);
            mtwitter_login_layout.setVisibility(View.GONE);
            twitter_logout_btn.setVisibility(View.VISIBLE);
            saveTwitterInformation(accessToken);
            if (App.isIsTwitterSignup()) {
                startActivity(new Intent(MyTwitter.this, Signup.class));
                finish();

            } else {

                if (NetworkHelper.hasNetworkAccess(getApplicationContext())) {

                    String appSocialAccessCode = AppConstants.APP_SOCIAL_ACCESS_CODE;
                    String oauthProvider = AppConstants.OAUTH_PROVIDER_TWITTER;
                    String deviceId = manager.getDeviceId();
                    //String oauthId = manager.getTwitterOauthId();
                    String oauthId = manager.getProfileId();
                    socialLogin(appSocialAccessCode, oauthProvider, oauthId, deviceId);

                } else {
                    Toast.makeText(MyTwitter.this, "no internet!", Toast.LENGTH_SHORT).show();

                }

            }

        } catch (Exception e) {
        }
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
                    Intent intent = new Intent(MyTwitter.this, Home.class);
                    //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                } else {
                    App.setIsTwitterLogin(status);
                    App.setIsFBLogin(true);
                    startActivity(new Intent(MyTwitter.this, Signup.class));
                    finish();

                }

            }

            @Override
            public void onFailure(Call<LoginUser> call, Throwable t) {

            }
        });
    }

    private void showAlertBox() {

        AlertDialog malertDialog = null;
        AlertDialog.Builder mdialogBuilder = null;
        if (mdialogBuilder == null) {
            mdialogBuilder = new AlertDialog.Builder(MyTwitter.this);

            mdialogBuilder.setTitle(R.string.alert);
            mdialogBuilder.setMessage(R.string.no_network);

            mdialogBuilder.setPositiveButton(R.string.enable,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // launch setting Activity
                            startActivityForResult(new Intent(
                                            android.provider.Settings.ACTION_SETTINGS),
                                    0);
                        }
                    });

            mdialogBuilder.setNegativeButton(android.R.string.no,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).setIcon(android.R.drawable.ic_dialog_alert);

            if (malertDialog == null) {
                malertDialog = mdialogBuilder.create();
                malertDialog.show();
            }

        }

    }

    class PostTweet extends AsyncTask<String, Integer, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mPostProgress = new ProgressDialog(MyTwitter.this);
            mPostProgress.setMessage(getString(R.string.loading));
            mPostProgress.setCancelable(false);
            mPostProgress.show();
        }

        @Override
        protected Void doInBackground(String... params) {

            String status = params[0];
            ConfigurationBuilder builder = new ConfigurationBuilder();
            builder.setOAuthConsumerKey(mConsumerKey);
            builder.setOAuthConsumerSecret(mConsumerSecret);

            SharedPreferences mSharedPreferences = null;
            mSharedPreferences =
                    getSharedPreferences(PREF_NAME, 0);
            String access_token = mSharedPreferences.getString(
                    PREF_KEY_OAUTH_TOKEN, "");
            String access_token_secret = mSharedPreferences.getString(
                    PREF_KEY_OAUTH_SECRET, "");
            Log.d("Async", "Consumer Key in Post Process : "
                    + access_token);
            Log.d("Async", "Consumer Secreat Key in post Process : "
                    + access_token_secret);

            AccessToken accessToken = new AccessToken(access_token,
                    access_token_secret);
            Twitter twitter = new TwitterFactory(builder.build())
                    .getInstance(accessToken);
            try {
                if (status.length() < 139) {
                    StatusUpdate statusUpdate = new StatusUpdate(status);
                    twitter4j.Status response = twitter.updateStatus(statusUpdate);
                    Log.d("Status", response.getText());
                }
            } catch (TwitterException e) {
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void Result) {
            mEditText.setText("");
            closeProgress();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // todo: goto back activity from here
                SharedPreferences.Editor e = mSharedPreferences.edit();
                e.remove(PREF_KEY_OAUTH_TOKEN);
                e.remove(PREF_KEY_OAUTH_SECRET);
                e.remove(PREF_KEY_TWITTER_LOGIN);
                e.remove(PREF_USER_NAME);
                e.commit();

                CookieSyncManager cookieSyncMngr = CookieSyncManager.createInstance(this);
                CookieManager cookieManager = CookieManager.getInstance();
                cookieManager.removeAllCookie();

                mtweet_layout.setVisibility(View.GONE);
                mtwitter_login_layout.setVisibility(View.VISIBLE);
                twitter_logout_btn.setVisibility(View.GONE);
                Intent intent = new Intent(MyTwitter.this, ForgotPasswords.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}