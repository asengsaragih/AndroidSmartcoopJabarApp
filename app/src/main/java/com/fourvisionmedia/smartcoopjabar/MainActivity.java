package com.fourvisionmedia.smartcoopjabar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.webkit.WebSettings;
import android.widget.Toast;

import im.delight.android.webview.AdvancedWebView;

public class MainActivity extends AppCompatActivity implements AdvancedWebView.Listener {

    private static final String TAG = "MAINTAGCHECK";
    private AdvancedWebView mMainWebView;
    private SwipeRefreshLayout mRefreshLayout;

    private boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMainWebView = findViewById(R.id.webView_main);
        mRefreshLayout = findViewById(R.id.swipe_container);

        //intialize webview
        initializeWebView();

        //refresh ketika page no internet muncul
        mRefreshLayout.setOnRefreshListener(() -> {
            initializeWebView();
            mRefreshLayout.setRefreshing(false);
        });
    }

    private void initializeWebView() {
        //listener implementation
        mMainWebView.setListener(this, this);

        //content allowed
        mMainWebView.setMixedContentAllowed(false);
        mMainWebView.getSettings().setJavaScriptEnabled(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mMainWebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        } else {
            mMainWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        }

        mMainWebView.loadUrl("http://diskumkm.koperasijabar.com/mobile");

        mRefreshLayout.setOnRefreshListener(() -> {
            mMainWebView.loadUrl(mMainWebView.getUrl());
            mRefreshLayout.setRefreshing(false);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMainWebView.onResume();
    }

    @Override
    protected void onPause() {
        mMainWebView.onPause();
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        if (mMainWebView.getUrl().equals(getString(R.string.base_url_home))) {

            if (doubleBackToExitPressedOnce)
                super.onBackPressed();

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Click back again to exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);

        } else if (mMainWebView.getUrl().equals(getString(R.string.base_url_login))) {
            super.onBackPressed();
        } else {
            mMainWebView.goBack();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mMainWebView.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onPageStarted(String url, Bitmap favicon) {
        Log.d(TAG, "onPageStarted Url: " + url);
    }

    @Override
    public void onPageFinished(String url) {
        Log.d(TAG, "onPageFinished: " + url);
        if (url.contains(getString(R.string.base_url_logout))) {
            mMainWebView.loadUrl("http://diskumkm.koperasijabar.com/mobile");
        }
    }

    @Override
    public void onPageError(int errorCode, String description, String failingUrl) {
        switch (errorCode) {
            case -2:
                mMainWebView.loadUrl("file:///android_res/raw/error.html");
                break;
            case -11:
                break;
        }
        Log.d(TAG, "onPageError Code : " + errorCode);
        Log.d(TAG, "onPageError Desc: " + description);
        Log.d(TAG, "onPageError: " + failingUrl);
    }

    @Override
    public void onDownloadRequested(String url, String suggestedFilename, String mimeType, long contentLength, String contentDisposition, String userAgent) {
        Log.d(TAG, "onDownloadRequested url: " + url);
        Log.d(TAG, "onDownloadRequested suggestedFilename: " + suggestedFilename);
        Log.d(TAG, "onDownloadRequested mimeType: " + mimeType);
        Log.d(TAG, "onDownloadRequested contentLength: " + contentLength);
        Log.d(TAG, "onDownloadRequested contentDisposition: " + contentDisposition);
        Log.d(TAG, "onDownloadRequested userAgent: " + userAgent);
    }

    @Override
    public void onExternalPageRequest(String url) {
        Log.d(TAG, "onExternalPageRequest URL: " + url);
    }
}