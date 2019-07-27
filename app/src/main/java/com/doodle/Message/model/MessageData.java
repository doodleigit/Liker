
package com.doodle.Message.model;

import java.io.Serializable;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MessageData implements Serializable, Parcelable
{

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("from_user_id")
    @Expose
    private String fromUserId;
    @SerializedName("to_user_id")
    @Expose
    private String toUserId;
    @SerializedName("content")
    @Expose
    private String content;
    @SerializedName("time_posted")
    @Expose
    private String timePosted;
    @SerializedName("seen")
    @Expose
    private String seen;
    @SerializedName("report_id")
    @Expose
    private String reportId;
    @SerializedName("report_status")
    @Expose
    private String reportStatus;
    @SerializedName("deleted_by")
    @Expose
    private String deletedBy;
    public final static Creator<MessageData> CREATOR = new Creator<MessageData>() {


        @SuppressWarnings({
            "unchecked"
        })
        public MessageData createFromParcel(Parcel in) {
            return new MessageData(in);
        }

        public MessageData[] newArray(int size) {
            return (new MessageData[size]);
        }

    }
    ;
    private final static long serialVersionUID = -8479514921931815011L;

    protected MessageData(Parcel in) {
        this.id = ((String) in.readValue((String.class.getClassLoader())));
        this.fromUserId = ((String) in.readValue((String.class.getClassLoader())));
        this.toUserId = ((String) in.readValue((String.class.getClassLoader())));
        this.content = ((String) in.readValue((String.class.getClassLoader())));
        this.timePosted = ((String) in.readValue((String.class.getClassLoader())));
        this.seen = ((String) in.readValue((String.class.getClassLoader())));
        this.reportId = ((String) in.readValue((String.class.getClassLoader())));
        this.reportStatus = ((String) in.readValue((String.class.getClassLoader())));
        this.deletedBy = ((String) in.readValue((String.class.getClassLoader())));
    }

    public MessageData() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(String fromUserId) {
        this.fromUserId = fromUserId;
    }

    public String getToUserId() {
        return toUserId;
    }

    public void setToUserId(String toUserId) {
        this.toUserId = toUserId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTimePosted() {
        return timePosted;
    }

    public void setTimePosted(String timePosted) {
        this.timePosted = timePosted;
    }

    public String getSeen() {
        return seen;
    }

    public void setSeen(String seen) {
        this.seen = seen;
    }

    public String getReportId() {
        return reportId;
    }

    public void setReportId(String reportId) {
        this.reportId = reportId;
    }

    public String getReportStatus() {
        return reportStatus;
    }

    public void setReportStatus(String reportStatus) {
        this.reportStatus = reportStatus;
    }

    public String getDeletedBy() {
        return deletedBy;
    }

    public void setDeletedBy(String deletedBy) {
        this.deletedBy = deletedBy;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(fromUserId);
        dest.writeValue(toUserId);
        dest.writeValue(content);
        dest.writeValue(timePosted);
        dest.writeValue(seen);
        dest.writeValue(reportId);
        dest.writeValue(reportStatus);
        dest.writeValue(deletedBy);
    }

    public int describeContents() {
        return  0;
    }

}
