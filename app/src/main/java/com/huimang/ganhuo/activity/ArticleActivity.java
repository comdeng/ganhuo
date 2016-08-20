package com.huimang.ganhuo.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.huimang.ganhuo.R;

public class ArticleActivity extends AppCompatActivity {
    private WebView webView;
    private String link;
    private String title;
    private String artiUrl;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.activity_article);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        webView = (WebView) findViewById(R.id.webView);
        webView.getSettings().setDisplayZoomControls(false);
        webView.getSettings().setJavaScriptEnabled(true);


        Intent intent = this.getIntent();
        artiUrl = intent.getStringExtra("url");
        link = intent.getStringExtra("origin");

        title = intent.getStringExtra("title");
        this.setTitle("");

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                //ArticleActivity.this.setTitle(view.getTitle());
            }

            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
                if (url.indexOf("http://www.infoq.com/cn/articles/") == 0) {
                    url = "http://ganhuo.tiaoshei.com/infoq/arti/?url=" + url;
                    webView.loadUrl(url);

                    return null;
                } else if (url.indexOf("http://www.infoq.om/cn/news/") == 0) {
                    url = "http://ganhuo.tiaoshei.com/infoq/arti/?url=" + url;
                    view.loadUrl(url);
                    return null;
                }
                return null;
            }

        });
        loadUrl();
    }

    public void loadUrl() {
        webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);

        webView.loadUrl(artiUrl);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
            webView.goBack();// 返回前一个页面
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.article, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.share:
                intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT, title);
                intent.putExtra(Intent.EXTRA_TEXT, link);

                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(Intent.createChooser(intent, "分享文章"));
                break;
            case R.id.browser:
                Uri uri = Uri.parse(link);
                intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
                break;
            case android.R.id.home:
                super.onBackPressed();
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
