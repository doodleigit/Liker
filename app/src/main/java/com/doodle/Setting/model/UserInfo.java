
package com.doodle.Setting.model;

import java.io.Serializable;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserInfo implements Serializable, Parcelable
{

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("user_name")
    @Expose
    private String userName;
    @SerializedName("user_first_name")
    @Expose
    private String userFirstName;
    @SerializedName("user_last_name")
    @Expose
    private String userLastName;
    @SerializedName("user_photo")
    @Expose
    private String userPhoto;
    @SerializedName("user_profile_likes")
    @Expose
    private String userProfileLikes;
    @SerializedName("user_gold_stars")
    @Expose
    private String userGoldStars;
    @SerializedName("user_silver_stars")
    @Expose
    private String userSilverStars;
    @SerializedName("user_email")
    @Expose
    private String userEmail;
    @SerializedName("deactivated")
    @Expose
    private String deactivated;
    @SerializedName("user_founding_member")
    @Expose
    private String userFoundingMember;
    @SerializedName("learn_about_site")
    @Expose
    private String learnAboutSite;
    @SerializedName("user_top_commenter")
    @Expose
    private String userTopCommenter;
    @SerializedName("is_master")
    @Expose
    private String isMaster;
    @SerializedName("email_notification")
    @Expose
    private String emailNotification;
    @SerializedName("push_notification")
    @Expose
    private String pushNotification;
    public final static Creator<UserInfo> CREATOR = new Creator<UserInfo>() {


        @SuppressWarnings({
            "unchecked"
        })
        public UserInfo createFromParcel(Parcel in) {
            return new UserInfo(in);
        }

        public UserInfo[] newArray(int size) {
            return (new UserInfo[size]);
        }

    }
    ;
    private final static long serialVersionUID = 1312140227813968926L;

    protected UserInfo(Parcel in) {
        this.id = ((String) in.readValue((String.class.getClassLoader())));
        this.userId = ((String) in.readValue((String.class.getClassLoader())));
        this.userName = ((String) in.readValue((String.class.getClassLoader())));
        this.userFirstName = ((String) in.readValue((String.class.getClassLoader())));
        this.userLastName = ((String) in.readValue((String.class.getClassLoader())));
        this.userPhoto = ((String) in.readValue((String.class.getClassLoader())));
        this.userProfileLikes = ((String) in.readValue((String.class.getClassLoader())));
        this.userGoldStars = ((String) in.readValue((String.class.getClassLoader())));
        this.userSilverStars = ((String) in.readValue((String.class.getClassLoader())));
        this.userEmail = ((String) in.readValue((String.class.getClassLoader())));
        this.deactivated = ((String) in.readValue((String.class.getClassLoader())));
        this.userFoundingMember = ((String) in.readValue((String.class.getClassLoader())));
        this.learnAboutSite = ((String) in.readValue((String.class.getClassLoader())));
        this.userTopCommenter = ((String) in.readValue((String.class.getClassLoader())));
        this.isMaster = ((String) in.readValue((String.class.getClassLoader())));
        this.emailNotification = ((String) in.readValue((String.class.getClassLoader())));
        this.pushNotification = ((String) in.readValue((String.class.getClassLoader())));
    }

    public UserInfo() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserFirstName() {
        return userFirstName;
    }

    public void setUserFirstName(String userFirstName) {
        this.userFirstName = userFirstName;
    }

    public String getUserLastName() {
        return userLastName;
    }

    public void setUserLastName(String userLastName) {
        this.userLastName = userLastName;
    }

    public String getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }

    public String getUserProfileLikes() {
        return userProfileLikes;
    }

    public void setUserProfileLikes(String userProfileLikes) {
        this.userProfileLikes = userProfileLikes;
    }

    public String getUserGoldStars() {
        return userGoldStars;
    }

    public void setUserGoldStars(String userGoldStars) {
        this.userGoldStars = userGoldStars;
    }

    public String getUserSilverStars() {
        return userSilverStars;
    }

    public void setUserSilverStars(String userSilverStars) {
        this.userSilverStars = userSilverStars;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getDeactivated() {
        return deactivated;
    }

    public void setDeactivated(String deactivated) {
        this.deactivated = deactivated;
    }

    public String getUserFoundingMember() {
        return userFoundingMember;
    }

    public void setUserFoundingMember(String userFoundingMember) {
        this.userFoundingMember = userFoundingMember;
    }

    public String getLearnAboutSite() {
        return learnAboutSite;
    }

    public void setLearnAboutSite(String learnAboutSite) {
        this.learnAboutSite = learnAboutSite;
    }

    public String getUserTopCommenter() {
        return userTopCommenter;
    }

    public void setUserTopCommenter(String userTopCommenter) {
        this.userTopCommenter = userTopCommenter;
    }

    public String getIsMaster() {
        return isMaster;
    }

    public void setIsMaster(String isMaster) {
        this.isMaster = isMaster;
    }

    public String getEmailNotification() {
        return emailNotification;
    }

    public void setEmailNotification(String emailNotification) {
        this.emailNotification = emailNotification;
    }

    public String getPushNotification() {
        return pushNotification;
    }

    public void setPushNotification(String pushNotification) {
        this.pushNotification = pushNotification;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(userId);
        dest.writeValue(userName);
        dest.writeValue(userFirstName);
        dest.writeValue(userLastName);
        dest.writeValue(userPhoto);
        dest.writeValue(userProfileLikes);
        dest.writeValue(userGoldStars);
        dest.writeValue(userSilverStars);
        dest.writeValue(userEmail);
        dest.writeValue(deactivated);
        dest.writeValue(userFoundingMember);
        dest.writeValue(learnAboutSite);
        dest.writeValue(userTopCommenter);
        dest.writeValue(isMaster);
        dest.writeValue(emailNotification);
        dest.writeValue(pushNotification);
    }

    public int describeContents() {
        return  0;
    }

}
