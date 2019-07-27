
package com.doodle.Message.model;

import java.io.Serializable;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserData implements Serializable, Parcelable
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
    @SerializedName("description")
    @Expose
    private String description;
    public final static Creator<UserData> CREATOR = new Creator<UserData>() {


        @SuppressWarnings({
            "unchecked"
        })
        public UserData createFromParcel(Parcel in) {
            return new UserData(in);
        }

        public UserData[] newArray(int size) {
            return (new UserData[size]);
        }

    }
    ;
    private final static long serialVersionUID = -642601059672515397L;

    protected UserData(Parcel in) {
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
        this.description = ((String) in.readValue((String.class.getClassLoader())));
    }

    public UserData() {
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
        dest.writeValue(description);
    }

    public int describeContents() {
        return  0;
    }

}
