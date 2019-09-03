package com.doodle.Message.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.doodle.R;

import static com.doodle.Tool.AppConstants.IN_CHAT_MODE;

public class MessageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        IN_CHAT_MODE = true;

        initialFragment();
    }

    private void initialFragment() {
        MessageListFragment messageListFragment = new MessageListFragment();
        android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, messageListFragment).commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        IN_CHAT_MODE = false;
    }
}
