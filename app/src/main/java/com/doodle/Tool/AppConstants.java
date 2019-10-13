package com.doodle.Tool;

public class AppConstants {

    //===============LOCAL SERVER==================

//    public static final String BASE_URL = "http://192.168.0.6:8040/sites/likerapp/";
//    public static final String BASE_URL_MEDIA = "http://192.168.0.6:8040/sites/likeropt/";
//    public static final String BASE_URL_IMAGES = "http://192.168.0.6:8040/sites/likeropt/";
//    public static final String LINK_IMAGES = "http://192.168.0.6:8040/sites/likeropt/uploads/link_images";
//    public static final String SOCKET_WEB = "http://192.168.0.6:4001";
//    public static final String SOCKET_MESSAGE = "http://192.168.0.6:4002";
//    public static final String SOCKET_VIDEO = "http://192.168.0.6:4004";
//    public static final String FACEBOOK_SHARE = "http://192.168.0.6:8040/sites/likerapp/public/posts/";
//    public static final String FACEBOOK_INVITATION = "http://192.168.0.6:8040/sites/likerapp/settings/fbinvite/";
//    public static final String BASE_URL_LOCATION = "http://192.168.0.6:8040/sites/likerapp/";
    //==========================STG SERVER=====================

//    public static final String BASE_URL = "https://www.stg.liker.com/";
//    public static final String BASE_URL_MEDIA = "https://www.cdn-liker.com/";
//    public static final String BASE_URL_IMAGES = "https://www.cdn-liker.com/";
//    public static final String SOCKET_WEB = "https://node.liker.com:7803";
//    public static final String SOCKET_MESSAGE = "https://node.liker.com:7804";
//    public static final String SOCKET_VIDEO = "https://node.liker.com:7807";
//    public static final String FACEBOOK_SHARE = "https://www.stg.liker.com/public/posts/";
//    public static final String LINK_IMAGES = "https://www.cdn-liker.com/uploads/link_images/";
//    public static final String FACEBOOK_INVITATION = "https://www.d.liker.com/settings/fbinvite/";
//    public static final String BASE_URL_LOCATION = "https://www.stg.liker.com/";

    //=======================LIVE SERVER=============================
    public static final String BASE_URL = "https://www.api.liker.com/";
    public static final String BASE_URL_MEDIA = "https://www.cdn-liker.com/";
    public static final String BASE_URL_IMAGES = "https://www.cdn-liker.com/";
    public static final String SOCKET_WEB = "https://node.liker.com:7800";
    public static final String SOCKET_MESSAGE = "https://node.liker.com:7801";
    public static final String SOCKET_VIDEO = "https://node.liker.com:7806";
    public static final String FACEBOOK_SHARE = "https://www.api.liker.com/public/posts/";
    public static final String LINK_IMAGES = "https://www.cdn-liker.com/uploads/link_images/";
    public static final String FACEBOOK_INVITATION = "https://www.liker.com/settings/fbinvite/";
    public static final String BASE_URL_LOCATION = "https://www.api.liker.com/";

    public static final String API_KEY = "cd662c2e9b2e49fc9d4d763089597ea8";
    public static final String NEWS_FEED = "top-headlines?country=us&apiKey=" + API_KEY;
    public static final String SIGN_UP = "complete_signup";
    public static final String LOGIN_NEW = "login_new";
    public static final String FORGOT_PASSWORD_NEW = "forgot_password_new";
    public static final String CITY_LIST = "get_city_list";
    public static final String COUNTRY_LIST = "countrylists";
    public static final String RESEND = "main/resend";
    public static final String OTP = "reset_password_by_code";
    public static final String CHECK_EMAIL_EXIST = "check_email_exists";
    public static final String RESET_PASSWORD = "reset_password";
    public static final String LOGIN_WITH_OTP_APPS = "login_with_otp_apps";
    public static final String SOCIAL_LOGIN_APPS = "social_login_apps";
    public static final String APP_SOCIAL_ACCESS_CODE = "ABCDabcd1234";
    public static final String OAUTH_PROVIDER_FB = "facebook";
    public static final String OAUTH_PROVIDER_TWITTER = "twitter";
    public static final String RESEND_SIGNUP_OTP = "resend_signup_otp";

    //MEDIA
    public static final String POST_VIDEOS = BASE_URL_MEDIA + "uploads/post_videos/";
    public static final String POST_VIDEOS_THUMBNAIL = BASE_URL_MEDIA + "uploads/post_videos/thumb/";
    public static final String PROFILE_IMAGE = BASE_URL_MEDIA + "uploads/thumb/";
    public static final String POST_IMAGES = BASE_URL_MEDIA + "uploads/post_images/";
    public static final String USER_UPLOADED_IMAGES = BASE_URL_IMAGES + "uploads/post_images/";
    public static final String MIM_IMAGE = BASE_URL_MEDIA + "assets/nimg/";
    public static final String Link_IMAGE_PATH = BASE_URL_MEDIA + "uploads/link_images/";
    public static final String YOUTUBE_IMAGE_PATH = BASE_URL_MEDIA + "uploads/youtube/main_img/";
    //https://www.cdn-liker.com/stg-static/uploads/post_images/5d2dafdfe9487.jpg
    //https://www.cdn-liker.com/stg-static/uploads/post_videos/thumb/1ovh2hg2jy5qwenx.png
//http://192.168.0.77:8040/sites/likeropt/uploads/post_images/5d6372064e3a2.jpg
    //ADVANCE SEARCH

    public static final String GET_SEARCH_HISTORY = "get_search_history";
    public static final String SEARCH_USER = "searchUser";//after 3 characters show available user list
    public static final String REMOVE_SEARCH_HISTORY = "remove_search_history";
    public static final String ADVANCE_SEARCH = "advance_search";


    //NEW POST
    public static final String GET_CATEGORIES = "get_categories";
    public static final String POST_ADDED = "postAdded";
    public static final String POST_EDITED = "postEdited";
    public static final String ADD_PHOTO = "addPhoto";
    public static final String UPLOAD_VIDEO = "/upload";
    public static final String SEARCH_MENTION_USER = "searchMentionUser";
    public static final String ADDED_POST_CONTRIBUTOR = "addedPostContributor";
    public static final String IS_DUPLICATE_FILE = "isDuplicateFile";
    public static boolean IN_CHAT_MODE = false;
    public static final int RESULT_BACK_NUMBER = 121;


    // http://192.168.0.77:8040/sites/likeropt/isDuplicateFile

    //FEED

    public static final String FEED = "feed";
    public static final String WALL_FEED = "wallfeed";
    public static final String GET_POST_COMMENTS = "get_postscomments";
    public static final String GET_POST_COMMENT_REPLY_LIST = "get_post_comment_reply_list";
    public static final String ADDED_COMMENTS = "added_comment";
    public static final String ADDED_COMMENT_REPLY = "add_comment_reply";
    public static final String EDIT_POST_COMMENT = "edit_postcomment";
    public static final String EDIT_COMMENT_REPLY = "edit_comment_reply";
    public static final String DELETE_POST_COMMENT = "delete_postcomment";
    public static final String DELETE_COMMENT_REPLY = "delete_comment_reply";
    public static final String COMMENT_LIKE = "comment_like";
    public static final String LIKE_COMMENT_REPLY = "like_comment_reply";
    public static final String COMMENT_UNLIKE = "comment_unlike";
    public static final String UNLIKE_COMMENT_REPLY = "unlike_comment_reply";
    public static final String GET_REPORT_REASON = "get_report_reason";
    public static final String REPORT_USER = "reportUser";
    public static final String UN_FOLLOW = "unfollow";

    //Post share
    public static final String GET_POST_DETAILS = "get_postdetails";
    public static final String ADD_SHARED_POST = "addSharedpost";
    public static final String SEND_BROWSER_NOTIFICATION = "send_browser_notification";
    public static final String GET_FILTER_CATEGORIES = "get_categories";
    public static final String GET_POST_FILTERS = "get_postfilters";
    public static final String GET_SINGLE_POST_FILTERS = "get_single_post_filter";
    public static final String ADDED_FILTER = "added_filter";
    public static final String GET_USER_RANKINGS = "getUserRankings";
    public static final String POST_PERMISSION = "post_permission";
    public static final String POST_NOTIFICATION_TURN_OFF = "post_notification_turn_off";
    public static final String POST_NOTIFICATION_TURN_ON = "post_notification_turn_on";
    public static final String POST_DELETE = "post_delete";
    public static final String POST_LIKE = "post_like";
    public static final String POST_UNLIKE = "post_unlike";
    public static final String POST_LIKERS = "post-likers";
    public static final String COMMENT_LIKERS = "comment-likers";
    public static final String COMMENT_REPLY_LIKERS = "comment-reply-likers";
    public static final String NOTIFICATION_SEEN = "notification_seen";


    public static final String NEW_MESSAGE_BROADCAST = "new_message_broadcast";
    public static final String NEW_MESSAGE_BROADCAST_FROM_HOME = "new_message_broadcast_from_home";
    public static final String LIST_MESSAGE_BROADCAST = "list_message_broadcast";
    public static final String NEW_NOTIFICATION_BROADCAST = "new_notification_broadcast";
    public static final String RECONNECT_SOCKET_BROADCAST = "reconnect_socket_broadcast";
    public static final String CATEGORY_CHANGE_BROADCAST = "category_change_broadcast";
    public static final String PROFILE_PAGE_PAGINATION_BROADCAST = "profile_page_pagination_broadcast";
    public static final String NEW_POST_ADD_BROADCAST = "new_post_add_broadcast";
    public static final String POST_CHANGE_BROADCAST = "post_change_broadcast";
    public static final String POST_FILTER_CAT_BROADCAST = "post_filter_cat_broadcast";
    public static final String PERMISSION_CHANGE_BROADCAST = "permission_change_broadcast";
    public static final String REPLY_CHANGE_BROADCAST = "reply_change_broadcast";

    public static final String NOTIFICATION = "newNotification";
    public static final String CHAT_USERS = "get_chat_users";
    public static final String CHAT_MESSAGES = "get_messages";
    public static final String FRIEND_LIST = "friendlist";
    public static final String VIEW_ALBUMS = "view_albums";
    public static final String GET_ALBUM_PHOTOS = "get_album_photos";
    public static final String GET_RECENT_PHOTOS = "get_recent_photos";
    public static final String GET_PROFILE_INFO = "get_profile_info_app";
    public static final String GET_EDUCATION_SUGGESTION = "get_education_suggestions";
    public static final String GET_EXPERIENCE_SUGGESTION = "get_experience_suggestions";
    public static final String GET_AWARDS_SUGGESTION = "get_award_suggestions";
    public static final String GET_CERTIFICATE_SUGGESTION = "get_certification_suggestions";
    public static final String INSERT_EDUCATION = "insert_education";
    public static final String UPDATE_EDUCATION = "update_education";
    public static final String INSERT_EXPERIENCE = "insert_experience";
    public static final String UPDATE_EXPERIENCE = "update_experience";
    public static final String INSERT_AWARDS = "insert_award";
    public static final String UPDATE_AWARDS = "update_award";
    public static final String INSERT_CERTIFICATE = "insert_certification";
    public static final String UPDATE_CERTIFICATE = "update_certification";
    public static final String INSERT_LINK = "link_add";
    public static final String UPDATE_LINK = "link_update";
    public static final String REMOVE_EDUCATION = "remove_education";
    public static final String REMOVE_EXPERIENCE = "remove_experience";
    public static final String REMOVE_CERTIFICATION = "remove_certification";
    public static final String REMOVE_AWARDS = "remove_award";
    public static final String REMOVE_LINKS = "link_remove";

    public static final String STAR_LIST = "wallbadgelist";
    public static final String GET_USER_INFO_BY_USER_NAME = "getUserinfoByUserName";
    public static final String GET_USER_INFO = "get_userinfo";
    public static final String UPLOAD_PROFILE_IMAGE = "updateProfilePicture";
    public static final String UPLOAD_COVER_IMAGE = "add_cover_photo";
    public static final String EMAIL_ADD = "email_add";
    public static final String PHONE_ADD = "phone_add";
    public static final String SET_STORY = "set_story";
    public static final String SET_LIVE_PLACE = "set_live_place";
    public static final String GET_CITY_LIST = "get_city_list";
    public static final String GET_COUNTRY_PHONE_CODES = "get_country_phone_codes";
    public static final String SET_INTRO = "set_intro";
    public static final String EMAIL_REMOVE = "email_remove";
    public static final String PHONE_REMOVE = "phone_remove";
    public static final String ITEM_KEY = "item_key";
    public static final String COMMENT_KEY = "comment_item_key";
    public static final String IS_FRIEND_STATUS = "isFriendStatus";

    public static final String PRIVACY_VIEW = "privacy_view";
    public static final String PRIVACY_UPDATE_PERMISSION = "privacy_update_permission";
    public static final String BLOCKED_USER = "blocked_user";
    public static final String UNBLOCKED_USER = "unblocked_user";
    public static final String GET_ON_OFFS_BY_USER_ID = "get_on_offs_by_user_id";
    public static final String NOTIFICATION_ON_OFF = "notifications_on_off";
    public static final String CONTRIBUTOR_VIEW = "contributor_view";
    public static final String GET_CONTRIBUTOR_CATEGORY = "get_contributor_categories";
    public static final String SET_CONTRIBUTOR_CATEGORY = "set_contributor_category";
    public static final String ACCOUNT_VIEW = "account_view";
    public static final String GET_EMAILS = "get_emails";
    public static final String UPDATE_PASSWORD = "account_update_password";
    public static final String DEACTIVATED_ACCOUNT = "deactivate_account";
    public static final String EMAIL_VERIFICATION_SEND = "email_verification_send";
    public static final String UPDATE_SECURITY_QUESTION = "account_update_security_question";
    public static final String PEOPLE_YOU_MAY_KNOW = "people_may_know";
    public static final String GET_SOCIAL_FRIENDS = "get_social_friends";
    public static final String GET_SOCIAL_LINKS = "get_social_links";
    public static final String FOLLOW = "follow";
    public static final String UNFOLLOW = "unfollow";
    public static final String GET_FOLLOWERS = "get_followers";
    public static final String GET_FOLLOWINGS = "get_followings";
    public static final String LOGOUT = "logout";

}
