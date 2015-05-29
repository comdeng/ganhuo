package com.tiaoshei.ganhuo.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.*;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import org.apache.http.Header;

import java.io.*;

/**
 * Created by ronnie on 15/5/19.
 */
public class ArticleActivity extends Activity implements SwipeRefreshLayout.OnRefreshListener{
    private WebView webView;
    private String link;
    private String title;
    private String summary;
    private String url;
    private static String layout = null;
    private SwipeRefreshLayout swipeLayout;
    private TextView refreshBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar ab = getActionBar();
        ab.setDisplayShowHomeEnabled(false);
        this.setContentView(R.layout.article);

        swipeLayout = (SwipeRefreshLayout)this.findViewById(R.id.swipeLayout);
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                swipeLayout.setRefreshing(true);
//            }
//        }, 100);

        webView = (WebView)findViewById(R.id.webView);
        if (this.isNetworkAvailable(this)) {
            webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        } else {
            webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        }
        refreshBtn = (TextView)findViewById(R.id.refreshBtn);

        refreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                webView.setVisibility(View.VISIBLE);
                refreshBtn.setVisibility(View.INVISIBLE);

                swipeLayout.setRefreshing(true);
                loadUrl();
            }
        });

        Intent intent = this.getIntent();
        url = intent.getStringExtra("url");
        link = intent.getStringExtra("origin");
        title = intent.getStringExtra("title");
        summary = intent.getStringExtra("summary");

        this.setTitle(title);

        if (layout == null) {
            layout = getFromRaw("/assets/article.layout.html");
        }

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                ArticleActivity.this.setTitle(view.getTitle());

                swipeLayout.setRefreshing(false);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                // TODO Auto-generated method stub
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        swipeLayout.setRefreshing(true);
//                    }
//                }, 100);
//                view.loadUrl(url);
//                return true;
                return false;
            }
        });

        loadUrl();
    }

    public void loadUrl()
    {
        if (!this.isNetworkAvailable(this) ) {
            swipeLayout.setRefreshing(false);
            Toast.makeText(getApplicationContext(), "网络中断", Toast.LENGTH_SHORT).show();
            return;
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeLayout.setRefreshing(true);
            }
        }, 100);

        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(10);
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                String content = new String(bytes);
                content = layout
                        .replace("{{.title}}", title)
                        .replace("{{.description}}", summary)
                        .replace("{{.content}}", content);
                webView.loadData(content, "text/html; charset=utf-8", null);
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                swipeLayout.setRefreshing(false);
                Toast.makeText(getApplicationContext(), "载入失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
            webView.goBack();// 返回前一个页面
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 获取原始内容
     * @param path
     * @return
     */
    public String getFromRaw(String path){
        try {
            InputStream is = getClass().getResourceAsStream(path);
            ByteArrayOutputStream bs= new ByteArrayOutputStream();
            int ch;
            while ( (ch = is.read()) != -1) {
                bs.write(ch);
            }
            String result = bs.toString();
            bs.close();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
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
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                webView.loadUrl(webView.getUrl());
//            }
//        }, 100);
        loadUrl();
    }

    /**
     * 检查当前网络是否可用
     *
     * @return
     */

    public boolean isNetworkAvailable(Activity activity)
    {
        Context context = activity.getApplicationContext();
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager == null)
        {
            return false;
        }
        else
        {
            // 获取NetworkInfo对象
            NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();

            if (networkInfo != null && networkInfo.length > 0)
            {
                for (int i = 0; i < networkInfo.length; i++)
                {
                    System.out.println(i + "===状态===" + networkInfo[i].getState());
                    System.out.println(i + "===类型===" + networkInfo[i].getTypeName());
                    // 判断当前网络状态是否为连接状态
                    if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED)
                    {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
