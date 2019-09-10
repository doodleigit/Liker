package com.doodle.Authentication.view.activity;

import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.doodle.R;
import com.doodle.Setting.view.SettingActivity;

import java.util.Calendar;

public class About extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);
        findViewById(R.id.imgClose).setOnClickListener(this);
        findViewById(R.id.containerTerms).setOnClickListener(this);
        findViewById(R.id.privacyContainer).setOnClickListener(this);
        findViewById(R.id.contactContainer).setOnClickListener(this);

        String cr = String.format(getString(R.string.copyright), Calendar.getInstance().get(Calendar.YEAR));
        TextView copyright = findViewById(R.id.copyright);
        copyright.setText(cr);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.imgClose:
                finish();
                break;
            case R.id.containerTerms:
                Intent termsIntent = new Intent(this, SettingActivity.class);
                termsIntent.putExtra("type", getString(R.string.terms_of_services));
                termsIntent.putExtra("link", getString(R.string.terms_of_services_link));
                startActivity(termsIntent);
                break;
            case R.id.privacyContainer:
                Intent privacyIntent = new Intent(this, SettingActivity.class);
                privacyIntent.putExtra("type", getString(R.string.privacy_policy));
                privacyIntent.putExtra("link", getString(R.string.privacy_policy_link));
                startActivity(privacyIntent);
                break;
            case R.id.contactContainer:
                Intent contactIntent = new Intent(this, SettingActivity.class);
                contactIntent.putExtra("type", getString(R.string.contact));
                contactIntent.putExtra("link", getString(R.string.contact_link));
                startActivity(contactIntent);
                break;

        }
    }
}
