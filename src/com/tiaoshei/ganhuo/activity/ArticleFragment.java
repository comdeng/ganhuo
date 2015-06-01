package com.tiaoshei.ganhuo.activity;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.tiaoshei.fr.view.TsSwipeRefreshLayout;
import com.tiaoshei.ganhuo.adapter.ArticleListAdapter;
import com.tiaoshei.ganhuo.model.Article;
import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ronnie on 15/6/1.
 */
public class ArticleFragment extends Fragment  implements TsSwipeRefreshLayout.OnRefreshListener {
    List<Article> list;
    private ListView listView;
    private TsSwipeRefreshLayout refreshLayout;
    private String nextUrl;
    private boolean isLoading = false;

    private RelativeLayout failureLayout;
    ArticleListAdapter adapter;

    private final String DEFAULT_URL = "http://ganhuo.tiaoshei.com/infoq/article/?_of=json";

    private MainActivity pActivity = null;
    private View myView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (myView == null) {
            myView = inflater.inflate(R.layout.my_activity, container, false);
        }
        return myView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (list != null) {
            return;
        }

        pActivity = (MainActivity)this.getActivity();

        failureLayout = (RelativeLayout) pActivity.findViewById(R.id.failureLayout);

        Button retryBtn = (Button) pActivity.findViewById(R.id.retryButton);
        retryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pActivity.isNetworkAvailable()) {
                    loadUrl();
                }
            }
        });


        listView = (ListView) pActivity.findViewById(R.id.listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Article article = list.get(i);
                Intent intent = new Intent(pActivity, ArticleActivity.class);
                intent.putExtra("url", article.get__LINK__());
                Log.v("hapn", article.get__LINK__());
                intent.putExtra("origin", article.getUrl());
                intent.putExtra("title", article.getTitle());
                intent.putExtra("summary", article.getSummary());
                intent.putExtra("author", article.getAuthor());
                intent.putExtra("pubtime", article.getTime());
                intent.putExtra("reply", article.getReply());
                startActivity(intent);
            }
        });

        refreshLayout = (TsSwipeRefreshLayout) pActivity.findViewById(R.id.refreshLayout);
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setColorScheme(
                android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        refreshLayout.setmMode(TsSwipeRefreshLayout.Mode.BOTH);


        nextUrl = DEFAULT_URL;

        if (pActivity.isNetworkAvailable()) {
            failureLayout.setVisibility(View.GONE);
            loadUrl();
        }

    }

    private void loadUrl() {
        if (isLoading) {
            return;
        }

        isLoading = true;


        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(true);
            }
        }, 100);

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(nextUrl, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                failureLayout.setVisibility(View.GONE);
                refreshLayout.setRefreshing(false);

                if (response.optString("err").equals("hapn.ok")) {
                    JSONObject data = response.optJSONObject("data");
                    JSONArray items = data.optJSONArray("items");

                    JSONObject links = data.optJSONObject("__LINKS__");
                    nextUrl = links.optString("next");

                    if (list == null) {
                        list = new ArrayList<Article>();
                    }
                    for (int i = 0, l = items.length(); i < l; i++) {

                        JSONObject item = items.optJSONObject(i);
                        Article arti = new Article();
                        arti.set__LINK__(item.optString("__LINK__"));
                        arti.setTitle(item.optString("title"));
                        arti.setSummary(item.optString("summary"));
                        arti.setTime(item.optString("time"));
                        arti.setUrl(item.optString("url"));
                        arti.setReply(item.optInt("reply"));
                        arti.setAuthor(item.optJSONObject("author").optString("name"));

                        list.add(arti);
                    }

                    if (adapter == null) {
                        adapter = new ArticleListAdapter(pActivity);
                        adapter.setData(list);
                        listView.setAdapter(adapter);
                    } else {
                        //adapter.setData(list);
                        adapter.notifyDataSetChanged();
                    }
                }

            }

            @Override
            public void onFinish() {
                isLoading = false;
                super.onFinish();
            }
        });
    }


    public void onRefresh() {
        if (pActivity.isNetworkAvailable()) {
            failureLayout.setVisibility(View.GONE);
            nextUrl = DEFAULT_URL;
            adapter = null;

            loadUrl();
        } else {
            refreshLayout.setRefreshing(false);
            failureLayout.setVisibility(View.VISIBLE);
        }
    }

    public void onLoadMore() {
        if (pActivity.isNetworkAvailable()) {
            failureLayout.setVisibility(View.GONE);
            loadUrl();
        } else {
            refreshLayout.setRefreshing(false);
            failureLayout.setVisibility(View.VISIBLE);
        }
    }
}
