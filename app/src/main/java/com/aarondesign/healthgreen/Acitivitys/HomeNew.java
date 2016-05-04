package com.aarondesign.healthgreen.Acitivitys;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.aarondesign.healthgreen.Application.SysApplication;
import com.aarondesign.healthgreen.Fragments.CarFragment;
import com.aarondesign.healthgreen.Fragments.PersonFragment;
import com.aarondesign.healthgreen.Fragments.UserFragment;
import com.aarondesign.healthgreen.GreenDaoBackEnd.CarRepository;
import com.aarondesign.healthgreen.GreenDaoBackEnd.PersonRepository;
import com.aarondesign.healthgreen.ModifyView.HomeChartView;
import com.aarondesign.healthgreen.ModifyView.HomeRingView;
import com.aarondesign.healthgreen.ModifyView.HomeScrollView;
import com.aarondesign.healthgreen.R;
import com.aarondesign.healthgreen.Static.Configs;
import com.aarondesign.healthgreen.Static.HomeNewConfig;
import com.aarondesign.healthgreen.Util.DateUtils;
import com.aarondesign.healthgreen.Util.MySharedPreferencesUtils;
import com.aarondesign.healthgreen.Util.TransitionsUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aaron on 2016/4/12 0012.
 */
public class HomeNew extends FragmentActivity implements View.OnClickListener {

    private int screen_width, screen_height;
    private int show_status = HomeNewConfig.HOME_PERSON_STATUS;
    private RelativeLayout titleRL;
    private RelativeLayout mapRL;
    private RelativeLayout hsvRL;
    private RelativeLayout hsvContentRL;
    private RelativeLayout hsvUnderlineRL;
    private RelativeLayout hsvRingViewRL;
    private RelativeLayout hsvTipRL;
    private RelativeLayout hsvChartRl;
    private RelativeLayout hsvTipBlankRecordRL;
    private HorizontalScrollView mHorizontalScrollView;
    private LinearLayout tab;
    private HomeScrollView hsv;
    private View underline;
    private HomeChartView mHomeChartView;

    private ViewPager mViewPager;
    private PersonFragment mPersonFrament;
    private CarFragment mCarFragment;
    private UserFragment mUserFragment;
    private List<Fragment> mFragmentList;
    private int currenttab = -1;                            // the item of mViewPager

    private TextView mPersonTV;
    private TextView mCarTV;
    private TextView mUserTV;
    private TextView mTipTV;
    private TextView mRecordTV;
    private TextView mMap;

    private HomeRingView mHomeRingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_new);
        SysApplication.getInstance().addActivity(this);

        getWidthAndHeight();
        findView();
//        initLayout();
        initViewOnEvent();
        initContentRL();
        initViewPagerFragment();
        initChartView();
    }

    private void initViewOnEvent() {
        mMap.setOnClickListener(this);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            getWidthAndHeight();
            initLayout();
        }
    }

    private void getWidthAndHeight() {
        Rect outRect = new Rect();
        this.getWindow().getDecorView().getWindowVisibleDisplayFrame(outRect);
        screen_width = outRect.width();
        screen_height = outRect.height();
        Log.d("HomeNew", "===screen_width,screen_height===" + screen_width + "," + screen_height);
    }

    private void initContentRL() {
        mHomeRingView.setData(20.3f, HomeNewConfig.HOME_PERSON_STATUS);
    }


    private void findView() {
        titleRL = (RelativeLayout) findViewById(R.id.home_title_rl);
        hsv = (HomeScrollView) findViewById(R.id.home_hsv);
        mapRL = (RelativeLayout) findViewById(R.id.home_map_rl);
        hsvRL = (RelativeLayout) findViewById(R.id.home_hsv_rl);
        hsvContentRL = (RelativeLayout) findViewById(R.id.home_hsv_rl_content);
        tab = (LinearLayout) findViewById(R.id.home_hsv_tab);
        hsvUnderlineRL = (RelativeLayout) findViewById(R.id.home_hsv_rl_underline);
        mViewPager = (ViewPager) findViewById(R.id.home_hsv_viewpager);
        hsvRingViewRL = (RelativeLayout) findViewById(R.id.home_hsv_content_rl_ringview);
        hsvTipRL = (RelativeLayout) findViewById(R.id.home_hsv_content_rl_tip);
        hsvTipBlankRecordRL = (RelativeLayout) findViewById(R.id.home_hsv_content_ll_blank_3);
        hsvChartRl = (RelativeLayout) findViewById(R.id.home_hsv_content_rl_chart);

        mPersonTV = (TextView) findViewById(R.id.home_hsv_tab_person);
        mCarTV = (TextView) findViewById(R.id.home_hsv_tab_car);
        mUserTV = (TextView) findViewById(R.id.home_hsv_tab_user);
        mHomeRingView = (HomeRingView) findViewById(R.id.home_hsv_ringview);
        mHomeChartView = (HomeChartView) findViewById(R.id.home_hsv_hsv_chartview);
        mTipTV = (TextView) findViewById(R.id.home_hsv_tv_tip);
        mRecordTV = (TextView) findViewById(R.id.home_hsv_tv_record);
        mHorizontalScrollView = (HorizontalScrollView) findViewById(R.id.home_hsv_hsv_chart);
        underline = findViewById(R.id.home_hsv_tab_underline);

        mMap = (TextView) findViewById(R.id.homenew_tv_map);
    }

    private void initLayout() {
//        screen_width = (int) MySharedPreferencesUtils.get(this, Configs.SCREEN_WIDTH, Configs.TYPE_INTEGER);
//        screen_height = (int) MySharedPreferencesUtils.get(this, Configs.SCREEN_HEIGHT, Configs.TYPE_INTEGER);
        int h1 = (int) Math.round(screen_height / 20.0);         // unit of layout
        Log.d("HomeNew", "===h1===" + h1);
        // LayoutParams of screen
        RelativeLayout.LayoutParams lp1 = new RelativeLayout.LayoutParams(screen_width, 2 * h1);         // LayoutParam of rl_title(康行)
        lp1.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(screen_width, 17 * h1);        // LayoutParam of ScrollView
        lp2.addRule(RelativeLayout.BELOW, titleRL.getId());
        lp2.addRule(RelativeLayout.ABOVE, mapRL.getId());
        RelativeLayout.LayoutParams lp3 = new RelativeLayout.LayoutParams(screen_width, h1);             // LayoutParam of rl_map
        lp3.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        // LayoutParams of ScrollView
        HomeScrollView.LayoutParams lp4 = new HomeScrollView.LayoutParams(screen_width, 33 * h1);        // LayoutParam of rl_ScrollView
        RelativeLayout.LayoutParams lp5 = new RelativeLayout.LayoutParams(screen_width, 16 * h1 - 1);     // LayoutParam of rl_content_above_tab
        lp5.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        RelativeLayout.LayoutParams lp6 = new RelativeLayout.LayoutParams(screen_width, h1 + 3);             // LayoutParam of ll_tab
        lp6.addRule(RelativeLayout.BELOW, hsvContentRL.getId());
        RelativeLayout.LayoutParams lp7 = new RelativeLayout.LayoutParams(screen_width, 1);              // LayoutParam of rl_underline
        lp7.addRule(RelativeLayout.BELOW, tab.getId());
        RelativeLayout.LayoutParams lp8 = new RelativeLayout.LayoutParams(screen_width, 16 * h1);        // LayoutParam of viewpager
        lp8.addRule(RelativeLayout.BELOW, hsvUnderlineRL.getId());
        // LayoutParams of rl_content
        RelativeLayout.LayoutParams lp9 = new RelativeLayout.LayoutParams(screen_width, 8 * h1);         // LayoutParam of rl_ringview
        lp9.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        RelativeLayout.LayoutParams lp10 = new RelativeLayout.LayoutParams(screen_width, 2 * h1);        // LayoutParam of rl_tip
        lp10.addRule(RelativeLayout.BELOW, hsvRingViewRL.getId());
//        RelativeLayout.LayoutParams lp11 = new RelativeLayout.LayoutParams(screen_width,h1);            // LayoutParam of rl_blank
//        lp11.addRule(RelativeLayout.BELOW,hsvTipRL.getId());
        RelativeLayout.LayoutParams lp12 = new RelativeLayout.LayoutParams(screen_width, 6 * h1);        // LayoutParam of rl_chart
        lp12.addRule(RelativeLayout.BELOW, hsvTipRL.getId());

        titleRL.setLayoutParams(lp1);
        hsv.setLayoutParams(lp2);
        mapRL.setLayoutParams(lp3);
        hsvRL.setLayoutParams(lp4);
        hsvContentRL.setLayoutParams(lp5);
        tab.setLayoutParams(lp6);
        hsvUnderlineRL.setLayoutParams(lp7);
        mViewPager.setLayoutParams(lp8);
        hsvRingViewRL.setLayoutParams(lp9);
        hsvTipRL.setLayoutParams(lp10);
//        hsvTipBlankRecordRL.setLayoutParams(lp11);
        hsvChartRl.setLayoutParams(lp12);
        hsv.post(new Runnable() {
            @Override
            public void run() {
                hsv.scrollTo(0, 0);
            }
        });
    }

    private void initViewPagerFragment() {
        mFragmentList = new ArrayList<>();
        mPersonFrament = new PersonFragment();
        mCarFragment = new CarFragment();
        mUserFragment = new UserFragment();
        mFragmentList.add(mPersonFrament);
        mFragmentList.add(mCarFragment);
        mFragmentList.add(mUserFragment);
        mPersonTV.setOnClickListener(this);
        mCarTV.setOnClickListener(this);
        mUserTV.setOnClickListener(this);
        mViewPager.setAdapter(new MyFrageStatePagerAdapter(getSupportFragmentManager()));
    }

    private void initChartView() {
        if (HomeNewConfig.HOME_CAR_STATUS == show_status) {
            mHomeChartView.setStatus(show_status, CarRepository.getAllCarBeans(this));
        } else if (HomeNewConfig.HOME_PERSON_STATUS == show_status) {
            mHomeChartView.setStatus(show_status, PersonRepository.getAllPersonBeans(this));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.home_hsv_tab_person:
                Log.d("HomeNew", "===触发onClick事件 person===");
                changeView(HomeNewConfig.HOME_PERSON_STATUS);
                break;
            case R.id.home_hsv_tab_car:
                Log.d("HomeNew", "===触发onClick事件 car===");
                changeView(HomeNewConfig.HOME_CAR_STATUS);
                break;
            case R.id.home_hsv_tab_user:
                Log.d("HomeNew", "===触发onClick事件 user===");
                changeView(2);
                break;
            case R.id.homenew_tv_map:
                TransitionsUtil.TranslateOfActivity(this, Map.class, Configs.TRANSLATE_STYLE_MAIN);
                break;
        }
    }

    private class MyFrageStatePagerAdapter extends FragmentStatePagerAdapter

    {

        public MyFrageStatePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        /**
         * 每次更新完成ViewPager的内容后，调用该接口，此处复写主要是为了让导航按钮上层的覆盖层能够动态的移动
         */



        @Override
        public void finishUpdate(ViewGroup container) {
            super.finishUpdate(container);//这句话要放在最前面，否则会报错
            //获取当前的视图是位于ViewGroup的第几个位置，用来更新对应的覆盖层所在的位置
            int currentItem = mViewPager.getCurrentItem();
            changeView(currentItem);
//                if (currentItem == currenttab) {
//                    return;
//                }
//            imageMove(mViewPager.getCurrentItem());
//            currenttab = mViewPager.getCurrentItem();
        }

    }

    private void changeTextColor(int status) {
        if (status == HomeNewConfig.HOME_PERSON_STATUS) {
            mPersonTV.setTextColor(getResources().getColor(R.color.color_text_blue));
            mCarTV.setTextColor(getResources().getColor(R.color.color_text_gray));
            mUserTV.setTextColor(getResources().getColor(R.color.color_text_gray));
        } else if (status == HomeNewConfig.HOME_CAR_STATUS) {
            mPersonTV.setTextColor(getResources().getColor(R.color.color_text_gray));
            mCarTV.setTextColor(getResources().getColor(R.color.color_text_blue));
            mUserTV.setTextColor(getResources().getColor(R.color.color_text_gray));
        } else if (status == 2) {
            mPersonTV.setTextColor(getResources().getColor(R.color.color_text_gray));
            mCarTV.setTextColor(getResources().getColor(R.color.color_text_gray));
            mUserTV.setTextColor(getResources().getColor(R.color.color_text_blue));
        }
    }

    /**
     * 移动覆盖层
     *
     * @param moveToTab 目标Tab，也就是要移动到的导航选项按钮的位置
     *                  第一个导航按钮对应0，第二个对应1，以此类推
     */
    private void imageMove(int moveToTab) {
        int startPosition = 0;
        int movetoPosition = 0;

        startPosition = currenttab * (screen_width / 3);
        movetoPosition = moveToTab * (screen_width / 3);
        //平移动画
        TranslateAnimation translateAnimation = new TranslateAnimation(startPosition, movetoPosition, 0, 0);
        translateAnimation.setFillAfter(true);
        translateAnimation.setDuration(200);
        underline.startAnimation(translateAnimation);
    }


    private void changeView(int desTab) {
        mViewPager.setCurrentItem(desTab, true);
        changeTextColor(desTab);
        changeContent(desTab);
        Log.d("HomeNew", "===desTab===" + desTab);
    }

    private void changeContent(int desTab) {
        switch (desTab) {
            case HomeNewConfig.HOME_PERSON_STATUS:
                mHomeRingView.setData((float) PersonRepository.getStayAndInhaleWithDate(this, DateUtils.getToday())[0], HomeNewConfig.HOME_PERSON_STATUS);
                mHomeChartView.setStatus(HomeNewConfig.HOME_PERSON_STATUS, PersonRepository.getAllPersonBeans(this));
                break;
            case HomeNewConfig.HOME_CAR_STATUS:
                mHomeRingView.setData((float) CarRepository.getExhaustAndGasolineWithDate(this, DateUtils.getToday())[0], HomeNewConfig.HOME_CAR_STATUS);
                mHomeChartView.setStatus(HomeNewConfig.HOME_CAR_STATUS, CarRepository.getAllCarBeans(this));
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("HomeNew", "===hsvRl,tab,viewpager,content===" + hsvRL.getHeight() + "," + tab.getHeight() + "," + mViewPager.getHeight() + "," + hsvContentRL.getHeight());
    }
}
