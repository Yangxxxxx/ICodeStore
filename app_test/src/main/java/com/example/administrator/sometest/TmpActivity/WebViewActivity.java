package com.example.administrator.sometest.TmpActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.example.administrator.sometest.R;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by ZN_mager on 2016/5/27.
 */
public class WebViewActivity extends Activity {
    private static final String TAG = "WebViewActivity";
    private WebView mWebView;
    public static final String PARAM_KEY_TITLE = "title";
    private Map<String, String> mPageTitles = new HashMap<>();
    private String mTitle;
    private String mUrl;
    private ViewGroup mContainer;
    private ProgressBar mPbProgress;
    private TextView textView;

    public static void start(Context context, String title, String url) {
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra(PARAM_KEY_TITLE, title);
        intent.setData(Uri.parse(url));
        context.startActivity(intent);
    }

    public static void start(Context context, int titleResId, int urlResId) {
        Resources res = context.getResources();
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra(PARAM_KEY_TITLE, res.getString(titleResId));
        intent.setData(Uri.parse(res.getString(urlResId)));
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);



        textView = findViewById(R.id.tv_progress);
        initViews();
    }

    private void initViews() {

        mPbProgress = findViewById(R.id.progress_loading);
        mTitle = getIntent().getStringExtra(PARAM_KEY_TITLE);
        mContainer = findViewById(R.id.webview_container);
        mUrl = getIntent().getDataString();
        mWebView = new WebView(this);
        mContainer.addView(mWebView, 0);
        initWebView();

    }

    private void initWebView() {
        WebSettings settings = mWebView.getSettings();
        mWebView.setBackgroundColor(0);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setSupportMultipleWindows(true);
        mWebView.setWebViewClient(new WebViewClient() {

            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);
                Log.d(TAG, "load resource " + url);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                setPageTitle("");
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                setPageTitle(url);
            }
        });
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                if (!mPageTitles.containsKey(view.getUrl()) || !mPageTitles.values().contains(title)) {
                    mPageTitles.put(view.getUrl(), title);
                    setPageTitle(view.getUrl());
                }

            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                mPbProgress.setProgress(newProgress);
                mPbProgress.setVisibility(newProgress == 100 ? View.INVISIBLE : View.VISIBLE);
                textView.setText("进度： " + newProgress);
            }
        });
        mWebView.loadUrl("http://webluckdraw.rcplatformhk.com/faq/female.html");
    }


    private void setPageTitle(String url) {
    }

    private String getPageTitle(String url) {
        String title = mWebView.getTitle();
        if (title == null) {
            title = "";
        }
        return mPageTitles.containsKey(url) ? mPageTitles.get(url) : title;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (mWebView != null && mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mWebView != null) {
            mWebView.onResume();
            mWebView.resumeTimers();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mWebView != null) {
            mWebView.onPause();
            mWebView.pauseTimers();

        }
    }

    @Override
    public void finish() {
        super.finish();
        if (mWebView != null) {
            mWebView.removeAllViews();
            mContainer.removeView(mWebView);
            mWebView.setTag(null);
            mWebView.clearCache(true);
            mWebView.destroyDrawingCache();
            mWebView.destroy();
            mWebView = null;
        }
    }
}
