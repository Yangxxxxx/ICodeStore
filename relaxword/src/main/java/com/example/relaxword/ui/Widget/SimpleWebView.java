package com.example.relaxword.ui.Widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.example.relaxword.R;


public class SimpleWebView extends RelativeLayout {
    private WebView webView;
    private ProgressBar progressBar;

    public SimpleWebView(Context context) {
        this(context, null);
    }

    public SimpleWebView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SimpleWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.layout_simple_webview, this, true);
        initView();
    }

    public void loadUrl(final String url){
        webView.loadUrl(url);
    }

    public void clearCache(){
        webView.removeAllViews();
        webView.setTag(null);
        webView.clearHistory();
        webView.destroy();
        webView = null;
    }


    private void initView(){
        webView = findViewById(R.id.webview);
        progressBar = findViewById(R.id.progress);
        initWebView(webView);
    }

    private void initWebView(WebView webView){
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.setWebViewClient(new WebViewClient(){

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }
        });

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                progressBar.setProgress(newProgress);
                if(newProgress == 100){
                    progressBar.setVisibility(GONE);
                }
            }
        });
    }
}
