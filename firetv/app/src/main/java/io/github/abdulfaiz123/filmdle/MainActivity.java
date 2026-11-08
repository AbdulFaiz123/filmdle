package io.github.abdulfaiz123.filmdle;

import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * The whole Fire TV app: a full-screen WebView showing the Filmdle website.
 *
 * Why an app instead of the Silk browser? Silk turns the remote into a mouse pointer.
 * Inside a WebView, the remote's arrow buttons arrive as real arrow-key presses and
 * OK arrives as Enter, so the game's own remote navigation works.
 */
public class MainActivity extends Activity {

    private static final String GAME_URL = "https://abdulfaiz123.github.io/filmdle/";
    private static final String GAME_HOST = "abdulfaiz123.github.io";

    private WebView web;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Debug builds can be inspected from a computer (Chrome DevTools over adb); release builds cannot
        if ((getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0) {
            WebView.setWebContentsDebuggingEnabled(true);
        }
        web = new WebView(this);
        setContentView(web);

        WebSettings settings = web.getSettings();
        settings.setJavaScriptEnabled(true);                // the game is JavaScript
        settings.setDomStorageEnabled(true);                // localStorage: stats, streaks, today's guesses
        settings.setMediaPlaybackRequiresUserGesture(false); // let the sound effects play
        // Tell the page it is running inside the TV app
        settings.setUserAgentString(settings.getUserAgentString() + " FilmdleTV");

        web.setWebViewClient(new WebViewClient() {
            // Stay on the game's own website; ignore links to anywhere else (no other browser on a TV)
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                Uri url = request.getUrl();
                return !GAME_HOST.equals(url.getHost());
            }

            // No internet: show a short message instead of the WebView's error page
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                if (request.isForMainFrame()) showOfflinePage();
            }
        });

        if (savedInstanceState != null) {
            web.restoreState(savedInstanceState);
        } else {
            web.loadUrl(GAME_URL);
        }
        web.requestFocus(); // so the remote's button presses go to the page
    }

    private void showOfflinePage() {
        String html = "<body style='background:#160a1e;color:#fbeefe;font:28px sans-serif;"
                + "display:grid;place-items:center;height:90vh;text-align:center'>"
                + "<div><p>" + getString(R.string.offline) + "</p>"
                + "<button autofocus onclick=\"location.href='" + GAME_URL + "'\" "
                + "style='font-size:28px;padding:12px 32px;border-radius:14px;border:0;"
                + "background:#ff3ea5;color:#fff'>OK</button></div></body>";
        web.loadDataWithBaseURL(GAME_URL, html, "text/html", "utf-8", null);
    }

    // The remote's Back button: close an open popup first; only leave the app when nothing is open
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            web.evaluateJavascript(
                    "(function(){var d=document.querySelector('dialog[open]');if(d){d.close();return true;}return false;})()",
                    closed -> { if (!"true".equals(closed)) finish(); });
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        web.saveState(outState);
    }

    @Override
    protected void onPause() {
        web.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        web.onResume();
    }

    @Override
    protected void onDestroy() {
        web.destroy();
        super.onDestroy();
    }
}
