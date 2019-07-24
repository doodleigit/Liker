package com.doodle.Authentication.view.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.doodle.R;

public class TwitterAuthenticationActivity extends AppCompatActivity {
    public final static String EXTRA_URL = "extra_url";
    private static final String TAG = TwitterAuthenticationActivity.class
            .getSimpleName();
    private WebView mWebView = null;
    private ProgressDialog mDialog = null;
    private Activity mActivity = null;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = this;
        setContentView(R.layout.activity_webview);
        toolbar = (Toolbar) findViewById(R.id.main_activiy_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Liker");
        mWebView = (WebView) findViewById(R.id.webView);
        final String url = this.getIntent().getStringExtra(EXTRA_URL);
        if (null == url) {
            finish();
        }
        mWebView.setWebViewClient(new MyWebViewClient());
        mWebView.loadUrl(url);
    }

    @Override
    protected void onStop() {
        cancelProgressDialog();
        super.onStop();
    }

    @Override
    protected void onPause() {
        cancelProgressDialog();
        super.onPause();
    }

    private void cancelProgressDialog() {
        if (mDialog != null) {
            mDialog.dismiss();
            mDialog.cancel();
            mDialog = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.onRestart();
    }

    private class MyWebViewClient extends WebViewClient {

        @Override
        public void onPageFinished(WebView view, String url) {
            try {
                if (mDialog != null && mDialog.isShowing()) {
                    mDialog.dismiss();
                    mDialog = null;
                }
            } catch (Exception exception) {
            }
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            if (mDialog == null)
                mDialog = new ProgressDialog(TwitterAuthenticationActivity.this);
            mDialog.setMessage("Loading..");

            if (!(mActivity.isFinishing())) {
                mDialog.show();
            }
        }

        @Override
        public void onLoadResource(WebView view, String url) {
            Log.i(TAG, "Loading Resources");
            Log.i(TAG,
                    "Resource Loading Progress : " + view.getProgress());
            if (view.getProgress() >= 70) {
                cancelProgressDialog();
            }
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Uri uri = Uri.parse(url);
            String verifier = uri.getQueryParameter("oauth_verifier");
            Intent resultIntent = new Intent();
            resultIntent.putExtra("oauth_verifier", verifier);
            setResult(RESULT_OK, resultIntent);
            finish();
            return true;

        }
    }
}