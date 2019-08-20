package com.doodle.Setting.view;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.doodle.R;

public class SettingWebViewFragment extends Fragment {

    View view;
    private Toolbar toolbar;
    private TextView tvTitle;
    private WebView webView;

    private ProgressDialog progressDialog;

    private String title, link;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.setting_web_view_fragment_layout, container, false);

        initialComponent();

        return view;
    }

    private void initialComponent() {
        title = getArguments().getString("title");
        link = getArguments().getString("link");

        toolbar = view.findViewById(R.id.toolbar);
        tvTitle = view.findViewById(R.id.title);
        webView = view.findViewById(R.id.webView);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getString(R.string.loading));
        progressDialog.show();

        tvTitle.setText(title);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(link);

        webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            public void onPageFinished(WebView view, String url) {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }
        });

        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

    }

}
