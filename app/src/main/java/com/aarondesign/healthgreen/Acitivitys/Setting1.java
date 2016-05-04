package com.aarondesign.healthgreen.Acitivitys;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aarondesign.healthgreen.Application.SysApplication;
import com.aarondesign.healthgreen.R;
import com.aarondesign.healthgreen.Static.Configs;
import com.aarondesign.healthgreen.Static.UserConfig;
import com.aarondesign.healthgreen.Util.MySharedPreferencesUtils;
import com.aarondesign.healthgreen.Util.StringUtil;

/**
 * Created by Aaron on 2016/4/27 0027.
 */
public class Setting1 extends Activity implements View.OnClickListener {

    private TextView tvName;
    private TextView tvGender;
    private TextView tvAge;
    private TextView tvHeight;
    private TextView tvWeight;
    private TextView tvFvc;
    private TextView tvCarKind;
    private ImageView ivBack;

    private RelativeLayout rlName;
    private RelativeLayout rlGender;
    private RelativeLayout rlAge;
    private RelativeLayout rlHeight;
    private RelativeLayout rlWeight;
    private RelativeLayout rlFvc;
    private RelativeLayout rlCarKind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_1);
        SysApplication.getInstance().addActivity(this);

        findView();
        initViewEvent();
        initDatas();
    }

    private void findView() {
        rlName = (RelativeLayout) findViewById(R.id.setting_rl_1);
        rlGender = (RelativeLayout) findViewById(R.id.setting_rl_2);
        rlAge = (RelativeLayout) findViewById(R.id.setting_rl_3);
        rlHeight = (RelativeLayout) findViewById(R.id.setting_rl_4);
        rlWeight = (RelativeLayout) findViewById(R.id.setting_rl_5);
        rlFvc = (RelativeLayout) findViewById(R.id.setting_rl_6);
        rlCarKind = (RelativeLayout) findViewById(R.id.setting_rl_7);

        tvName = (TextView) findViewById(R.id.setting_tv_name);
        tvGender = (TextView) findViewById(R.id.setting_tv_gender);
        tvAge = (TextView) findViewById(R.id.setting_tv_age);
        tvHeight = (TextView) findViewById(R.id.setting_tv_height);
        tvWeight = (TextView) findViewById(R.id.setting_tv_weight);
        tvFvc = (TextView) findViewById(R.id.setting_tv_fvc);
        tvCarKind = (TextView) findViewById(R.id.setting_tv_carkind);

        ivBack = (ImageView) findViewById(R.id.setting_iv_back);
    }

    private void initViewEvent() {
        rlName.setOnClickListener(this);
        rlGender.setOnClickListener(this);
        rlAge.setOnClickListener(this);
        rlHeight.setOnClickListener(this);
        rlWeight.setOnClickListener(this);
        rlFvc.setOnClickListener(this);
        rlCarKind.setOnClickListener(this);
        ivBack.setOnClickListener(this);
    }

    private void initDatas() {
        if (Configs.LOGINED_SUCCESS == (int)MySharedPreferencesUtils.get(this, UserConfig.LOGIN_STATUS, Configs.TYPE_INTEGER)) {
            tvName.setText((String) MySharedPreferencesUtils.get(this, UserConfig.USER_NAME, Configs.TYPE_STRING));
            if (UserConfig.GENDER_MAN == (int)MySharedPreferencesUtils.get(this, UserConfig.USER_GENDER, Configs.TYPE_INTEGER)) {
                tvGender.setText("男");
            } else if (UserConfig.GENDER_WOMEN ==(int) MySharedPreferencesUtils.get(this, UserConfig.USER_GENDER, Configs.TYPE_INTEGER)) {
                tvGender.setText("女");
            }
            tvAge.setText((String) MySharedPreferencesUtils.get(this, UserConfig.USER_AGE, Configs.TYPE_STRING));
            tvHeight.setText((String) MySharedPreferencesUtils.get(this, UserConfig.USER_HEIGHT, Configs.TYPE_STRING));
            tvWeight.setText((String) MySharedPreferencesUtils.get(this, UserConfig.USER_WEIGHT, Configs.TYPE_STRING));
            tvFvc.setText((String) MySharedPreferencesUtils.get(this, UserConfig.USER_FVC, Configs.TYPE_STRING));
            tvCarKind.setText(StringUtil.getCarKind((int) MySharedPreferencesUtils.get(this, UserConfig.USER_CARKIND_ID, Configs.TYPE_INTEGER)));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.setting_rl_1:

                break;
            case R.id.setting_rl_2:

                break;
            case R.id.setting_rl_3:

                break;
            case R.id.setting_rl_4:

                break;
            case R.id.setting_rl_5:

                break;
            case R.id.setting_rl_6:

                break;
            case R.id.setting_rl_7:

                break;
            case R.id.setting_iv_back:

                break;
        }
    }
}
