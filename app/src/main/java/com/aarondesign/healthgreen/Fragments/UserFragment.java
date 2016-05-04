package com.aarondesign.healthgreen.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aarondesign.healthgreen.Acitivitys.Home;
import com.aarondesign.healthgreen.Acitivitys.Login;
import com.aarondesign.healthgreen.Acitivitys.Settings;
import com.aarondesign.healthgreen.Application.SysApplication;
import com.aarondesign.healthgreen.R;
import com.aarondesign.healthgreen.Static.CarKindConfig;
import com.aarondesign.healthgreen.Static.Configs;
import com.aarondesign.healthgreen.Static.UserConfig;
import com.aarondesign.healthgreen.Util.MySharedPreferencesUtils;
import com.aarondesign.healthgreen.Util.TransitionsUtil;

import java.util.zip.Inflater;

/**
 * Created by Aaron on 2016/4/13 0013.
 */
public class UserFragment extends Fragment implements View.OnClickListener {

    private View view;
    private Intent translateBack;
    private Intent translateSetting;
    private RelativeLayout rlUser;
    private TextView tvName;
    private TextView tvHeightAndWeight;
    private TextView tvAge;
    private TextView tvFvc;
    private TextView tvKind;
    private TextView tvPower;
    private TextView tvDisplacement;
    private TextView tvCarWeight;
    private ImageView ivGender;
    private Button btnUnregister;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_user_1, null);
        init();
        findView();
        initDatas();
        initEvent();
        return view;
    }

    private void initDatas() {
        tvName.setText((String) MySharedPreferencesUtils.get(getActivity(), UserConfig.USER_NAME, Configs.TYPE_STRING));
        if (UserConfig.GENDER_MAN == (int)MySharedPreferencesUtils.get(getActivity(), UserConfig.USER_GENDER, Configs.TYPE_INTEGER)) {
            ivGender.setImageResource(R.drawable.icon_man);
        } else if (UserConfig.GENDER_WOMEN == (int)MySharedPreferencesUtils.get(getActivity(), UserConfig.USER_GENDER, Configs.TYPE_INTEGER)) {
            ivGender.setImageResource(R.drawable.icon_woman);
        } else {
            ivGender.setImageResource(R.drawable.icon_man);
        }
        tvAge.setText((String) MySharedPreferencesUtils.get(getActivity(), UserConfig.USER_AGE, Configs.TYPE_STRING));
        String weight = (String) MySharedPreferencesUtils.get(getActivity(), UserConfig.USER_WEIGHT, Configs.TYPE_STRING);
        String height = (String) MySharedPreferencesUtils.get(getActivity(), UserConfig.USER_HEIGHT, Configs.TYPE_STRING);
        tvHeightAndWeight.setText(height + "cm/" + weight + "kg");
        tvFvc.setText((String) MySharedPreferencesUtils.get(getActivity(), UserConfig.USER_FVC, Configs.TYPE_STRING));
        tvDisplacement.setText((String) MySharedPreferencesUtils.get(getActivity(), CarKindConfig.DISPLACEMENT, Configs.TYPE_STRING));
        tvKind.setText((String) MySharedPreferencesUtils.get(getActivity(), CarKindConfig.KIND, Configs.TYPE_STRING));
        tvPower.setText((String) MySharedPreferencesUtils.get(getActivity(), CarKindConfig.POWER, Configs.TYPE_STRING));
        tvCarWeight.setText((String) MySharedPreferencesUtils.get(getActivity(), CarKindConfig.WEIGHT, Configs.TYPE_STRING));
    }

    private void findView() {
        rlUser = (RelativeLayout) view.findViewById(R.id.frg_user_rl1_1);
        ivGender = (ImageView) view.findViewById(R.id.frg_user_iv_gender);
        tvName = (TextView) view.findViewById(R.id.frg_user_tv_name);
        tvAge = (TextView) view.findViewById(R.id.frg_user_tv_age);
        tvHeightAndWeight = (TextView) view.findViewById(R.id.frg_user_tv_height_weight);
        tvFvc = (TextView) view.findViewById(R.id.frg_user_tv_fvc);
        tvKind = (TextView) view.findViewById(R.id.frg_user_tv_carkind);
        tvPower = (TextView) view.findViewById(R.id.frg_user_tv_power);
        tvDisplacement = (TextView) view.findViewById(R.id.frg_user_tv_displacement);
        tvCarWeight = (TextView) view.findViewById(R.id.frg_user_tv_weight);
        btnUnregister = (Button) view.findViewById(R.id.frg_user_btn_unregister);
    }

    private void initEvent() {
        rlUser.setOnClickListener(this);
        btnUnregister.setOnClickListener(this);
    }

    private void init() {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.frg_user_rl1_1:
                long startTime = System.nanoTime();
                translate(Configs.TRANSLATE_SETTING);
//                TransitionsUtil.TranslateOfActivity(getActivity(),Settings.class,Configs.TRANSLATE_STYLE_MAIN);
                long time = System.nanoTime() - startTime;
                Log.d("msg", "代码运行时间：" + time);
                break;
            case R.id.frg_user_btn_unregister:
                TransitionsUtil.TranslateOfActivity(getActivity(), Login.class, Configs.TRANSLATE_STYLE_MAIN);
                break;
        }
    }

    private void translate(int position) {
        if (Configs.TRANSLATE_BACK == position) {
            if (null == translateBack)
                translateBack = new Intent(getActivity(), Home.class);
            else {
                translateBack.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                translateBack.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            }
            startActivity(translateBack);
            getActivity().overridePendingTransition(R.anim.ts_main_in_back, R.anim.ts_main_out_back);
        } else if (Configs.TRANSLATE_SETTING == position) {
            if (null == translateSetting) {
                translateSetting = new Intent();
                translateSetting.putExtra("状态", Configs.USER_STATUS);
                translateSetting.setClass(getActivity(), Settings.class);
            } else {
                translateSetting.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                translateSetting.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            }
            startActivity(translateSetting);
            getActivity().overridePendingTransition(R.anim.ts_main_in, R.anim.ts_main_out);
        }
    }
}

