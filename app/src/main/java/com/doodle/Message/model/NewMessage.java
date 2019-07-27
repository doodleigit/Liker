
package com.doodle.Message.model;

import java.io.Serializable;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

public class NewMessage implements Serializable, Parcelable
{

    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("to_user_id")
    @Expose
    private String toUserId;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("return_result")
    @Expose
    private Boolean returnResult;
    @SerializedName("time_posted")
    @Expose
    private String timePosted;
    @SerializedName("user_data")
    @Expose
    private SenderData senderData;
    @SerializedName("insert_id")
    @Expose
    private String insertId;
    @SerializedName("unread_total")
    @Expose
    private String unreadTotal;
    public final static Creator<NewMessage> CREATOR = new Creator<NewMessage>() {


        @SuppressWarnings({
            "unchecked"
        })
        public NewMessage createFromParcel(Parcel in) {
            return new NewMessage(in);
        }

        public NewMessage[] newArray(int size) {
            return (new NewMessage[size]);
        }

    }
    ;
    private final static long serialVersionUID = -2952525196262867240L;

    protected NewMessage(Parcel in) {
        this.userId = ((String) in.readValue((String.class.getClassLoader())));
        this.toUserId = ((String) in.readValue((String.class.getClassLoader())));
        this.message = ((String) in.readValue((String.class.getClassLoader())));
        this.returnResult = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
        this.timePosted = ((String) in.readValue((String.class.getClassLoader())));
        this.senderData = ((SenderData) in.readValue((SenderData.class.getClassLoader())));
        this.insertId = ((String) in.readValue((String.class.getClassLoader())));
        this.unreadTotal = ((String) in.readValue((String.class.getClassLoader())));
    }

    public NewMessage() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getToUserId() {
        return toUserId;
    }

    public void setToUserId(String toUserId) {
        this.toUserId = toUserId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getReturnResult() {
        return returnResult;
    }

    public void setReturnResult(Boolean returnResult) {
        this.returnResult = returnResult;
    }

    public String getTimePosted() {
        return timePosted;
    }

    public void setTimePosted(String timePosted) {
        this.timePosted = timePosted;
    }

    public SenderData getSenderData() {
        return senderData;
    }

    public void setSenderData(SenderData senderData) {
        this.senderData = senderData;
    }

    public String getInsertId() {
        return insertId;
    }

    public void setInsertId(String insertId) {
        this.insertId = insertId;
    }

    public String getUnreadTotal() {
        return unreadTotal;
    }

    public void setUnreadTotal(String unreadTotal) {
        this.unreadTotal = unreadTotal;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(userId);
        dest.writeValue(toUserId);
        dest.writeValue(message);
        dest.writeValue(returnResult);
        dest.writeValue(timePosted);
        dest.writeValue(senderData);
        dest.writeValue(insertId);
        dest.writeValue(unreadTotal);
    }

    public int describeContents() {
        return  0;
    }

}
