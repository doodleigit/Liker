
package com.doodle.Home.model;

import java.io.Serializable;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StarContributor implements Serializable, Parcelable
{

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("whole_network")
    @Expose
    private String wholeNetwork;
    @SerializedName("post_category_id")
    @Expose
    private String postCategoryId;
    @SerializedName("sub_cat_id")
    @Expose
    private String subCatId;
    @SerializedName("likes_total")
    @Expose
    private String likesTotal;
    @SerializedName("rank")
    @Expose
    private String rank;
    @SerializedName("rank_percent")
    @Expose
    private String rankPercent;
    @SerializedName("badge")
    @Expose
    private String badge;
    @SerializedName("prev_badge")
    @Expose
    private String prevBadge;
    @SerializedName("site_notify")
    @Expose
    private String siteNotify;
    @SerializedName("push_notify")
    @Expose
    private String pushNotify;
    @SerializedName("prev_rank_notify")
    @Expose
    private String prevRankNotify;
    @SerializedName("bdg1_is_notified")
    @Expose
    private String bdg1IsNotified;
    @SerializedName("bdg5_is_notified")
    @Expose
    private String bdg5IsNotified;
    @SerializedName("last_update")
    @Expose
    private String lastUpdate;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("current_corn")
    @Expose
    private String currentCorn;
    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("last_name")
    @Expose
    private String lastName;
    @SerializedName("photo")
    @Expose
    private String photo;
    @SerializedName("user_name")
    @Expose
    private String userName;
    @SerializedName("gold_stars")
    @Expose
    private String goldStars;
    @SerializedName("sliver_stars")
    @Expose
    private String sliverStars;
    public final static Creator<StarContributor> CREATOR = new Creator<StarContributor>() {


        @SuppressWarnings({
            "unchecked"
        })
        public StarContributor createFromParcel(Parcel in) {
            return new StarContributor(in);
        }

        public StarContributor[] newArray(int size) {
            return (new StarContributor[size]);
        }

    }
    ;
    private final static long serialVersionUID = -4725206240045473235L;

    protected StarContributor(Parcel in) {
        this.id = ((String) in.readValue((String.class.getClassLoader())));
        this.userId = ((String) in.readValue((String.class.getClassLoader())));
        this.wholeNetwork = ((String) in.readValue((String.class.getClassLoader())));
        this.postCategoryId = ((String) in.readValue((String.class.getClassLoader())));
        this.subCatId = ((String) in.readValue((String.class.getClassLoader())));
        this.likesTotal = ((String) in.readValue((String.class.getClassLoader())));
        this.rank = ((String) in.readValue((String.class.getClassLoader())));
        this.rankPercent = ((String) in.readValue((String.class.getClassLoader())));
        this.badge = ((String) in.readValue((String.class.getClassLoader())));
        this.prevBadge = ((String) in.readValue((String.class.getClassLoader())));
        this.siteNotify = ((String) in.readValue((String.class.getClassLoader())));
        this.pushNotify = ((String) in.readValue((String.class.getClassLoader())));
        this.prevRankNotify = ((String) in.readValue((String.class.getClassLoader())));
        this.bdg1IsNotified = ((String) in.readValue((String.class.getClassLoader())));
        this.bdg5IsNotified = ((String) in.readValue((String.class.getClassLoader())));
        this.lastUpdate = ((String) in.readValue((String.class.getClassLoader())));
        this.status = ((String) in.readValue((String.class.getClassLoader())));
        this.currentCorn = ((String) in.readValue((String.class.getClassLoader())));
        this.firstName = ((String) in.readValue((String.class.getClassLoader())));
        this.lastName = ((String) in.readValue((String.class.getClassLoader())));
        this.photo = ((String) in.readValue((String.class.getClassLoader())));
        this.userName = ((String) in.readValue((String.class.getClassLoader())));
        this.goldStars = ((String) in.readValue((String.class.getClassLoader())));
        this.sliverStars = ((String) in.readValue((String.class.getClassLoader())));
    }

    public StarContributor() {
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

    public String getWholeNetwork() {
        return wholeNetwork;
    }

    public void setWholeNetwork(String wholeNetwork) {
        this.wholeNetwork = wholeNetwork;
    }

    public String getPostCategoryId() {
        return postCategoryId;
    }

    public void setPostCategoryId(String postCategoryId) {
        this.postCategoryId = postCategoryId;
    }

    public String getSubCatId() {
        return subCatId;
    }

    public void setSubCatId(String subCatId) {
        this.subCatId = subCatId;
    }

    public String getLikesTotal() {
        return likesTotal;
    }

    public void setLikesTotal(String likesTotal) {
        this.likesTotal = likesTotal;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getRankPercent() {
        return rankPercent;
    }

    public void setRankPercent(String rankPercent) {
        this.rankPercent = rankPercent;
    }

    public String getBadge() {
        return badge;
    }

    public void setBadge(String badge) {
        this.badge = badge;
    }

    public String getPrevBadge() {
        return prevBadge;
    }

    public void setPrevBadge(String prevBadge) {
        this.prevBadge = prevBadge;
    }

    public String getSiteNotify() {
        return siteNotify;
    }

    public void setSiteNotify(String siteNotify) {
        this.siteNotify = siteNotify;
    }

    public String getPushNotify() {
        return pushNotify;
    }

    public void setPushNotify(String pushNotify) {
        this.pushNotify = pushNotify;
    }

    public String getPrevRankNotify() {
        return prevRankNotify;
    }

    public void setPrevRankNotify(String prevRankNotify) {
        this.prevRankNotify = prevRankNotify;
    }

    public String getBdg1IsNotified() {
        return bdg1IsNotified;
    }

    public void setBdg1IsNotified(String bdg1IsNotified) {
        this.bdg1IsNotified = bdg1IsNotified;
    }

    public String getBdg5IsNotified() {
        return bdg5IsNotified;
    }

    public void setBdg5IsNotified(String bdg5IsNotified) {
        this.bdg5IsNotified = bdg5IsNotified;
    }

    public String getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCurrentCorn() {
        return currentCorn;
    }

    public void setCurrentCorn(String currentCorn) {
        this.currentCorn = currentCorn;
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

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(userId);
        dest.writeValue(wholeNetwork);
        dest.writeValue(postCategoryId);
        dest.writeValue(subCatId);
        dest.writeValue(likesTotal);
        dest.writeValue(rank);
        dest.writeValue(rankPercent);
        dest.writeValue(badge);
        dest.writeValue(prevBadge);
        dest.writeValue(siteNotify);
        dest.writeValue(pushNotify);
        dest.writeValue(prevRankNotify);
        dest.writeValue(bdg1IsNotified);
        dest.writeValue(bdg5IsNotified);
        dest.writeValue(lastUpdate);
        dest.writeValue(status);
        dest.writeValue(currentCorn);
        dest.writeValue(firstName);
        dest.writeValue(lastName);
        dest.writeValue(photo);
        dest.writeValue(userName);
        dest.writeValue(goldStars);
        dest.writeValue(sliverStars);
    }

    public int describeContents() {
        return  0;
    }

}
