package com.doodle.Tool;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.Objects;

public class ScreenOnOffBroadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Objects.requireNonNull(intent.getAction()).equals(Intent.ACTION_SCREEN_ON)) {
            context.sendBroadcast((new Intent()).setAction(AppConstants.RECONNECT_SOCKET_BROADCAST));
//            Toast.makeText(context, "Working", Toast.LENGTH_LONG).show();
        }
    }
}
