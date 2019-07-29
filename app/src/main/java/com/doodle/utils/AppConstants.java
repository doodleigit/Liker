package com.doodle.utils;

import android.widget.Button;

import java.util.ArrayList;
import java.util.HashMap;

public class AppConstants {

    //===============LOCAL SERVER==================
    public static final String BASE_URL = "http://192.168.0.77:8040/sites/likerapp/";
    public static final String BASE_URL_MEDIA = "http://192.168.0.77:8040/sites/likeropt/";
    public static final String SOCKET_WEB = "http://192.168.0.77:4001";
    public static final String SOCKET_MESSAGE = "http://192.168.0.77:4002";
    public static final String SOCKET_VIDEO = "http://192.168.0.77:4004";
    public static final String FACEBOOK_SHARE = "http://192.168.0.77:8040/sites/likerapp/public/posts/";


    //http://192.168.0.77:8040/sites/likeropt/uploads/thumb/b3dae106729bc78378bd9566fe533a6f.jpg

    //==========================STG SERVER=====================
//    public static final String BASE_URL = "https://www.stg.liker.com/";
//    public static final String BASE_URL_MEDIA = "https://www.cdn-liker.com/stg-static/";
//    public static final String SOCKET_WEB = "https://node.liker.com:7803";
//    public static final String SOCKET_MESSAGE = "https://node.liker.com:7804";
//    public static final String SOCKET_VIDEO = "https://node.liker.com:7807";
//    public static final String FACEBOOK_SHARE = "https://www.stg.liker.com/public/posts/";


    //=======================LIVE SERVER=============================
    //    public static final String BASE_URL = "https://www.liker.com/";
    //    public static final String BASE_URL_MEDIA = "https://www.cdn-liker.com/";
//    public static final String SOCKET_WEB = "https://node.liker.com:7800";
//    public static final String SOCKET_MESSAGE = "https://node.liker.com:7801";
//    public static final String SOCKET_VIDEO = "https://node.liker.com:7806";


    public static final String BASE_URL_LOCATION = "https://www.liker.com/";


    public static final String API_KEY = "cd662c2e9b2e49fc9d4d763089597ea8";
    public static final String NEWS_FEED = "top-headlines?country=us&apiKey=" + API_KEY;
    public static final String SIGN_UP = "complete_signup";
    public static final String LOGIN_NEW = "login_new";
    public static final String FORGOT_PASSWORD_NEW = "forgot_password_new";
    public static final String CITY_LIST = "get_city_list";
    public static final String COUNTRY_LIST = "countrylists";
    public static final String RESEND = "main/resend";
    public static final String OTP = "reset_password_by_code";
    public static final String RESET_PASSWORD = "reset_password";
    public static final String LOGIN_WITH_OTP_APPS = "login_with_otp_apps";
    public static final String SOCIAL_LOGIN_APPS = "social_login_apps";
    public static final String APP_SOCIAL_ACCESS_CODE = "ABCDabcd1234";
    public static final String OAUTH_PROVIDER_FB = "facebook";
    public static final String OAUTH_PROVIDER_TWITTER = "twitter";
    public static final String RESEND_SIGNUP_OTP = "resend_signup_otp";

    //MEDIA
    public static final String POST_VIDEOS = BASE_URL_MEDIA + "uploads/post_videos/";
    public static final String PROFILE_IMAGE = BASE_URL_MEDIA + "uploads/thumb/";
    public static final String POST_IMAGES = BASE_URL_MEDIA + "uploads/post_images/";
    public static final String MIM_IMAGE = BASE_URL_MEDIA + "assets/nimg/";
    public static final String Link_IMAGE_PATH = BASE_URL_MEDIA + "uploads/link_images/";
    public static final String YOUTUBE_IMAGE_PATH = BASE_URL_MEDIA + "uploads/youtube/main_img/";
   //https://www.cdn-liker.com/stg-static/uploads/post_images/5d2dafdfe9487.jpg
   //https://www.cdn-liker.com/stg-static/uploads/post_videos/thumb/1ovh2hg2jy5qwenx.png

    //ADVANCE SEARCH

    public static final String GET_SEARCH_HISTORY = "get_search_history";
    public static final String SEARCH_USER = "searchUser";//after 3 characters show available user list
    public static final String REMOVE_SEARCH_HISTORY = "remove_search_history";
    public static final String ADVANCE_SEARCH = "advance_search";


    //NEW POST
    public static final String GET_CATEGORIES = "get_categories";
    public static final String POST_ADDED = "postAdded";
    public static final String ADD_PHOTO = "addPhoto";
    public static final String UPLOAD_VIDEO = "/upload";
    public static final String SEARCH_MENTION_USER = "searchMentionUser";
    public static final String ADDED_POST_CONTRIBUTOR = "addedPostContributor";
    public static final String IS_DUPLICATE_FILE = "isDuplicateFile";


   // http://192.168.0.77:8040/sites/likeropt/isDuplicateFile

    //FEED

    public static final String FEED = "feed";
    public static final String GET_POST_COMMENTS = "get_postscomments";

    //Post share
    public static final String GET_POST_DETAILS = "get_postdetails";
    public static final String ADD_SHARED_POST = "addSharedpost";
    public static final String SEND_BROWSER_NOTIFICATION = "send_browser_notification";


    public static final String NEW_MESSAGE_BROADCAST = "new_message_broadcast";
    public static final String LIST_MESSAGE_BROADCAST = "list_message_broadcast";
    public static final String NEW_NOTIFICATION_BROADCAST = "new_notification_broadcast";

    public static final String NOTIFICATION = "newNotification";
    public static final String CHAT_USERS = "get_chat_users";
    public static final String CHAT_MESSAGES = "get_messages";
    public static final String FRIEND_LIST = "friendlist";

}
