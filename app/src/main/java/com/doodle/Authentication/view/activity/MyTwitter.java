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
import com.doodle.Authentication.service.AuthService;
import com.doodle.Home.Liker;
import com.doodle.Home.view.activity.Home;
import com.doodle.R;
import com.doodle.utils.AppConstants;
import com.doodle.utils.NetworkHelper;
import com.doodle.utils.PrefManager;
import com.doodle.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

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
        getSupportActionBar().setTitle("Liker");
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
        Utils utils = new Utils();
        switch (v.getId()) {
            case R.id.tweet_btn:

                if (utils.isNetworkConnected(MyTwitter.this) == false) {
                    showAlertBox();
                } else {
                    String tweetText = mEditText.getText().toString();
                    new PostTweet().execute(tweetText);
                }
                break;
            case R.id.twitter_login_btn:
                if (utils.isNetworkConnected(MyTwitter.this) == false) {
                    showAlertBox();
                } else {
                    mSharedPreferences = getSharedPreferences(PREF_NAME, 0);
                    if (isAuthenticated()) {
                        Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
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
        mAuthVerifier = "oauth_verifier";

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
            username_tv.setText("Welcome \n" + mSharedPreferences.getString(PREF_USER_NAME, ""));

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
            username_tv.setText("Welcome\n " + username);

            mtweet_layout.setVisibility(View.VISIBLE);
            mtwitter_login_layout.setVisibility(View.GONE);
            twitter_logout_btn.setVisibility(View.VISIBLE);
            saveTwitterInformation(accessToken);
            if (App.isIsTwitterSignup()) {
                startActivity(new Intent(MyTwitter.this, Signup.class));
                finish();

            } else {

                if (networkOk) {

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

        Call<String> call = webService.socialLogin(appSocialAccessCode, oauthProvider, oauthId, deviceId);
        sendRequest(call);

    }

    private void sendRequest(Call<String> call) {
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {


                String loginUser = response.body();
                try {
                    JSONObject object = new JSONObject(loginUser);
                    boolean status = object.getBoolean("status");
                    if (status) {
                        Log.d("LoginUser:", loginUser);
                     //   startActivity(new Intent(MyTwitter.this, Liker.class));
                        startActivity(new Intent(MyTwitter.this, Home.class));
                        finish();
                    } else {
                        App.setIsTwitterLogin(status);
                        App.setIsFBLogin(true);
                        startActivity(new Intent(MyTwitter.this, Signup.class));
                        finish();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    private void showAlertBox() {

        AlertDialog malertDialog = null;
        AlertDialog.Builder mdialogBuilder = null;
        if (mdialogBuilder == null) {
            mdialogBuilder = new AlertDialog.Builder(MyTwitter.this);

            mdialogBuilder.setTitle("Alert");
            mdialogBuilder.setMessage("No Network");

            mdialogBuilder.setPositiveButton("Enable",
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
            mPostProgress.setMessage("Loading...");
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
                Intent intent = new Intent(MyTwitter.this, Welcome.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}