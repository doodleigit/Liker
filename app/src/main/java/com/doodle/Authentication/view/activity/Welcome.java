package com.doodle.Authentication.view.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.doodle.Authentication.model.SocialInfo;
import com.doodle.R;

import static com.doodle.Authentication.view.activity.Login.SOCIAL_ITEM;

public class Welcome extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_welcome);
        findViewById(R.id.btnLogin).setOnClickListener(this);
        findViewById(R.id.tvSignup).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btnLogin:
                startActivity(new Intent(this, Login.class));
                break;
            case R.id.tvSignup:
                SocialInfo info = new SocialInfo("","","","","","","");
                Intent intent = new Intent(this, Signup.class);
                intent.putExtra(SOCIAL_ITEM, info);
                startActivity(intent);
                break;
        }
    }
}
