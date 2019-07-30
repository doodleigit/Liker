
package com.doodle.Comment.model;

import java.io.Serializable;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class LinkData_ implements Serializable, Parcelable
{

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("reply_id")
    @Expose
    private String replyId;
    @SerializedName("media_id")
    @Expose
    private String mediaId;
    @SerializedName("link_title")
    @Expose
    private String linkTitle;
    @SerializedName("link_full_url")
    @Expose
    private String linkFullUrl;
    @SerializedName("image_name")
    @Expose
    private String imageName;
    public final static Creator<LinkData_> CREATOR = new Creator<LinkData_>() {


        @SuppressWarnings({
            "unchecked"
        })
        public LinkData_ createFromParcel(Parcel in) {
            return new LinkData_(in);
        }

        public LinkData_[] newArray(int size) {
            return (new LinkData_[size]);
        }

    }
    ;
    private final static long serialVersionUID = -7979584049009878085L;

    protected LinkData_(Parcel in) {
        this.id = ((String) in.readValue((String.class.getClassLoader())));
        this.replyId = ((String) in.readValue((String.class.getClassLoader())));
        this.mediaId = ((String) in.readValue((String.class.getClassLoader())));
        this.linkTitle = ((String) in.readValue((String.class.getClassLoader())));
        this.linkFullUrl = ((String) in.readValue((String.class.getClassLoader())));
        this.imageName = ((String) in.readValue((String.class.getClassLoader())));
    }

    public LinkData_() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReplyId() {
        return replyId;
    }

    public void setReplyId(String replyId) {
        this.replyId = replyId;
    }

    public String getMediaId() {
        return mediaId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }

    public String getLinkTitle() {
        return linkTitle;
    }

    public void setLinkTitle(String linkTitle) {
        this.linkTitle = linkTitle;
    }

    public String getLinkFullUrl() {
        return linkFullUrl;
    }

    public void setLinkFullUrl(String linkFullUrl) {
        this.linkFullUrl = linkFullUrl;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }



    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(replyId);
        dest.writeValue(mediaId);
        dest.writeValue(linkTitle);
        dest.writeValue(linkFullUrl);
        dest.writeValue(imageName);
    }

    public int describeContents() {
        return  0;
    }

}
