package com.aarondesign.healthgreen.Acitivitys;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.aarondesign.healthgreen.Application.SysApplication;
import com.aarondesign.healthgreen.ModifyView.ClearEditText;
import com.aarondesign.healthgreen.R;
import com.aarondesign.healthgreen.Static.CarConfig;
import com.aarondesign.healthgreen.Static.Configs;
import com.aarondesign.healthgreen.Static.RegisterConfig;
import com.aarondesign.healthgreen.Static.UserConfig;
import com.aarondesign.healthgreen.Util.MySharedPreferencesUtils;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by Aaron on 2015/11/5 0005.
 */
public class Settings extends Activity implements View.OnClickListener, GestureDetector.OnGestureListener {

    private Intent translateHome;
    private Intent translateBack;
    private Intent translateRegister;
    private Intent translateContents;
    private Button ivSettingSet;
    private ToggleButton isCarHave;
    private View carLine;
    private LinearLayout carSetting;

    private ClearEditText name;
    private EditText age;
    private EditText weight;
    private EditText height;
    private EditText fvc;
    private Spinner carKind;
    private RadioGroup radioGroup;
    private int carKind_id = 1;
    private int gender = 1;
    private String city = "杭州";

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case RegisterConfig.REGISTER_SUCCESS:
                    Toast.makeText(Settings.this, "设置成功！", Toast.LENGTH_SHORT).show();
                    translate(Configs.TRANSLATE_HOME);
                    break;
                case RegisterConfig.REGISTER_FAILURE:
                    Toast.makeText(Settings.this, "网络不佳请重试！", Toast.LENGTH_SHORT).show();
                    break;
                case RegisterConfig.REGISTER_ERROR:

                    break;
            }
        }
    };

    private GestureDetector detector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        SysApplication.getInstance().addActivity(this);

        init();
        findView();
        initEvent();

    }

    private void init() {
        detector = new GestureDetector(this, this);
    }

    private void findView() {
        name = (ClearEditText) findViewById(R.id.input_user);
        age = (EditText) findViewById(R.id.input_ages);
        weight = (EditText) findViewById(R.id.input_weight);
        height = (EditText) findViewById(R.id.input_height);
        fvc = (EditText) findViewById(R.id.input_fvc);
        carKind = (Spinner) findViewById(R.id.setting_spinner_car_kind);
        carKind.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (carKind == parent.getAdapter()) {
                    carKind_id = position + 2;
                    Log.d("carKind_id_selected", "" + carKind_id);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                carKind_id = 2;
                Log.d("carKind_id_no_select", "" + carKind_id);
            }
        });

        ivSettingSet = (Button) findViewById(R.id.iv_setting_set);
        carLine = findViewById(R.id.setting_car_line);
        carSetting = (LinearLayout) findViewById(R.id.setting_have_car);

        radioGroup = (RadioGroup) findViewById(R.id.rg_sex);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                gender = radioGroup.getCheckedRadioButtonId() + 2;
                Log.d("user_gender", "" + gender);
            }
        });

        isCarHave = (ToggleButton) findViewById(R.id.setting_car_have);
        isCarHave.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isCarHave.isChecked()) {
                    isCarHave.setBackgroundDrawable(getResources().getDrawable(R.drawable.icon_btn_l_slip));
                    carLine.setVisibility(View.INVISIBLE);
                    carSetting.setVisibility(View.INVISIBLE);
                    carKind_id = 1;
                } else {
                    isCarHave.setBackgroundDrawable(getResources().getDrawable(R.drawable.icon_btn_r_slip));
                    carLine.setVisibility(View.VISIBLE);
                    carSetting.setVisibility(View.VISIBLE);
                    carKind_id = carKind.getSelectedItemPosition() + 2;
                    Log.d("user_carKind_id", "" + carKind_id);
                }
            }
        });
    }

    private void initEvent() {
        ivSettingSet.setOnClickListener(this);
        if (Configs.REGISTER_STATUS != getIntent().getExtras().getInt("状态")) {
            name.setText((String) MySharedPreferencesUtils.get(this, UserConfig.USER_NAME, Configs.TYPE_STRING));
            weight.setText((String) MySharedPreferencesUtils.get(this, UserConfig.USER_WEIGHT, Configs.TYPE_STRING));
            height.setText((String) MySharedPreferencesUtils.get(this, UserConfig.USER_HEIGHT, Configs.TYPE_STRING));
            age.setText((String) MySharedPreferencesUtils.get(this, UserConfig.USER_AGE, Configs.TYPE_STRING));
            fvc.setText((String) MySharedPreferencesUtils.get(this, UserConfig.USER_FVC, Configs.TYPE_STRING));
            carKind.setSelection((int) MySharedPreferencesUtils.get(this, UserConfig.USER_CARKIND_ID, Configs.TYPE_INTEGER) - 2);
            radioGroup.getChildAt((int) MySharedPreferencesUtils.get(this, UserConfig.USER_GENDER, Configs.TYPE_INTEGER) - 1).setSelected(true);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            translate(Configs.TRANSLATE_BACK);
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_setting_set:
                if ("".equals(name.getText().toString())) {
                    Toast.makeText(this, "请输入您的用户名称", Toast.LENGTH_SHORT).show();
                } else if ("".equals(height.getText().toString())) {
                    Toast.makeText(this, "请输入您的身高", Toast.LENGTH_SHORT).show();
                } else if ("".equals(weight.getText().toString())) {
                    Toast.makeText(this, "请输入您的体重", Toast.LENGTH_SHORT).show();
                } else if ("".equals(age.getText().toString())) {
                    Toast.makeText(this, "请输入您的年龄", Toast.LENGTH_SHORT).show();
                } else if ("".equals(fvc.getText().toString())) {
                    Toast.makeText(this, "请输入您的肺活量", Toast.LENGTH_SHORT).show();
                } else {
                    getTranslateStatus();
                }
                break;
        }
    }

    public void getTranslateStatus() {

        MySharedPreferencesUtils.save(this, UserConfig.USER_NAME, name.getText().toString());
        MySharedPreferencesUtils.save(this, UserConfig.USER_GENDER, gender);
        MySharedPreferencesUtils.save(this, UserConfig.USER_HEIGHT, height.getText().toString());
        MySharedPreferencesUtils.save(this, UserConfig.USER_WEIGHT, weight.getText().toString());
        MySharedPreferencesUtils.save(this, UserConfig.USER_AGE, age.getText().toString());
        MySharedPreferencesUtils.save(this, UserConfig.USER_FVC, fvc.getText().toString());
        MySharedPreferencesUtils.save(this, UserConfig.USER_CARKIND_ID, carKind_id);
        MySharedPreferencesUtils.save(this, UserConfig.USER_CITY, city);

        Intent i = getIntent();
        int status = i.getExtras().getInt("状态");
        Log.d("msg", "状态 " + status);
        if (Configs.USER_STATUS == status) {
            saveAndModify();
        } else if (Configs.REGISTER_STATUS == status) {
            saveAndUpload();
        }
    }

    public void saveAndModify() {
        int user_id = (int) MySharedPreferencesUtils.get(this, UserConfig.USER_ID, Configs.TYPE_INTEGER);
        String phone = (String) (MySharedPreferencesUtils.get(this, UserConfig.USER_PHONE, Configs.TYPE_STRING));
        String password = (String) (MySharedPreferencesUtils.get(this, UserConfig.USER_PASSWORD, Configs.TYPE_STRING));
        String url = Configs.URL_HEAD + Configs.URL_MODIFY_USER + "phone=" + phone + "&password=" + password + "&name=" + name.getText().toString()
                + "&gender=" + gender + "&weight=" + weight.getText().toString() + "&height=" + height.getText().toString() + "&age=" + age.getText().toString()
                + "&fvc=" + fvc.getText().toString() + "&city=" + city + "&carkind_id=" + carKind_id + "&user_id=" + user_id;
        Log.d("url_details", url);
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
//                String example = response.toString();
                String res = response.body().string();
                Log.d("Setting_response_body", "返回的body：" + res);
                getStatus(res);
            }
        });
    }

    public void saveAndUpload() {

        String phone = (String) (MySharedPreferencesUtils.get(this, UserConfig.USER_PHONE, Configs.TYPE_STRING));
        String register_datetime = (String) (MySharedPreferencesUtils.get(this, UserConfig.USER_REGISTER_DATETIME, Configs.TYPE_STRING));
        String password = (String) (MySharedPreferencesUtils.get(this, UserConfig.USER_PASSWORD, Configs.TYPE_STRING));
        String url = Configs.URL_HEAD + Configs.URL_UPLOAD_USER + "phone=" + phone + "&password=" + password + "&name=" + name.getText().toString()
                + "&gender=" + gender + "&weight=" + weight.getText().toString() + "&height=" + height.getText().toString() + "&age=" + age.getText().toString()
                + "&fvc=" + fvc.getText().toString() + "&city=" + city + "&register_datetime=" + register_datetime + "&carkind_id=" + carKind_id;
        Log.d("url_details", url);
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
//                String example = response.toString();
                String res = response.body().string();
                Log.d("Setting_response_body", "返回的body：" + res);
                getStatus(res);
            }
        });

    }

    private void getStatus(String json) {

        try {
            JSONObject jsonObject = new JSONObject(json);
            int status = jsonObject.getInt("status");
            Message message = new Message();
            if (RegisterConfig.REGISTER_SUCCESS == status) {
                if (Configs.REGISTER_STATUS == getIntent().getExtras().getInt("状态")) {
                    int user_id = jsonObject.getInt("id");
                    MySharedPreferencesUtils.save(Settings.this, UserConfig.USER_ID, user_id);
                }
                message.what = RegisterConfig.REGISTER_SUCCESS;
                handler.sendMessage(message);
            } else if (RegisterConfig.REGISTER_FAILURE == status) {
                message.what = RegisterConfig.REGISTER_FAILURE;
                handler.sendMessage(message);
            } else if (RegisterConfig.REGISTER_ERROR == status) {
                message.what = RegisterConfig.REGISTER_ERROR;
                handler.sendMessage(message);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void translate(int position) {
        long startTime = System.nanoTime();
        if (Configs.TRANSLATE_BACK == position) {
            if (null == translateBack) {
                translateBack = getIntent();
                int i = translateBack.getExtras().getInt("状态");
                translate(i);
            }
        } else if (Configs.TRANSLATE_HOME == position) {
            if (null == translateHome)
                translateHome = new Intent(this, Home.class);
            else {
                translateHome.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                translateHome.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            }
            startActivity(translateHome);
            overridePendingTransition(R.anim.ts_main_in, R.anim.ts_main_out);
        } else if (Configs.USER_STATUS == position) {
            if (null == translateContents)
                translateContents = new Intent(this, User.class);
            else {
                translateContents.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                translateContents.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            }
            startActivity(translateContents);
            overridePendingTransition(R.anim.ts_main_in_back, R.anim.ts_main_out_back);
        } else if (Configs.REGISTER_STATUS == position) {
            if (null == translateRegister)
                translateRegister = new Intent(this, Register.class);
            else {
                translateRegister.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                translateRegister.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            }
            startActivity(translateRegister);
            overridePendingTransition(R.anim.ts_main_in_back, R.anim.ts_main_out_back);
        }
        long time = System.nanoTime() - startTime;
        Log.d("msg", "代码运行时间：" + time);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return detector.onTouchEvent(event);
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
        if (e2.getY() - e1.getY() > 20 && Math.abs(velocityY) > 0) {
            translate(Configs.TRANSLATE_BACK);
        }
        return false;
    }


}
