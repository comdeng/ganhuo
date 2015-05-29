package com.tiaoshei.ganhuo.activity;

import com.tiaoshei.ganhuo.adapter.ArticleListAdapter;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.AdapterView;
import android.widget.ListView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.tiaoshei.ganhuo.model.Article;
import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MyActivity extends Activity {
    List<Article> list;
    private ListView listView;
    private SwipeRefreshLayout refreshLayout;
    private String nextUrl;
    private boolean isLoading = false;
    private int mYDown;
    private int mLastY;
    private int mTouchSlop;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.my_activity);

        listView = (ListView)this.findViewById(R.id.listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Article article = list.get(i);
                Intent intent = new Intent(MyActivity.this, ArticleActivity.class);
                intent.putExtra("url", article.get__LINK__());
                intent.putExtra("origin", article.getUrl());
                intent.putExtra("title", article.getTitle());
                intent.putExtra("summary", article.getSummary());
                startActivity(intent);
            }
        });

        refreshLayout = (SwipeRefreshLayout) this.findViewById(R.id.refreshLayout);
        refreshLayout.setRefreshing(true);

        ActionBar ab = getActionBar();
        ab.setDisplayShowHomeEnabled(false);

        mTouchSlop = ViewConfiguration.get(this.getApplicationContext()).getScaledTouchSlop();

        nextUrl = "http://ganhuo.tiaoshei.com/infoq/?_of=json";
        loadUrl();

    }

    private void loadUrl()
    {
        if (isLoading) {
            return;
        }
        isLoading = true;

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(nextUrl, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                if (response.optString("err").equals("hapn.ok")) {
                    JSONObject data = response.optJSONObject("data");
                    JSONArray items = data.optJSONArray("items");

                    JSONObject links = data.optJSONObject("__LINKS__");
                    nextUrl = links.optString("next");

                    if (list == null) {
                        list = new ArrayList<Article>();
                    }
                    for(int i = 0, l = items.length(); i < l; i++) {

                        JSONObject item = items.optJSONObject(i);
                        Article arti = new Article();
                        arti.set__LINK__(item.optString("__LINK__"));
                        arti.setTitle(item.optString("title"));
                        arti.setSummary(item.optString("summary"));
                        arti.setTime(item.optString("time"));
                        arti.setUrl(item.optString("url"));
                        arti.setAuthor(item.optJSONObject("author").optString("name"));

                        list.add(arti);
                    }

                    ArticleListAdapter adapter = new ArticleListAdapter(MyActivity.this);
                    adapter.setData(list);
                    listView.setAdapter(adapter);
                }
                refreshLayout.setRefreshing(false);
            }

            @Override
            public void onFinish() {
                isLoading = false;
                super.onFinish();
            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch(ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 按下
                mYDown = (int) ev.getRawY();
                break;

            case MotionEvent.ACTION_MOVE:
                // 移动
                mLastY = (int) ev.getRawY();
                break;
            case MotionEvent.ACTION_UP:
                if (isBottom() && isPullUp()) {
                    loadUrl();
                }
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 判断是否到了最底部
     */
    private boolean isBottom() {

        if (listView != null && listView.getAdapter() != null) {
            return listView.getLastVisiblePosition() == (listView.getAdapter().getCount() - 1);
        }
        return false;
    }

    /**
     * 是否是上拉操作
     *
     * @return
     */
    private boolean isPullUp() {
        return (mYDown - mLastY) >= mTouchSlop;
    }

}