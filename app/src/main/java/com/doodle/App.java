package com.doodle;

import android.app.Application;
import android.content.Context;
import android.os.StrictMode;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;

import com.danikula.videocache.HttpProxyCacheServer;
import com.doodle.Post.model.Category;
import com.doodle.Post.model.Subcatg;
import com.doodle.utils.PrefManager;
import com.onesignal.OneSignal;
import com.twitter.sdk.android.core.DefaultLogger;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterConfig;
import com.vanniktech.emoji.EmojiManager;
import com.vanniktech.emoji.twitter.TwitterEmojiProvider;

import static android.support.v7.app.AppCompatDelegate.MODE_NIGHT_AUTO;

public class App extends Application {


    private static boolean isValidate;
    private static String fbProvider;
    private static String queryResult;
    private static String socketId;
    private static String categoryId;
    private static boolean isFBSignup;
    private static boolean isTwitterSignup;
    private static boolean isFBLogin;
    private static boolean isTwitterLogin;
    private static final String TAG = App.class.getSimpleName();
    private static App myInstance;
    private static Context mContext;
    PrefManager manager;
    private static Category mCategory;
    private static Subcatg mSubcatg;



    private HttpProxyCacheServer proxy;

    public static HttpProxyCacheServer getProxy(Context context) {
        App app = (App) context.getApplicationContext();
        return app.proxy == null ? (app.proxy = app.newProxy()) : app.proxy;
    }

    private HttpProxyCacheServer newProxy() {
        return new HttpProxyCacheServer(this);
    }

    public static String getCategoryId() {
        return categoryId;
    }

    public static void setCategoryId(String categoryId) {
        App.categoryId = categoryId;
    }

    public static Category getmCategory() {
        return mCategory;
    }

    public static void setmCategory(Category mCategory) {
        App.mCategory = mCategory;
    }

    public static Subcatg getmSubcatg() {
        return mSubcatg;
    }

    public static void setmSubcatg(Subcatg mSubcatg) {
        App.mSubcatg = mSubcatg;
    }

    public static String getSocketId() {
        return socketId;
    }

    public static void setSocketId(String socketId) {
        App.socketId = socketId;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_AUTO);

        EmojiManager.install(new TwitterEmojiProvider());

        if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll().build());
        }

        myInstance = this;
        mContext = getApplicationContext();
        manager = new PrefManager(mContext);
        // OneSignal Initialization
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

        TwitterConfig config = new TwitterConfig.Builder(mContext)
                .logger(new DefaultLogger(Log.DEBUG))//enable logging when app is in debug mode
                .twitterAuthConfig(new TwitterAuthConfig(getResources().getString(R.string.com_twitter_sdk_android_CONSUMER_KEY), getResources().getString(R.string.com_twitter_sdk_android_CONSUMER_SECRET)))//pass the created app Consumer KEY and Secret also called API Key and Secret
                .debug(true)//enable debug mode
                .build();


        //finally initialize twitter with created configs
        Twitter.initialize(config);

    }

    public static String getQueryResult() {
        return queryResult;
    }

    public static void setQueryResult(String queryResult) {
        App.queryResult = queryResult;
    }

    public static String getFbProvider() {
        return fbProvider;
    }

    public static void setFbProvider(String fbProvider) {
        App.fbProvider = fbProvider;
    }

    public static boolean isIsFBSignup() {
        return isFBSignup;
    }

    public static void setIsFBSignup(boolean isFBSignup) {
        App.isFBSignup = isFBSignup;
    }

    public static boolean isIsTwitterSignup() {
        return isTwitterSignup;
    }

    public static void setIsTwitterSignup(boolean isTwitterSignup) {
        App.isTwitterSignup = isTwitterSignup;
    }


    public static boolean isIsTwitterLogin() {
        return isTwitterLogin;
    }

    public static void setIsTwitterLogin(boolean isTwitterLogin) {
        App.isTwitterLogin = isTwitterLogin;
    }

    public static boolean isIsFBLogin() {
        return isFBLogin;
    }

    public static void setIsFBLogin(boolean isFBLogin) {
        App.isFBLogin = isFBLogin;
    }

    public static synchronized App getInstance() {
        return myInstance;
    }

    public static Context getAppContext() {
        return App.mContext;
    }

    public static boolean isIsValidate() {
        return isValidate;
    }

    public static void setIsValidate(boolean isValidate) {
        App.isValidate = isValidate;
    }
}
