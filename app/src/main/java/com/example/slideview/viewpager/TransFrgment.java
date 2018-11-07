package com.example.slideview.viewpager;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class TransFrgment extends Fragment {

    public static TransFrgment newInstance() {

        Bundle args = new Bundle();

        TransFrgment fragment = new TransFrgment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.first_fragment,container, false);
        WebView webView = (WebView)view.findViewById(R.id.webView1);
        String url = "https://translate.google.com/m/translate?hl=ko";
        webView.setWebViewClient(new WebViewClient()); // 새창열기 없이 웹뷰 내에서 열기
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.loadUrl(url);

        return view;
    }
}
