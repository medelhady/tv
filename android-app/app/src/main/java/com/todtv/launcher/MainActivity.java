package com.todtv.launcher;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.net.Uri;
import android.view.KeyEvent;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity {
    private WebView web;
    private String urlArabic = "https://www.tod.tv/ar";
    private String urlEnglish = "https://www.tod.tv/en";

    public void onCreate(Bundle b) {
        super.onCreate(b);
        showMenu();
    }

    private void showMenu() {
        LinearLayout box = new LinearLayout(this);
        box.setOrientation(LinearLayout.VERTICAL);
        box.setPadding(28, 28, 28, 28);
        box.setBackgroundColor(0xff081018);

        TextView title = new TextView(this);
        title.setText("TOD TV Launcher");
        title.setTextColor(0xffffffff);
        title.setTextSize(30);
        title.setPadding(0, 0, 0, 22);
        box.addView(title);

        Button b1 = btn("Open TOD Arabic inside app");
        b1.setOnClickListener(v -> openInside(urlArabic, false));
        box.addView(b1);

        Button b2 = btn("Open TOD English inside app");
        b2.setOnClickListener(v -> openInside(urlEnglish, false));
        box.addView(b2);

        Button b3 = btn("Open TOD with Desktop UA");
        b3.setOnClickListener(v -> openInside(urlArabic, true));
        box.addView(b3);

        Button b4 = btn("Open TOD in external browser");
        b4.setOnClickListener(v -> openExternal(urlArabic));
        box.addView(b4);

        Button b5 = btn("Clear cookies then open");
        b5.setOnClickListener(v -> {
            CookieManager.getInstance().removeAllCookies(null);
            CookieManager.getInstance().flush();
            openInside(urlArabic, true);
        });
        box.addView(b5);

        TextView note = new TextView(this);
        note.setText("Use arrows and OK. If inside app keeps loading, try external browser.");
        note.setTextColor(0xffcbd8e6);
        note.setTextSize(18);
        note.setPadding(0, 22, 0, 0);
        box.addView(note);

        setContentView(box);
        b1.requestFocus();
    }

    private Button btn(String text) {
        Button b = new Button(this);
        b.setText(text);
        b.setTextSize(22);
        b.setAllCaps(false);
        b.setPadding(12, 18, 12, 18);
        return b;
    }

    private void openInside(String target, boolean desktopUa) {
        web = new WebView(this);
        setContentView(web);

        WebSettings s = web.getSettings();
        s.setJavaScriptEnabled(true);
        s.setDomStorageEnabled(true);
        s.setDatabaseEnabled(true);
        s.setMediaPlaybackRequiresUserGesture(false);
        s.setUseWideViewPort(true);
        s.setLoadWithOverviewMode(true);
        s.setSupportZoom(false);

        if (desktopUa) {
            s.setUserAgentString("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/145.0.0.0 Safari/537.36");
        }

        CookieManager cm = CookieManager.getInstance();
        cm.setAcceptCookie(true);
        cm.setAcceptThirdPartyCookies(web, true);

        web.setWebChromeClient(new WebChromeClient());
        web.setWebViewClient(new WebViewClient());
        web.loadUrl(target);
    }

    private void openExternal(String target) {
        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(target));
        startActivity(i);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (web != null && web.canGoBack()) {
                web.goBack();
                return true;
            }
            showMenu();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}