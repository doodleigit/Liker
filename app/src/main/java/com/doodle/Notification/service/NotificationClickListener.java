package com.doodle.Notification.service;

public interface NotificationClickListener {

    void onNotificationPostActionClick(String postId, boolean isCommentAction);
    void onNotificationClick(String notificationId);

}
