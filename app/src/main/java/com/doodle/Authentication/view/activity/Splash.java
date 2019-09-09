package com.doodle.Authentication.view.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.doodle.Home.view.activity.Home;
import com.doodle.Tool.PrefManager;

public class Splash extends AppCompatActivity {


    PrefManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        manager = new PrefManager(this);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //   setContentView(R.layout.activity_splash);

//        Handler handler=new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                startActivity(new Intent(Splash.this, ForgotPassword.class));
//                finish();
//            }
//        },2000);
        if (manager.getProfileId().isEmpty()) {
         //   startActivity(new Intent(Splash.this, ForgotPassword.class));
            startActivity(new Intent(Splash.this, Welcome.class));
        } else {
            startActivity(new Intent(Splash.this, Home.class));
        }

//        startActivity(new Intent(Splash.this, ProfileActivity.class));

        finish();


    }
}
