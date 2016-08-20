package com.huimang.framework;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.huimang.ganhuo.R;
import com.huimang.ganhuo.bean.Article;
import com.sch.rfview.AnimRFRecyclerView;
import com.sch.rfview.decoration.DividerItemDecoration;
import com.sch.rfview.manager.AnimRFLinearLayoutManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by ronnie on 16/8/13.
 */
public abstract class BaseFragment extends Fragment {

    OkHttpClient client = new OkHttpClient();
    public String nextUrl;
    public String defaultUrl;

    public AnimRFRecyclerView mRecyclerView;
    public List<Article> list = new ArrayList<Article>();
    private View footerView;

    //控件是否已经初始化
    private boolean isCreateView = false;
    //是否已经加载过数据
    private boolean isLoadData = false;

    /**
     * 请求网页
     *
     * @param url
     * @return
     * @throws IOException
     */
    public String request(String url) throws IOException {
        Log.v("request", url);
        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        nextUrl = defaultUrl;
        Fresco.initialize(this.getContext());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getUserVisibleHint()) {
            lazyLoad();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (mRecyclerView == null) {
            // 自定义的RecyclerView, 也可以在布局文件中正常使用
            mRecyclerView = new AnimRFRecyclerView(this.getContext());
            mRecyclerView.setBackgroundColor(Color.WHITE);
            // 脚部
            footerView = LayoutInflater.from(getActivity()).inflate(R.layout.footer_view, null);

            // 使用重写后的ƒ线性布局管理器
            AnimRFLinearLayoutManager manager = new AnimRFLinearLayoutManager(getActivity());
            mRecyclerView.setLayoutManager(manager);
            mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), manager.getOrientation(), true));
//            // 添加头部和脚部，如果不添加就使用默认的头部和脚部
//            mRecyclerView.addHeaderView(headerView);
            // 设置头部的最大拉伸倍率，默认1.5f，必须写在setHeaderImage()之前
            mRecyclerView.setScaleRatio(1.7f);
            // 设置下拉时拉伸的图片，不设置就使用默认的
//            mRecyclerView.setHeaderImage((ImageView) headerView.findViewById(R.id.iv_hander));
            mRecyclerView.addFootView(footerView);
            // 设置刷新动画的颜色
            mRecyclerView.setColor(R.color.primary, R.color.primaryDark);
            // 设置头部恢复动画的执行时间，默认500毫秒
            mRecyclerView.setHeaderImageDurationMillis(300);
            // 设置拉伸到最高时头部的透明度，默认0.5f
            mRecyclerView.setHeaderImageMinAlpha(0.6f);

            mRecyclerView.addItemDecoration(new com.huimang.framework.DividerItemDecoration(
                    this.getContext(),
                    LinearLayoutManager.VERTICAL));
            mRecyclerView.setPadding(8, 4, 8, 4);

            this.initRecyclerView();
        }

        isCreateView = true;
        return mRecyclerView;
    }

    /**
     * 初始化recyclerView
     */
    public abstract void initRecyclerView();

    /**
     * 延迟加载数据
     */
    public void lazyLoad() {
        if (isLoadData) {
            return;
        }
        isLoadData = true;
        mRecyclerView.setRefresh(true);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isCreateView) {
            lazyLoad();
        }
    }

    public void refreshComplate() {
        mRecyclerView.getAdapter().notifyDataSetChanged();
        mRecyclerView.refreshComplate();
    }

    public void loadMoreComplate() {
        mRecyclerView.getAdapter().notifyDataSetChanged();
        mRecyclerView.loadMoreComplate();
    }
}
