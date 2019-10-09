
package com.doodle.Home.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LikeUsers implements Serializable, Parcelable
{

    @SerializedName("like_user")
    @Expose
    private ArrayList<LikeUser> likeUser = null;
    @SerializedName("reloadUser")
    @Expose
    private String reloadUser;
    @SerializedName("cpage")
    @Expose
    private Integer cpage;
    @SerializedName("offset")
    @Expose
    private Integer offset;
    @SerializedName("refload")
    @Expose
    private Boolean refload;
    @SerializedName("total_like")
    @Expose
    private String totalLike;
    @SerializedName("current_user")
    @Expose
    private String currentUser;
    public final static Creator<LikeUsers> CREATOR = new Creator<LikeUsers>() {


        @SuppressWarnings({
            "unchecked"
        })
        public LikeUsers createFromParcel(Parcel in) {
            return new LikeUsers(in);
        }

        public LikeUsers[] newArray(int size) {
            return (new LikeUsers[size]);
        }

    }
    ;
    private final static long serialVersionUID = 8014592890285715712L;

    protected LikeUsers(Parcel in) {
        in.readList(this.likeUser, (com.doodle.Home.model.LikeUser.class.getClassLoader()));
        this.reloadUser = ((String) in.readValue((String.class.getClassLoader())));
        this.cpage = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.offset = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.refload = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
        this.totalLike = ((String) in.readValue((String.class.getClassLoader())));
        this.currentUser = ((String) in.readValue((String.class.getClassLoader())));
    }

    public LikeUsers() {
    }

    public ArrayList<LikeUser> getLikeUser() {
        return likeUser;
    }

    public void setLikeUser(ArrayList<LikeUser> likeUser) {
        this.likeUser = likeUser;
    }

    public String getReloadUser() {
        return reloadUser;
    }

    public void setReloadUser(String reloadUser) {
        this.reloadUser = reloadUser;
    }

    public Integer getCpage() {
        return cpage;
    }

    public void setCpage(Integer cpage) {
        this.cpage = cpage;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public Boolean getRefload() {
        return refload;
    }

    public void setRefload(Boolean refload) {
        this.refload = refload;
    }

    public String getTotalLike() {
        return totalLike;
    }

    public void setTotalLike(String totalLike) {
        this.totalLike = totalLike;
    }

    public String getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(String currentUser) {
        this.currentUser = currentUser;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(likeUser);
        dest.writeValue(reloadUser);
        dest.writeValue(cpage);
        dest.writeValue(offset);
        dest.writeValue(refload);
        dest.writeValue(totalLike);
        dest.writeValue(currentUser);
    }

    public int describeContents() {
        return  0;
    }

}
