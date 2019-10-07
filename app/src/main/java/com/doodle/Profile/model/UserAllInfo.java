
package com.doodle.Profile.model;

import java.io.Serializable;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserAllInfo implements Serializable, Parcelable
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
    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("last_name")
    @Expose
    private String lastName;
    @SerializedName("total_likes")
    @Expose
    private String totalLikes;
    @SerializedName("gold_stars")
    @Expose
    private String goldStars;
    @SerializedName("sliver_stars")
    @Expose
    private String sliverStars;
    @SerializedName("photo")
    @Expose
    private String photo;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("deactivated")
    @Expose
    private String deactivated;
    @SerializedName("founding_user")
    @Expose
    private String foundingUser;
    @SerializedName("learn_about_site")
    @Expose
    private String learnAboutSite;
    @SerializedName("is_top_commenter")
    @Expose
    private String isTopCommenter;
    @SerializedName("is_master")
    @Expose
    private String isMaster;
    @SerializedName("cover_image")
    @Expose
    private String coverImage;
    @SerializedName("date_of_birth")
    @Expose
    private String dateOfBirth;
    @SerializedName("privacy")
    @Expose
    private Privacy privacy;
    @SerializedName("is_blocked")
    @Expose
    private Boolean isBlocked;
    @SerializedName("total_followers")
    @Expose
    private String totalFollowers;
    @SerializedName("total_followings")
    @Expose
    private String totalFollowings;
    public final static Creator<UserAllInfo> CREATOR = new Creator<UserAllInfo>() {


        @SuppressWarnings({
            "unchecked"
        })
        public UserAllInfo createFromParcel(Parcel in) {
            return new UserAllInfo(in);
        }

        public UserAllInfo[] newArray(int size) {
            return (new UserAllInfo[size]);
        }

    }
    ;
    private final static long serialVersionUID = -5268600507778240518L;

    protected UserAllInfo(Parcel in) {
        this.id = ((String) in.readValue((String.class.getClassLoader())));
        this.userId = ((String) in.readValue((String.class.getClassLoader())));
        this.userName = ((String) in.readValue((String.class.getClassLoader())));
        this.firstName = ((String) in.readValue((String.class.getClassLoader())));
        this.lastName = ((String) in.readValue((String.class.getClassLoader())));
        this.totalLikes = ((String) in.readValue((String.class.getClassLoader())));
        this.goldStars = ((String) in.readValue((String.class.getClassLoader())));
        this.sliverStars = ((String) in.readValue((String.class.getClassLoader())));
        this.photo = ((String) in.readValue((String.class.getClassLoader())));
        this.email = ((String) in.readValue((String.class.getClassLoader())));
        this.deactivated = ((String) in.readValue((String.class.getClassLoader())));
        this.foundingUser = ((String) in.readValue((String.class.getClassLoader())));
        this.learnAboutSite = ((String) in.readValue((String.class.getClassLoader())));
        this.isTopCommenter = ((String) in.readValue((String.class.getClassLoader())));
        this.isMaster = ((String) in.readValue((String.class.getClassLoader())));
        this.coverImage = ((String) in.readValue((String.class.getClassLoader())));
        this.dateOfBirth = ((String) in.readValue((String.class.getClassLoader())));
        this.privacy = ((Privacy) in.readValue((Privacy.class.getClassLoader())));
        this.isBlocked = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
        this.totalFollowers = ((String) in.readValue((String.class.getClassLoader())));
        this.totalFollowings = ((String) in.readValue((String.class.getClassLoader())));
    }

    public UserAllInfo() {
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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getTotalLikes() {
        return totalLikes;
    }

    public void setTotalLikes(String totalLikes) {
        this.totalLikes = totalLikes;
    }

    public String getGoldStars() {
        return goldStars;
    }

    public void setGoldStars(String goldStars) {
        this.goldStars = goldStars;
    }

    public String getSliverStars() {
        return sliverStars;
    }

    public void setSliverStars(String sliverStars) {
        this.sliverStars = sliverStars;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDeactivated() {
        return deactivated;
    }

    public void setDeactivated(String deactivated) {
        this.deactivated = deactivated;
    }

    public String getFoundingUser() {
        return foundingUser;
    }

    public void setFoundingUser(String foundingUser) {
        this.foundingUser = foundingUser;
    }

    public String getLearnAboutSite() {
        return learnAboutSite;
    }

    public void setLearnAboutSite(String learnAboutSite) {
        this.learnAboutSite = learnAboutSite;
    }

    public String getIsTopCommenter() {
        return isTopCommenter;
    }

    public void setIsTopCommenter(String isTopCommenter) {
        this.isTopCommenter = isTopCommenter;
    }

    public String getIsMaster() {
        return isMaster;
    }

    public void setIsMaster(String isMaster) {
        this.isMaster = isMaster;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Privacy getPrivacy() {
        return privacy;
    }

    public void setPrivacy(Privacy privacy) {
        this.privacy = privacy;
    }

    public Boolean getIsBlocked() {
        return isBlocked;
    }

    public void setIsBlocked(Boolean isBlocked) {
        this.isBlocked = isBlocked;
    }

    public String getTotalFollowers() {
        return totalFollowers;
    }

    public void setTotalFollowers(String totalFollowers) {
        this.totalFollowers = totalFollowers;
    }

    public String getTotalFollowings() {
        return totalFollowings;
    }

    public void setTotalFollowings(String totalFollowings) {
        this.totalFollowings = totalFollowings;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(userId);
        dest.writeValue(userName);
        dest.writeValue(firstName);
        dest.writeValue(lastName);
        dest.writeValue(totalLikes);
        dest.writeValue(goldStars);
        dest.writeValue(sliverStars);
        dest.writeValue(photo);
        dest.writeValue(email);
        dest.writeValue(deactivated);
        dest.writeValue(foundingUser);
        dest.writeValue(learnAboutSite);
        dest.writeValue(isTopCommenter);
        dest.writeValue(isMaster);
        dest.writeValue(coverImage);
        dest.writeValue(dateOfBirth);
        dest.writeValue(privacy);
        dest.writeValue(isBlocked);
        dest.writeValue(totalFollowers);
        dest.writeValue(totalFollowings);
    }

    public int describeContents() {
        return  0;
    }

}
