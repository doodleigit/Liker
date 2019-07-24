package com.doodle.Authentication.view.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.doodle.R;
import com.doodle.utils.PrefManager;
import com.squareup.picasso.Picasso;

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
        findViewById(R.id.tvFacebookPage).setOnClickListener(this);
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
                startActivity(new Intent(this, Welcome.class));
                finish();
                break;
            case R.id.tvFacebookPage:
                startActivity(new Intent(this, FBLogin.class));
                finish();
                break;
        }

    }
}
