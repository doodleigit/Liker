
package com.doodle.Message.model;

import java.io.Serializable;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User implements Serializable, Parcelable {

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
    private Integer totalLikes;
    @SerializedName("gold_stars")
    @Expose
    private Integer goldStars;
    @SerializedName("sliver_stars")
    @Expose
    private Integer sliverStars;
    @SerializedName("photo")
    @Expose
    private String photo;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("deactivated")
    @Expose
    private Integer deactivated;
    @SerializedName("founding_user")
    @Expose
    private String foundingUser;
    @SerializedName("learn_about_site")
    @Expose
    private Integer learnAboutSite;
    @SerializedName("is_top_commenter")
    @Expose
    private Integer isTopCommenter;
    @SerializedName("is_master")
    @Expose
    private Integer isMaster;
    @SerializedName("description")
    @Expose
    private String description;
    public final static Creator<User> CREATOR = new Creator<User>() {


        @SuppressWarnings({
            "unchecked"
        })
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        public User[] newArray(int size) {
            return (new User[size]);
        }

    }
    ;
    private final static long serialVersionUID = -6382591179973633605L;

    protected User(Parcel in) {
        this.userId = ((String) in.readValue((Integer.class.getClassLoader())));
        this.userName = ((String) in.readValue((String.class.getClassLoader())));
        this.firstName = ((String) in.readValue((String.class.getClassLoader())));
        this.lastName = ((String) in.readValue((String.class.getClassLoader())));
        this.totalLikes = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.goldStars = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.sliverStars = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.photo = ((String) in.readValue((String.class.getClassLoader())));
        this.email = ((String) in.readValue((String.class.getClassLoader())));
        this.deactivated = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.foundingUser = ((String) in.readValue((String.class.getClassLoader())));
        this.learnAboutSite = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.isTopCommenter = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.isMaster = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.description = ((String) in.readValue((String.class.getClassLoader())));
    }

    public User() {
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

    public Integer getTotalLikes() {
        return totalLikes;
    }

    public void setTotalLikes(Integer totalLikes) {
        this.totalLikes = totalLikes;
    }

    public Integer getGoldStars() {
        return goldStars;
    }

    public void setGoldStars(Integer goldStars) {
        this.goldStars = goldStars;
    }

    public Integer getSliverStars() {
        return sliverStars;
    }

    public void setSliverStars(Integer sliverStars) {
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

    public Integer getDeactivated() {
        return deactivated;
    }

    public void setDeactivated(Integer deactivated) {
        this.deactivated = deactivated;
    }

    public String getFoundingUser() {
        return foundingUser;
    }

    public void setFoundingUser(String foundingUser) {
        this.foundingUser = foundingUser;
    }

    public Integer getLearnAboutSite() {
        return learnAboutSite;
    }

    public void setLearnAboutSite(Integer learnAboutSite) {
        this.learnAboutSite = learnAboutSite;
    }

    public Integer getIsTopCommenter() {
        return isTopCommenter;
    }

    public void setIsTopCommenter(Integer isTopCommenter) {
        this.isTopCommenter = isTopCommenter;
    }

    public Integer getIsMaster() {
        return isMaster;
    }

    public void setIsMaster(Integer isMaster) {
        this.isMaster = isMaster;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void writeToParcel(Parcel dest, int flags) {
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
