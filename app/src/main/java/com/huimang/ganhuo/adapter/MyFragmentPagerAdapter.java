package com.huimang.ganhuo.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.huimang.ganhuo.activity.MainActivity;
import com.huimang.ganhuo.fragment.ArticleFragment;
import com.huimang.ganhuo.fragment.NewsFragment;
import com.huimang.ganhuo.fragment.PresentationFragment;
import com.huimang.ganhuo.fragment.ProfileFragment;

/**
 * Created by ronnie on 16/8/10.
 */
public class MyFragmentPagerAdapter extends FragmentPagerAdapter {
    private final int PAGER_COUNT = 4;
    private NewsFragment newsFragment = null;
    private ArticleFragment articleFragment = null;
    private PresentationFragment presentationFragment = null;
    private ProfileFragment profileFragment = null;

    public MyFragmentPagerAdapter(FragmentManager fm) {
        super(fm);

        newsFragment = new NewsFragment();
        articleFragment = new ArticleFragment();
        presentationFragment = new PresentationFragment();
        profileFragment = new ProfileFragment();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        return super.instantiateItem(container, position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        System.out.println("position Destory" + position);
        super.destroyItem(container, position, object);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case MainActivity.PAGE_NEWS:
                return newsFragment;
            case MainActivity.PAGE_ARTICLE:
                return articleFragment;
            case MainActivity.PAGE_PRESENTATION:
                return presentationFragment;
            case MainActivity.PAGE_PROFILE:
                return profileFragment;
        }
        return null;
    }

    @Override
    public int getCount() {
        return PAGER_COUNT;
    }
}
