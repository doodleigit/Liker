package com.doodle.Authentication.view.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.doodle.Authentication.model.SocialInfo;
import com.doodle.R;
import com.doodle.Tool.PrefManager;
import com.squareup.picasso.Picasso;

import static com.doodle.Authentication.view.activity.Login.SOCIAL_ITEM;

public class LoginAgain extends AppCompatActivity implements View.OnClickListener {

    private PrefManager manager;
    private ImageView profileImage;
    private TextView tvProfileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.login_again);

        manager = new PrefManager(this);

        findViewById(R.id.tvLoginPage).setOnClickListener(this);
        findViewById(R.id.tvSignUpPage).setOnClickListener(this);
        findViewById(R.id.profile_layout).setOnClickListener(this);
        profileImage = findViewById(R.id.profile_image);
        tvProfileName = findViewById(R.id.tvProfileName);

        String image_url = manager.getProfileImage();
        String profileName = manager.getProfileName();

        if (image_url != null && image_url.length() > 0) {
            Picasso.with(LoginAgain.this)
                    .load(image_url)
                    .placeholder(R.drawable.profile)
                    .into(profileImage);

        }
        if (profileName != null && profileName.length() > 0) {
            tvProfileName.setText(profileName);
        }

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.tvLoginPage:
                startActivity(new Intent(this, Login.class));
                break;
            case R.id.tvSignUpPage:
                SocialInfo info = new SocialInfo("","","","","","","");
                Intent intent=new Intent(this,Signup.class);
                intent.putExtra(SOCIAL_ITEM,info);
                startActivity(intent);
                break;
            case R.id.profile_layout:
//                startActivity(new Intent(this, Signup.class));
                break;
        }

    }
}
