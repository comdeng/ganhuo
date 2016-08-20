package com.huimang.ganhuo.fragment;


import android.os.AsyncTask;

import com.huimang.framework.BaseFragment;
import com.huimang.ganhuo.adapter.ArticleListAdapter;
import com.huimang.ganhuo.bean.Article;
import com.sch.rfview.AnimRFRecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ronnie on 16/8/10.
 */
public class ArticleFragment extends BaseFragment {
    public String defaultUrl = "http://ganhuo.tiaoshei.com/infoq/article/";

    @Override
    public void initRecyclerView() {
        // 设置适配器
        mRecyclerView.setAdapter(new ArticleListAdapter(this.getActivity(), this.list));
        // 设置刷新和加载更多数据的监听，分别在onRefresh()和onLoadMore()方法中执行刷新和加载更多操作
        mRecyclerView.setLoadDataListener(new AnimRFRecyclerView.LoadDataListener() {
            @Override
            public void onRefresh() {
                new RetrieveFeedTask(true).execute(defaultUrl);
            }

            @Override
            public void onLoadMore() {
                new RetrieveFeedTask(true).execute(nextUrl);
            }
        });
    }


    class RetrieveFeedTask extends AsyncTask<String, Void, List> {

        private Exception exception;

        private boolean isRefresh;

        public RetrieveFeedTask(boolean isRefresh) {
            this.isRefresh = isRefresh;
        }

        protected List doInBackground(String... urls) {
            try {
                String result = request(urls[0]);
                JSONObject response = new JSONObject(result);
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
                        arti.setCover(item.optString("cover").replace(" ", "%20"));

                        list.add(arti);
                    }

                }

                return list;
            } catch (Exception e) {
                this.exception = e;
                return null;
            }
        }

        @Override
        protected void onPostExecute(List list) {
            if (isRefresh) {
                refreshComplate();
            } else {
                loadMoreComplate();
            }
        }
    }
}
