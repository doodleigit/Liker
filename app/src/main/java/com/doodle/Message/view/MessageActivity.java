package com.doodle.Message.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.doodle.Home.service.SocketIOManager;
import com.doodle.R;

import io.socket.client.Socket;

public class MessageActivity extends AppCompatActivity {

    public Socket socket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        initialFragment();
    }

    private void initialFragment() {
        MessageListFragment messageListFragment = new MessageListFragment();
        android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, messageListFragment).commit();
    }

}
