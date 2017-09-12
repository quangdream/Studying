package vn.edu.ptit.miniproject.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import java.io.File;

import vn.edu.ptit.miniproject.R;
import vn.edu.ptit.miniproject.fragment.NewsFragment;

/**
 * Created by QuangPC on 7/24/2017.
 */

public class WebViewActivity extends AppCompatActivity {
    private WebView webView;
    private ProgressDialog dialog;
    private String link = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_view_layout);
        webView = (WebView) findViewById(R.id.webView);
        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading...");
        dialog.setIndeterminate(false);
        dialog.setCancelable(false);
        webView.setWebViewClient(new MyBrower(dialog));
        Intent intent = getIntent();
        link = intent.getStringExtra(NewsFragment.LINK);
        Log.e("LINK",link);

        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        if(!isNetworkAvailable() && isSaved()) {
            Toast.makeText(this, "OFFLINE", Toast.LENGTH_SHORT).show();
            webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
            String[] path = link.split("/");
            link = path[path.length - 1];
            link = "file:////" + NewsFragment.PATH + link;
        }else {
            Toast.makeText(this, "ONLINE", Toast.LENGTH_SHORT).show();
        }
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.loadUrl(link);
    }
    private class MyBrower extends WebViewClient {
        private ProgressDialog dialog;

        public MyBrower(ProgressDialog dialog){
            this.dialog = dialog;
            dialog.show();
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            dialog.dismiss();
        }
    }


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService( CONNECTIVITY_SERVICE );
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private boolean isSaved() {
        String[] path = link.split("/");
        String l = path[path.length-1];
        File file = new File(NewsFragment.PATH+l);
        return file.exists();
    }
}
