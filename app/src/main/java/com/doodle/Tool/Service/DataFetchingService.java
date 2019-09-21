package com.doodle.Tool.Service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.doodle.Home.service.SocketIOManager;
import com.doodle.Message.model.NewMessage;
import com.doodle.Message.model.SenderData;
import com.doodle.Tool.AppConstants;
import com.doodle.Tool.ScreenOnOffBroadcast;

import org.json.JSONException;
import org.json.JSONObject;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;

import static com.doodle.Tool.AppConstants.IN_CHAT_MODE;

public class DataFetchingService extends Service {

    private Socket socket, mSocket;
    private BroadcastReceiver mReceiver;

    @Override
    public void onCreate() {
        super.onCreate();
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
                }
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
                }
            }
        });
    }

    private void setBroadcast() {
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        mReceiver = new ScreenOnOffBroadcast();
        registerReceiver(mReceiver, filter);
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
