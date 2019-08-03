package com.doodle.Profile.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.doodle.R;

public class ProfileActivity extends AppCompatActivity {

    private String profileUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        initialComponent();
        initialFragment();
    }

    private void initialComponent() {
        profileUserId = getIntent().getStringExtra("user_id");
    }

    private void initialFragment() {
        Bundle bundle = new Bundle();
        bundle.putString("user_id", profileUserId);
        ProfileFragment profileFragment = new ProfileFragment();
        android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        profileFragment.setArguments(bundle);
        transaction.replace(R.id.container, profileFragment).commit();
    }

}
