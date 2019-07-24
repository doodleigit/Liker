package com.doodle.Authentication.view.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.doodle.R;

public class About extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);
        findViewById(R.id.imgClose).setOnClickListener(this);
        findViewById(R.id.containerTerms).setOnClickListener(this);
        findViewById(R.id.privacyContainer).setOnClickListener(this);
        findViewById(R.id.contactContainer).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.imgClose:
                finish();
                break;
            case R.id.containerTerms:
                startActivity(new Intent(About.this, TermService.class));
                break;
            case R.id.privacyContainer:
                startActivity(new Intent(About.this, Privacy.class));
                break;
            case R.id.contactContainer:
             //   startActivity(new Intent(About.this, Contact.class));
                break;

        }
    }
}
