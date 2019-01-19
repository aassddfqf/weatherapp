package com.example.asfd.weatherapp_wang;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.edu.pku.wangluyue.bean.GuideAdapter;

public class GuideActivity extends Activity implements ViewPager.OnPageChangeListener {
    private ViewPager pageView;
    private List<View> views;
    private GuideAdapter guideAdapter;
    private ImageView[] dots;
    private int[] ids = {R.id.iv1, R.id.iv2};
    private Button btn;
    private boolean flag;

    void initDots() {
        dots = new ImageView[views.size()];
        for (int i = 0; i < views.size(); i++) {
            dots[i] = (ImageView) findViewById(ids[i]);
        }
    }

    private void initViews() {
        LayoutInflater inflater = LayoutInflater.from(this);
        views = new ArrayList<View>();
        views.add(LayoutInflater.from(this).inflate(R.layout.page1_guide, null));
        views.add(LayoutInflater.from(this).inflate(R.layout.page2_guide, null));
        guideAdapter = new GuideAdapter(this, views);
        pageView = (ViewPager) findViewById(R.id.guidePage);
        pageView.setAdapter(guideAdapter);
        pageView.setOnPageChangeListener(this);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //TODO 自动生成方法存根
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guide_layout);
        initViews();
        initDots();
        btn = (Button) views.get(1).findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(GuideActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int i) {
        for (int a = 0; a < ids.length; a++) {
            if (a == i)
                dots[a].setImageResource(R.drawable.page_indicator_focused);
            else
                dots[a].setImageResource(R.drawable.page_indicator_unfocused);
        }
    }

    //ViewPagerhua
    @Override
    public void onPageScrollStateChanged(int i) {
        switch (i) {
            case ViewPager.SCROLL_STATE_DRAGGING:
                flag = false;
                break;
            case ViewPager.SCROLL_STATE_SETTLING:
                flag = true;
                break;
            case ViewPager.SCROLL_STATE_IDLE:
                if (pageView.getCurrentItem() == pageView.getAdapter().getCount() - 1 && !flag) {
                    Intent ss = new Intent(GuideActivity.this, MainActivity.class);
                    startActivity(ss);
                    finish();
                }
                flag = true;
                break;
        }
    }
}


