package com.tiaoshei.ganhuo.activity;

import android.app.ActionBar;
import android.app.Activity;
import com.tiaoshei.fr.activity.TsActivity;
import com.tiaoshei.fr.view.TsTabListener;

/**
 * Created by ronnie on 15/6/1.
 */
public class MainActivity extends TsActivity {
    @Override
    public void initView() {
        ActionBar ab = this.getActionBar();
        ab.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        ab.setDisplayShowTitleEnabled(false);
        ab.setDisplayHomeAsUpEnabled(false);

        ActionBar.Tab tab = ab.newTab().setText(R.string.tab_news).setTabListener(new TsTabListener<NewsFragment>(this, "news", NewsFragment.class));
        ab.addTab(tab);

        tab = ab.newTab().setText(R.string.tab_article).setTabListener(new TsTabListener<ArticleFragment>(this, "article", ArticleFragment.class));
        ab.addTab(tab);

        tab = ab.newTab().setText(R.string.tab_presentation).setTabListener(new TsTabListener<PresentationFragment>(this, "presentation", PresentationFragment.class));
        ab.addTab(tab);
    }
}
