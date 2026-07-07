package com.todtv.launcher;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
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
    private final String todAr = "https://www.tod.tv/ar";
    private final String todEn = "https://www.tod.tv/en";
    private final String portal = "https://tv.vercel.app";

    public void onCreate(Bundle b) {
        super.onCreate(b);
        showMenu();
    }

    private void showMenu() {
        LinearLayout box = new LinearLayout(this);
        box.setOrientation(LinearLayout.VERTICAL);
        box.setPadding(28, 28, 28, 28);
        box.setBackgroundColor(0xff081018);

        TextView title = text("TOD TV Assistant", 30, 0xffffffff);
        title.setPadding(0, 0, 0, 20);
        box.addView(title);

        TextView note = text("If TOD keeps loading, this TV WebView is probably not compatible. Try external browser or install a better browser from the portal.", 17, 0xffcbd8e6);
        note.setPadding(0, 0, 0, 18);
        box.addView(note);

        Button b1 = btn("1 - Open TOD in external browser");
        b1.setOnClickListener(v -> openExternal(todAr));
        box.addView(b1);

        Button b2 = btn("2 - Try TOD inside app");
        b2.setOnClickListener(v -> openInside(todAr, false));
        box.addView(b2);

        Button b3 = btn("3 - Try desktop mode");
        b3.setOnClickListener(v -> openInside(todAr, true));
        box.addView(b3);

        Button b4 = btn("4 - Clear cookies and retry");
        b4.setOnClickListener(v -> {
            CookieManager.getInstance().removeAllCookies(null);
            CookieManager.getInstance().flush();
            openInside(todAr, true);
        });
        box.addView(b4);

        Button b5 = btn("5 - Open English TOD");
        b5.setOnClickListener(v -> openExternal(todEn));
        box.addView(b5);

        Button b6 = btn("6 - Open download portal");
        b6.setOnClickListener(v -> openExternal(portal));
        box.addView(b6);

        Button b7 = btn("7 - Open app settings");
        b7.setOnClickListener(v -> {
            Intent i = new Intent(Settings.ACTION_SETTINGS);
            startActivity(i);
        });
        box.addView(b7);

        TextView footer = text("Back returns to this menu. Use arrows and OK.", 16, 0xff93a4b5);
        footer.setPadding(0, 18, 0, 0);
        box.addView(footer);

        setContentView(box);
        b1.requestFocus();
    }

    private TextView text(String value, int size, int color) {
        TextView t = new TextView(this);
        t.setText(value);
        t.setTextSize(size);
        t.setTextColor(color);
        return t;
    }

    private Button btn(String text) {
        Button b = new Button(this);
        b.setText(text);
        b.setTextSize(21);
        b.setAllCaps(false);
        b.setPadding(12, 16, 12, 16);
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
            web = null;
            showMenu();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}