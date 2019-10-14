package com.doodle.Tool.Service;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.IBinder;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.RemoteViews;

import com.doodle.Home.model.PostItem;
import com.doodle.Home.service.SocketIOManager;
import com.doodle.Home.view.activity.StarContributorActivity;
import com.doodle.Message.model.NewMessage;
import com.doodle.Message.model.SenderData;
import com.doodle.Notification.model.BadgeInfo;
import com.doodle.Notification.model.Data;
import com.doodle.Notification.model.NotificationItem;
import com.doodle.Notification.service.NotificationService;
import com.doodle.Notification.view.NotificationActivity;
import com.doodle.Post.view.activity.PostPopup;
import com.doodle.Profile.view.ProfileActivity;
import com.doodle.R;
import com.doodle.Tool.AppConstants;
import com.doodle.Tool.PrefManager;
import com.doodle.Tool.ScreenOnOffBroadcast;
import com.doodle.Tool.Tools;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.Calendar;
import java.util.Locale;
import java.util.Random;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.doodle.Tool.AppConstants.IN_CHAT_MODE;

public class DataFetchingService extends Service {

    NotificationManager notificationManager = null;
    private Socket socket, mSocket;
    private BroadcastReceiver mReceiver;
    String KEY_NOTIFICATION_GROUP = "LIKER_PUSH_NOTIFICATION";

    private NotificationService webService;
    private String deviceId, profileId, token, userIds;
    private PrefManager manager;

    @Override
    public void onCreate() {
        super.onCreate();

        manager = new PrefManager(this);
        deviceId = manager.getDeviceId();
        profileId = manager.getProfileId();
        token = manager.getToken();
        userIds = manager.getProfileId();
        webService = NotificationService.mRetrofit.create(NotificationService.class);

        if (socket != null) {
            if (!socket.connected()) {
                socket = new SocketIOManager().getWSocketInstance();
//                Toast.makeText(context, "Web Socket Reconnected", Toast.LENGTH_LONG).show();
            }
        } else {
            socket = new SocketIOManager().getWSocketInstance();
        }
        if (mSocket != null) {
            if (!mSocket.connected()) {
                mSocket = new SocketIOManager().getMSocketInstance();
//                Toast.makeText(context, "Web Socket Reconnected", Toast.LENGTH_LONG).show();
            }
        } else {
            mSocket = new SocketIOManager().getMSocketInstance();
        }
        setBroadcast();
        getNotificationData();
        getMessagesData();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (!socket.connected()) {
            socket = new SocketIOManager().getWSocketInstance();
//                Toast.makeText(context, "Web Socket Reconnected", Toast.LENGTH_LONG).show();
        }
        if (!mSocket.connected()) {
            mSocket = new SocketIOManager().getMSocketInstance();
//                Toast.makeText(context, "Message Socket Reconnected", Toast.LENGTH_LONG).show();
        }
        return START_NOT_STICKY;
    }

    private void getNotificationData() {
        socket.on("web_notification", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                sendBroadcast((new Intent().putExtra("type", "0")).setAction(AppConstants.NEW_NOTIFICATION_BROADCAST));
//                try {
//                    JSONObject object = new JSONObject(args[0].toString());
//                    NotificationItem notificationItem = new NotificationItem();
//                    Data data = new Data();
//
//                    notificationItem.setText(object.getString("text"));
//                    notificationItem.setData(data);
//                    JSONObject jsonObject = object.getJSONObject("data");
//                    notificationItem.getData().setId(jsonObject.getString("id"));
//                    notificationItem.getData().setUserId(jsonObject.getString("user_id"));
//                    notificationItem.getData().setFromUserId(jsonObject.getString("from_user_id"));
//                    notificationItem.getData().setNotifType(jsonObject.getString("notif_type"));
//                    notificationItem.getData().setTypeId(jsonObject.getString("type_id"));
//                    notificationItem.getData().setTimeSent(jsonObject.getString("time_sent"));
//                    notificationItem.getData().setHasSeen(jsonObject.getString("has_seen"));
//                    notificationItem.getData().setHasSeenDetails(jsonObject.getString("has_seen_details"));
//                    notificationItem.getData().setCategoryId(jsonObject.getString("category_id"));
//                    notificationItem.getData().setSubCatgId(jsonObject.getString("sub_catg_id"));
//                    notificationItem.getData().setUsername(jsonObject.getString("username"));
//                    notificationItem.getData().setFirstName(jsonObject.getString("first_name"));
//                    notificationItem.getData().setLastName(jsonObject.getString("last_name"));
//                    notificationItem.getData().setPhoto(jsonObject.getString("photo"));
//                    notificationItem.getData().setGoldStars(jsonObject.getString("gold_stars"));
//                    notificationItem.getData().setSliverStars(jsonObject.getString("sliver_stars"));
//                    notificationItem.getData().setCategoryName(jsonObject.getString("category_name"));
//                    notificationItem.getData().setBadgeInfo(new BadgeInfo());
//                    notificationItem.getData().setIsFollowed(jsonObject.getBoolean("is_followed"));
//                    notificationItem.getData().setPostUserUsername(jsonObject.getString("post_user_username"));
//
//                    sendPushNotification(DataFetchingService.this, notificationItem);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
            }
        });
    }

    private void getMessagesData() {
        mSocket.on("message", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                try {
                    JSONObject messageJson = new JSONObject(args[0].toString());
                    NewMessage newMessage = new NewMessage();

                    newMessage.setUserId(messageJson.getString("user_id"));
                    newMessage.setToUserId(messageJson.getString("to_user_id"));
                    newMessage.setMessage(messageJson.getString("message"));
                    newMessage.setReturnResult(messageJson.getBoolean("return_result"));
                    newMessage.setTimePosted(messageJson.getString("time_posted"));
                    newMessage.setInsertId(messageJson.getString("insert_id"));
                    newMessage.setUnreadTotal(messageJson.getString("unread_total"));

                    SenderData senderData = new SenderData();
                    senderData.setId(messageJson.getJSONObject("user_data").getString("id"));
                    senderData.setUserId(messageJson.getJSONObject("user_data").getString("user_id"));
                    senderData.setUserName(messageJson.getJSONObject("user_data").getString("user_name"));
                    senderData.setFirstName(messageJson.getJSONObject("user_data").getString("first_name"));
                    senderData.setLastName(messageJson.getJSONObject("user_data").getString("last_name"));
                    senderData.setTotalLikes(messageJson.getJSONObject("user_data").getString("total_likes"));
                    senderData.setGoldStars(messageJson.getJSONObject("user_data").getString("gold_stars"));
                    senderData.setSliverStars(messageJson.getJSONObject("user_data").getString("sliver_stars"));
                    senderData.setPhoto(messageJson.getJSONObject("user_data").getString("photo"));
                    senderData.setEmail(messageJson.getJSONObject("user_data").getString("email"));
                    senderData.setDeactivated(messageJson.getJSONObject("user_data").getString("deactivated"));
                    senderData.setFoundingUser(messageJson.getJSONObject("user_data").getString("founding_user"));
                    senderData.setLearnAboutSite(messageJson.getJSONObject("user_data").getInt("learn_about_site"));
                    senderData.setIsTopCommenter(messageJson.getJSONObject("user_data").getString("is_top_commenter"));
                    senderData.setIsMaster(messageJson.getJSONObject("user_data").getString("is_master"));
                    senderData.setDescription(messageJson.getJSONObject("user_data").getString("description"));

                    newMessage.setSenderData(senderData);
                    sendBroadcast((new Intent().putExtra("new_message", (Parcelable) newMessage).putExtra("type", 0)).setAction(AppConstants.NEW_MESSAGE_BROADCAST_FROM_HOME));
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (NullPointerException ignored) {}
                if (!IN_CHAT_MODE)
                    sendBroadcast((new Intent().putExtra("type", "1")).setAction(AppConstants.NEW_NOTIFICATION_BROADCAST));
            }
        });

        mSocket.on("message_own", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                try {
                    JSONObject messageJson = new JSONObject(args[0].toString());
                    NewMessage newMessage = new NewMessage();

                    newMessage.setUserId(messageJson.getString("user_id"));
                    newMessage.setToUserId(messageJson.getString("to_user_id"));
                    newMessage.setMessage(messageJson.getString("message"));
                    newMessage.setReturnResult(messageJson.getBoolean("return_result"));
                    newMessage.setTimePosted(messageJson.getString("time_posted"));
                    newMessage.setInsertId(messageJson.getString("insert_id"));
                    newMessage.setUnreadTotal(messageJson.getString("unread_total"));

                    SenderData senderData = new SenderData();
                    senderData.setId(messageJson.getJSONObject("to_user_data").getString("id"));
                    senderData.setUserId(messageJson.getJSONObject("to_user_data").getString("user_id"));
                    senderData.setUserName(messageJson.getJSONObject("to_user_data").getString("user_name"));
                    senderData.setFirstName(messageJson.getJSONObject("to_user_data").getString("first_name"));
                    senderData.setLastName(messageJson.getJSONObject("to_user_data").getString("last_name"));
                    senderData.setTotalLikes(messageJson.getJSONObject("to_user_data").getString("total_likes"));
                    senderData.setGoldStars(messageJson.getJSONObject("to_user_data").getString("gold_stars"));
                    senderData.setSliverStars(messageJson.getJSONObject("to_user_data").getString("sliver_stars"));
                    senderData.setPhoto(messageJson.getJSONObject("to_user_data").getString("photo"));
                    senderData.setEmail(messageJson.getJSONObject("to_user_data").getString("email"));
                    senderData.setDeactivated(messageJson.getJSONObject("to_user_data").getString("deactivated"));
                    senderData.setFoundingUser(messageJson.getJSONObject("to_user_data").getString("founding_user"));
                    senderData.setLearnAboutSite(messageJson.getJSONObject("to_user_data").getInt("learn_about_site"));
                    senderData.setIsTopCommenter(messageJson.getJSONObject("to_user_data").getString("is_top_commenter"));
                    senderData.setIsMaster(messageJson.getJSONObject("to_user_data").getString("is_master"));
                    senderData.setDescription(messageJson.getJSONObject("to_user_data").getString("description"));

                    newMessage.setSenderData(senderData);
                    sendBroadcast((new Intent().putExtra("new_message", (Parcelable) newMessage).putExtra("type", 1)).setAction(AppConstants.NEW_MESSAGE_BROADCAST_FROM_HOME));
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (NullPointerException ignored) {}
            }
        });
    }

    private void setBroadcast() {
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        mReceiver = new ScreenOnOffBroadcast();
        registerReceiver(mReceiver, filter);
    }

    public void sendPushNotification(Context context, NotificationItem notificationItem) {
        Log.d("sendNotification", "true");
        new LoadNotificationMedia(context, notificationItem).execute(AppConstants.PROFILE_IMAGE + notificationItem.getData().getPhoto());
    }

    @SuppressLint("StaticFieldLeak")
    public class LoadNotificationMedia extends AsyncTask<String, Void, Bitmap> {
        Context context;
        NotificationItem notificationItem;

        public LoadNotificationMedia(Context context, NotificationItem notificationItem) {
            this.context = context;
            this.notificationItem = notificationItem;
        }

        @Override
        protected Bitmap doInBackground(String... url) {
            String urldisplay = url[0];
            Bitmap bitmap = null;
            try {
                InputStream srt = new java.net.URL(urldisplay).openStream();
                bitmap = BitmapFactory.decodeStream(srt);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
//            Intent intent = new Intent(context, NotificationActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
//            intent.putExtra("notification_item", (Parcelable) notificationItem);
//            intent.putExtra("is_pushed", true);

            int actionType = Tools.getNotificationTypeActionType(notificationItem.getData().getNotifType());

            if (actionType == 1) {
                Intent intent = new Intent(context, ProfileActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                intent.putExtra("user_id", notificationItem.getData().getFromUserId());
                intent.putExtra("user_name", notificationItem.getData().getUsername());
                pushNotification(notificationItem, bitmap, intent, context);
            } else if (actionType == 2) {
                sendPostItemRequest(notificationItem.getData().getTypeId(), false, notificationItem, bitmap, context);
            } else if (actionType == 3) {
                sendPostItemRequest(notificationItem.getData().getTypeId(), true, notificationItem, bitmap, context);
            } else if (actionType == 4) {
                Intent intent = new Intent(context, StarContributorActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                intent.putExtra("category_id", "");
                intent.putExtra("category_name", "");
                pushNotification(notificationItem, bitmap, intent, context);
            } else if (actionType == 5) {
                Intent intent = new Intent(context, StarContributorActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                intent.putExtra("category_id", notificationItem.getData().getCategoryId());
                intent.putExtra("category_name", notificationItem.getData().getCategoryName());
                pushNotification(notificationItem, bitmap, intent, context);
            } else {
                Intent intent = new Intent();
                pushNotification(notificationItem, bitmap, intent, context);
            }

        }
    }

    private void pushNotification(NotificationItem notificationItem, Bitmap bitmap, Intent intent, Context context) {
        int requestCode = new Random().nextInt();
        int notificationCode = Integer.parseInt(notificationItem.getData().getId().isEmpty() ? String.valueOf(requestCode) : notificationItem.getData().getId());

        PendingIntent pendingIntent = PendingIntent.getActivity(context, requestCode, intent, PendingIntent.FLAG_ONE_SHOT);

        long time = Long.valueOf(notificationItem.getData().getTimeSent());

        RemoteViews contentViews = new RemoteViews(context.getPackageName(), R.layout.push_notification_item);
        contentViews.setTextViewText(R.id.notificationDetails, Tools.colorBackground(notificationItem.getText()));
        contentViews.setTextViewText(R.id.notificationTime, Tools.chatDateCompare(context, time));
        if (bitmap != null) {
            contentViews.setImageViewBitmap(R.id.imageNotification, bitmap);
        } else {
            contentViews.setImageViewResource(R.id.imageNotification, R.drawable.profile);
        }

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_notifications_setting_black_24dp)
                .setCustomContentView(contentViews)
                .setAutoCancel(true)
                .setGroup(KEY_NOTIFICATION_GROUP)
                .setGroupSummary(true)
                .setPriority(android.app.Notification.PRIORITY_HIGH)
                .setVibrate(new long[0])
                .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                .setContentIntent(pendingIntent);

        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(notificationCode, notificationBuilder.build());
    }

    private String getDate(long time) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time * 1000);
        String date = DateFormat.format("dd MMM yyyy hh:mm a", cal).toString();
        return date;
    }

    private void sendPostItemRequest(String postId, boolean isCommentAction, NotificationItem notificationItem, Bitmap bitmap, Context context) {
        Call<PostItem> call = webService.getPostDetails(deviceId, profileId, token, profileId, postId);
        call.enqueue(new Callback<PostItem>() {
            @Override
            public void onResponse(Call<PostItem> call, Response<PostItem> response) {
                PostItem postItem = response.body();
                if (postItem != null) {
                    Intent intent = new Intent(context, PostPopup.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                    intent.putExtra(AppConstants.ITEM_KEY, (Parcelable) postItem);
                    intent.putExtra("has_footer", false);
                    intent.putExtra("is_comment_action", isCommentAction);
                    pushNotification(notificationItem, bitmap, intent, context);
                }
            }

            @Override
            public void onFailure(Call<PostItem> call, Throwable t) {
                Log.d("MESSAGE: ", t.getMessage());
            }
        });
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        socket.off("web_notification");
        mSocket.off("message");
        mSocket.off("message_own");
        unregisterReceiver(mReceiver);
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        socket.off("web_notification");
        mSocket.off("message");
        mSocket.off("message_own");
        unregisterReceiver(mReceiver);
    }
}
