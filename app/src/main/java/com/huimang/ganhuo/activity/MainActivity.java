package com.huimang.ganhuo.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.huimang.ganhuo.R;
import com.huimang.ganhuo.adapter.MyFragmentPagerAdapter;

public class MainActivity extends AppCompatActivity
        implements RadioGroup.OnCheckedChangeListener,
        ViewPager.OnPageChangeListener {

    private RadioGroup rg_tab;
    private RadioButton rb_news;
    private RadioButton rb_article;
    private RadioButton rb_presentation;
    private RadioButton rb_profile;
    private ViewPager vpager;
    private MyFragmentPagerAdapter mAdapter;

    public static final int PAGE_NEWS = 0;
    public static final int PAGE_ARTICLE = 1;
    public static final int PAGE_PRESENTATION = 2;
    public static final int PAGE_PROFILE = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager());
        bindViews();
        rb_news.setChecked(true);
    }

    private void bindViews() {
        rg_tab = (RadioGroup) findViewById(R.id.rg_tab_bar);
        rb_news = (RadioButton) findViewById(R.id.rb_news);
        rb_article = (RadioButton) findViewById(R.id.rb_article);
        rb_presentation = (RadioButton) findViewById(R.id.rb_presentation);
        rb_profile = (RadioButton) findViewById(R.id.rb_profile);

        rg_tab.setOnCheckedChangeListener(this);

        vpager = (ViewPager) findViewById(R.id.vpager);
        vpager.setAdapter(mAdapter);
        vpager.setCurrentItem(0);
        vpager.addOnPageChangeListener(this);
    }

    //重写ViewPager页面切换的处理方法
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        //state的状态有三个，0表示什么都没做，1正在滑动，2滑动完毕
        if (state == 2) {
            switch (vpager.getCurrentItem()) {
                case PAGE_NEWS:
                    rb_news.setChecked(true);
                    break;
                case PAGE_ARTICLE:
                    rb_article.setChecked(true);
                    break;
                case PAGE_PRESENTATION:
                    rb_presentation.setChecked(true);
                    break;
                case PAGE_PROFILE:
                    rb_profile.setChecked(true);
                    break;
            }
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        switch (i) {
            case R.id.rb_news:
                vpager.setCurrentItem(PAGE_NEWS);
                break;
            case R.id.rb_article:
                vpager.setCurrentItem(PAGE_ARTICLE);
                break;
            case R.id.rb_presentation:
                vpager.setCurrentItem(PAGE_PRESENTATION);
                break;
            case R.id.rb_profile:
                vpager.setCurrentItem(PAGE_PROFILE);
                break;
        }
    }
}