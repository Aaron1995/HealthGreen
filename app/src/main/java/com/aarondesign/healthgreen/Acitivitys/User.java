package com.aarondesign.healthgreen.Acitivitys;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aarondesign.healthgreen.Application.SysApplication;
import com.aarondesign.healthgreen.GBean.GUser;
import com.aarondesign.healthgreen.R;
import com.aarondesign.healthgreen.Static.CarKindConfig;
import com.aarondesign.healthgreen.Static.Configs;
import com.aarondesign.healthgreen.Static.UserConfig;
import com.aarondesign.healthgreen.Util.MySharedPreferencesUtils;

import java.util.List;

/**
 * Created by Aaron on 2015/11/5 0005.
 */
public class User extends Activity implements View.OnClickListener, GestureDetector.OnGestureListener {

    private GestureDetector detector;
    private Intent translateBack;
    private Intent translateSetting;
    private ImageView contentIVSure;
    private TextView tvName;
    private TextView tvGender;
    private TextView tvWeight;
    private TextView tvHeight;
    private TextView tvAge;
    private TextView tvFvc;
    private TextView tvKind;
    private TextView tvPower;
    private TextView tvDisplacement;
    private TextView tvCarWeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        SysApplication.getInstance().addActivity(this);

        init();
        findView();
        initDatas();
        initEvent();
    }

    private void initDatas() {
        tvName.setText((String) MySharedPreferencesUtils.get(User.this, UserConfig.USER_NAME, Configs.TYPE_STRING));
        tvGender.setText(String.valueOf(MySharedPreferencesUtils.get(User.this, UserConfig.USER_GENDER, Configs.TYPE_INTEGER)));
        tvAge.setText((String) MySharedPreferencesUtils.get(User.this, UserConfig.USER_AGE, Configs.TYPE_STRING));
        tvWeight.setText((String) MySharedPreferencesUtils.get(User.this, UserConfig.USER_WEIGHT, Configs.TYPE_STRING));
        tvHeight.setText((String) MySharedPreferencesUtils.get(User.this, UserConfig.USER_HEIGHT, Configs.TYPE_STRING));
        tvFvc.setText((String) MySharedPreferencesUtils.get(User.this, UserConfig.USER_FVC, Configs.TYPE_STRING));
        tvDisplacement.setText((String) MySharedPreferencesUtils.get(User.this, CarKindConfig.DISPLACEMENT, Configs.TYPE_STRING));
        tvKind.setText((String) MySharedPreferencesUtils.get(User.this, CarKindConfig.KIND, Configs.TYPE_STRING));
        tvPower.setText((String) MySharedPreferencesUtils.get(User.this, CarKindConfig.POWER, Configs.TYPE_STRING));
        tvCarWeight.setText((String) MySharedPreferencesUtils.get(User.this, CarKindConfig.WEIGHT, Configs.TYPE_STRING));
    }

    private void findView() {
        contentIVSure = (ImageView) findViewById(R.id.user_sure);
        tvName = (TextView) findViewById(R.id.user_tv_name);
        tvAge = (TextView) findViewById(R.id.user_tv_ages);
        tvHeight = (TextView) findViewById(R.id.user_tv_height);
        tvWeight = (TextView) findViewById(R.id.user_tv_weight);
        tvGender = (TextView) findViewById(R.id.user_tv_gender);
        tvFvc = (TextView) findViewById(R.id.user_tv_fvc);
        tvKind = (TextView) findViewById(R.id.user_car_tv_kind);
        tvPower = (TextView) findViewById(R.id.user_car_tv_power);
        tvDisplacement = (TextView) findViewById(R.id.user_car_tv_displacement);
        tvCarWeight = (TextView) findViewById(R.id.user_car_tv_weight);
    }

    private void initEvent() {
        contentIVSure.setOnClickListener(this);
    }

    private void init() {
        detector = new GestureDetector(this, this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.user_sure:
                long startTime = System.nanoTime();
                translate(Configs.TRANSLATE_SETTING);
                long time = System.nanoTime() - startTime;
                Log.d("msg", "代码运行时间：" + time);
                break;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return detector.onTouchEvent(event);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0)
            translate(Configs.TRANSLATE_BACK);
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if (e2.getY() - e1.getY() > 20 && Math.abs(velocityY) > 0)
            translate(Configs.TRANSLATE_BACK);
        return false;
    }

    private void translate(int position) {
        if (Configs.TRANSLATE_BACK == position) {
            if (null == translateBack)
                translateBack = new Intent(this, Home.class);
            else {
                translateBack.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                translateBack.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            }
            startActivity(translateBack);
            overridePendingTransition(R.anim.ts_main_in_back, R.anim.ts_main_out_back);
        } else if (Configs.TRANSLATE_SETTING == position) {
            if (null == translateSetting) {
                translateSetting = new Intent();
                translateSetting.putExtra("状态", Configs.USER_STATUS);
                translateSetting.setClass(this, Settings.class);
            } else {
                translateSetting.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                translateSetting.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            }
            startActivity(translateSetting);
            overridePendingTransition(R.anim.ts_main_in, R.anim.ts_main_out);
        }
    }
}
