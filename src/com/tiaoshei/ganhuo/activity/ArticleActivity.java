package com.tiaoshei.ganhuo.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.view.*;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.tiaoshei.fr.activity.TsActivity;
import com.tiaoshei.fr.view.TsSwipeRefreshLayout;
import com.tiaoshei.ganhuo.model.Article;
import org.apache.http.Header;

import java.io.*;

/**
 * Created by ronnie on 15/5/19.
 */
public class ArticleActivity extends TsActivity implements TsSwipeRefreshLayout.OnRefreshListener{
    private WebView webView;
    private String link;
    private String title;
    private String artiUrl;
    private TsSwipeRefreshLayout swipeLayout;

    @Override
    public void initView() {

        this.setContentView(R.layout.article);

        swipeLayout = (TsSwipeRefreshLayout) this.findViewById(R.id.swipeLayout);
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorScheme(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        webView = (WebView) findViewById(R.id.webView);
        webView.getSettings().setDisplayZoomControls(false);
        webView.getSettings().setJavaScriptEnabled(true);


    }

    @Override
    public void loadData() {
        Intent intent = this.getIntent();
        artiUrl = intent.getStringExtra("url");
        link = intent.getStringExtra("origin");

        title = intent.getStringExtra("title");
        this.setTitle(title);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                ArticleActivity.this.setTitle(view.getTitle());

                swipeLayout.setRefreshing(false);
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

                return super.shouldInterceptRequest(view, url);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);

                swipeLayout.setRefreshing(false);
            }
        });

        loadUrl();
    }

    public void loadUrl()
    {
        boolean hasNw = this.isNetworkAvailable();
        if (hasNw) {
            webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        } else {
            webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeLayout.setRefreshing(true);
            }
        }, 100);

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
        inflater.inflate(R.menu.actionbar_menu, menu);
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
            case R.id.about:
                new AlertDialog.Builder(ArticleActivity.this).setTitle("关于")//设置对话框标题

                        .setMessage("干货，全都是干货！")
                        .show();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onRefresh()
    {
        loadUrl();
    }

    public void onLoadMore()
    {

    }
}
