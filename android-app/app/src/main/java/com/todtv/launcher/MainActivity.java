package com.todtv.launcher;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.content.Intent;
import android.net.Uri;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity {
    private WebView webView;
    private LinearLayout menu;
    private final String todUrl = "https://www.tod.tv/ar";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showMenu();
    }

    private void showMenu() {
        menu = new LinearLayout(this);
        menu.setOrientation(LinearLayout.VERTICAL);
        menu.setPadding(32, 32, 32, 32);
        menu.setBackgroundColor(0xff081018);

        TextView title = new TextView(this);
        title.setText("TOD TV Launcher");
        title.setTextColor(0xffffffff);
        title.setTextSize(30);
        title.setPadding(0, 0, 0, 24);
        menu.addView(title);

        Button openInside = makeButton("Open TOD inside app");
        openInside.setOnClickListener(v -> openInsideApp());
        menu.addView(openInside);

        Button openExternal = makeButton("Open TOD in browser");
        openExternal.setOnClickListener(v -> openExternalBrowser());
        menu.addView(openExternal);

        Button reload = makeButton("Clear session and open TOD");
        reload.setOnClickListener(v -> {
            CookieManager.getInstance().removeAllCookies(null);
            CookieManager.getInstance().flush();
            openInsideApp();
        });
        menu.addView(reload);

        TextView note = new TextView(this);
        note.setText("Use remote arrows and OK. If TOD keeps loading inside the app, try the external browser option.");
        note.setTextColor(0xffcbd8e6);
        note.setTextSize(18);
        note.setPadding(0, 24, 0, 0);
        menu.addView(note);

        setContentView(menu);
        openInside.requestFocus();
    }

    private Button makeButton(String text) {
        Button b = new Button(this);
        b.setText(text);
        b.setTextSize(24);
        b.setAllCaps(false);
        b.setPadding(16, 20, 16, 20);
        return b;
    }

    private void openInsideApp() {
        webView = new WebView(this);
        setContentView(webView);

        WebSettings s = webView.getSettings();
        s.setJavaScriptEnabled(true);
        s.setDomStorageEnabled(true);
        s.setDatabaseEnabled(true);
        s.setMediaPlaybackRequiresUserGesture(false);
        s.setLoadWithOverviewMode(true);
        s.setUseWideViewPort(true);
        s.setSupportZoom(false);
        s.setBuiltInZoomControls(false);

        CookieManager cm = CookieManager.getInstance();
        cm.setAcceptCookie(true);
        cm.setAcceptThirdPartyCookies(webView, true);

        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(todUrl);
    }

    private void openExternalBrowser() {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(todUrl));
        startActivity(intent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (webView != null && webView.canGoBack()) {
                webView.goBack();
                return true;
            }
            showMenu();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
