package com.doodle.Setting.view;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.doodle.R;

public class SettingWebViewFragment extends Fragment {

    View view;
    private ImageView close, backward, forward, share, options;
    private TextView tvTitle;
    private WebView webView;
    private ProgressBar progressBar;

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

        close = view.findViewById(R.id.close);
        backward = view.findViewById(R.id.backward);
        forward = view.findViewById(R.id.forward);
        share = view.findViewById(R.id.share);
        options = view.findViewById(R.id.options);
        tvTitle = view.findViewById(R.id.title);
        webView = view.findViewById(R.id.webView);
        progressBar = view.findViewById(R.id.progress_bar);

        tvTitle.setText(link);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(link);

        webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            public void onPageFinished(WebView view, String url) {
                tvTitle.setText(title);
                progressBar.setVisibility(View.INVISIBLE);
            }
        });

        backward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (webView.canGoBack()) {
                    webView.goBack();
                }
            }
        });

        forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (webView.canGoForward()) {
                    webView.goForward();
                }
            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                share();
            }
        });

        options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                options();
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

    }

    private void share() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, link);
        intent.setType("text/plain");
        startActivity(intent);
    }

    private void copyLink() {
        ClipboardManager clipboard = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Link", link);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(getContext(), "Link copied", Toast.LENGTH_SHORT).show();
    }

    private void options() {
        PopupMenu popupMenu = new PopupMenu(getActivity(), options);
        popupMenu.getMenuInflater().inflate(R.menu.web_browser_menu, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                int id = menuItem.getItemId();
                if(id == R.id.forward){
                    if (webView.canGoForward()) {
                        webView.goForward();
                    }
                    return true;
                }
                if(id == R.id.share_to_other_app){
                    share();
                    return true;
                }
                if(id == R.id.copy_link){
                    copyLink();
                    return true;
                }
                if(id == R.id.refresh_page){
                    webView.reload();
                    return true;
                }
                return false;
            }
        });

        popupMenu.show();
    }

}
